package edu.neu.madcourse.welink.utility;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import edu.neu.madcourse.welink.R;

public class UploadProfileIconActivity extends AppCompatActivity {

    private Button selectButton;
    private Button uploadButton;
    private int PICK_CODE = 19;
    private FirebaseStorage storage;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private Uri uri;
    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_icon);
        selectButton = findViewById(R.id.chooseButton);
        uploadButton = findViewById(R.id.uploadImageButton);
        storage = FirebaseStorage.getInstance();
        iv = findViewById(R.id.imageViewForIcon);
        selectButton.setOnClickListener(chooseListener);
        uploadButton.setOnClickListener(uploadListener);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth =  FirebaseAuth.getInstance();


    }

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
                iv.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private View.OnClickListener chooseListener = new View.OnClickListener() {
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

    private View.OnClickListener uploadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (uri != null) {
                StorageReference ref = storage.getReference().
                        child("profileIcon/" + UUID.randomUUID().toString());
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] data = baos.toByteArray();
                    ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    Task<Uri> user_iconUrl = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            System.out.println("herrrrrrrrrrrrrrrrrrrrr " + uri.toString());
                                            user.setIconUrl(uri.toString());
                                            mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                                            finish();
                                        }
                                    });
//                                    if (user_iconUrl.isSuccessful()) {
//                                        System.out.println("herrrrrrrrrrrrrrrrrrrrr");
//                                        user.setIconUrl(user_iconUrl.getResult().toString());
//                                        mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
//                                        finish();
//                                    }
//                                    else {
//                                        System.out.println(user_iconUrl.getResult().toString());
//                                        System.out.println("eeeeeeeeeeeeeeeeeeeeeeee");
//                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                        mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                                User user = snapshot.getValue(User.class);
////                                Task<Uri> user_iconUrl = taskSnapshot.getStorage().getDownloadUrl();
////                                if (user_iconUrl.isSuccessful()) {
////                                    System.out.println("herrrrrrrrrrrrrrrrrrrrr");
////                                    user.setIconUrl(user_iconUrl.getResult().toString());
////                                    mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
////                                    finish();
////                                }
////                                else {
////                                    System.out.println("eeeeeeeeeeeeeeeeeeeeeeee");
////                                }
////
////                            }
////
////                            @Override
////                            public void onCancelled(@NonNull DatabaseError error) {
////
////                            }
////                        });
////                        Toast
////                                .makeText(UploadProfileIconActivity.this,
////                                        "Uploaded image successfully",
////                                        Toast.LENGTH_SHORT)
////                                .show();
//                    }
//                });

            }
        }
    };
}