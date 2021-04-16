package edu.neu.madcourse.welink.posts;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post_prompt);
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
                        Toast.makeText(AddPostActivity.this, locationString, Toast.LENGTH_SHORT).show();
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

    private void addPost() {
        Date now = new Date();
        PostDAO newPost;
        if(locationString == null) {
            newPost = new PostDAO(content.getText().toString(),"null", now.getTime(), currUID);
        } else {
            newPost = new PostDAO(content.getText().toString(),locationString, now.getTime(), currUID);
        }

        //add to posts
        DatabaseReference res = ref.child("posts").push();
        res.setValue(newPost);
        String postId = res.getKey();
        //add to self post
        if(postId != null) {
            ref.child("posts_self").child(currUID).child(postId).setValue(postId);
            //add to follower's list
            getFollowersOfCurrentUser(postId);
            if(locationString != null) {
                ref.child("posts_location").child(newPost.getLocation()).child(postId).setValue(postId);
            }
        }
        finish();
    }

    private void setCurrentUserFollowerList(List<String> followers,String postId) {
        for(String followersUID : followers) {
            ref.child("posts_followings").child(followersUID).child(postId).setValue(postId);
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
}
