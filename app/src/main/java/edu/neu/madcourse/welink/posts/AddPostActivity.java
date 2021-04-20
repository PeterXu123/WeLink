package edu.neu.madcourse.welink.posts;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.Formatter;
import edu.neu.madcourse.welink.utility.PostDAO;

public class AddPostActivity extends AppCompatActivity implements LocationListener {

    private Switch locationSwitch;
    private EditText content;
    private String locationString;
    private String currUID;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    LocationManager locationManager;
    boolean isGPSEnable;
    boolean canGetLocation;
    Location location;
    public static final int REQUEST_LOCATION_CODE = 1;
    private int PICK_CODE = 19;
    private Uri uri;
    private ImageView image;
    private FirebaseStorage  storage = FirebaseStorage.getInstance();
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            currUID = intent.getExtras().getString("currUID");
        }
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        content = findViewById(R.id.new_post_content);
        Button cancelBtn = findViewById(R.id.new_post_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button submitBtn = findViewById(R.id.new_post_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });

        locationSwitch = findViewById(R.id.new_post_location_switch);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getLocationPermission();
                    setLocationString();
                } else {
                    removeLocationInfo();
                }
            }
        });

        Button addImages = findViewById(R.id.new_post_select_image);
        image = findViewById(R.id.new_post_image);
        addImages.setOnClickListener(chooseImageListener);
        pb = findViewById(R.id.progressBar);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGPSEnable) {
            locationSwitch.setChecked(false);
        }
    }

    private void getLocationPermission() {
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGPSEnable) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddPostActivity.this);
            builder.setMessage("GPS not enabled.")
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            locationSwitch.setChecked(false);
                            dialog.cancel();
                        }
                    });
            builder.create();
            builder.show();
        }
        canGetLocation = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!canGetLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_CODE);
        }
    }

    private void setLocationString() {
        canGetLocation = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable && canGetLocation && locationSwitch.isChecked()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000, 0,this);
                if(locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null) {
                        locationString = Formatter.formateLocationString(location.getLatitude(), location.getLongitude());
                    }
                }
        } else {
            locationSwitch.setChecked(false);
        }
    }

    private void removeLocationInfo() {
        locationString = null;
        Toast.makeText(AddPostActivity.this, "null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_CODE) {
            setLocationString();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(locationString == null) {
            setLocationString();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        if(locationSwitch.isChecked()) {
            setLocationString();
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if(locationSwitch.isChecked()) {
            getLocationPermission();
        }
    }

    //Add Image
    private View.OnClickListener chooseImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CODE && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                uri);
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addPost() {
        Date now = new Date();
        PostDAO newPost;
        if(locationString == null) {
            newPost = new PostDAO(content.getText().toString(),"null", now.getTime(), currUID);
        } else {
            newPost = new PostDAO(content.getText().toString(),locationString, now.getTime(), currUID);
        }

        //add to posts
        DatabaseReference newPostRef = ref.child("posts").push();
        newPostRef.setValue(newPost);
        String postId = newPostRef.getKey();
        //uploadImage
        uploadImage(newPostRef, now.toString());

        if(postId != null) {
            //add to self posts
            ref.child("posts_self").child(currUID).child(postId).setValue(postId);
            //add to follower's list
            getFollowersOfCurrentUser(postId);
            if(locationString != null) {
                ref.child("posts_location").child(newPost.getLocation()).child(postId).setValue(postId);
            }
        }
        finish();
    }

    private void uploadImage(DatabaseReference newPost, String time) {
        if (uri != null) {

            StorageReference ref = storage.getReference().
                    child("postImages/" +currUID + "/" + time);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 25, baos);
                byte[] data = baos.toByteArray();
                pb.setVisibility(View.VISIBLE);
                ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newPost.child("imageUrl").setValue(uri.toString());
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        System.out.println(progress);

                        pb.setProgress((int) progress);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void getFollowersOfCurrentUser(String postId) {
        ref.child("follower_relation").child(currUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                List<String> followers = new ArrayList<>();
                while(iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    followers.add(next.getKey());
                }
                setCurrentUserFollowerList(followers,postId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCurrentUserFollowerList(List<String> followers,String postId) {
        for(String followersUID : followers) {
            ref.child("posts_followings").child(followersUID).child(postId).setValue(postId);
        }
    }

    private String generateImageFileName() {
        Date currTime = new Date();
        return currUID + "_" + currTime.getTime();
    }
}
