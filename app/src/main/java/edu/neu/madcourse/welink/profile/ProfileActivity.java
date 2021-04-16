package edu.neu.madcourse.welink.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.chat.MainChatActivity;
import edu.neu.madcourse.welink.follower.FollowerActivity;
import edu.neu.madcourse.welink.following.FollowingActivity;
import edu.neu.madcourse.welink.login_signup.MainActivity;
import edu.neu.madcourse.welink.utility.UploadProfileIconActivity;
import edu.neu.madcourse.welink.utility.User;

public class ProfileActivity extends AppCompatActivity  {
    TextView username;
    String uid;
    ImageView iv;
    Button uploadImage;
    FirebaseAuth auth;
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.displayNameInProfile);
        iv = findViewById(R.id.profileIconInProfile);
        uploadImage = findViewById(R.id.uploadImageInProfile);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        if (getIntent().getExtras().getString("uid") != null && getIntent().getExtras().getString("uid").equals(auth.getCurrentUser().getUid())) {
            mDatabaseReference.child("users").child( getIntent().getExtras().getString("uid")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(User.class).getIconUrl() != null) {
                        Picasso.get().load(snapshot.getValue(User.class).getIconUrl()).into(iv);

//                    Picasso.with(getApplicationContext()).load(snapshot.getValue(User.class).getIconUrl()).into(icon);
                    }
                    else {
                        iv.setImageResource(R.drawable.profile_icon);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, UploadProfileIconActivity.class);
                    startActivity(intent);
                }
            });

        }
        else {
            uploadImage.setVisibility(View.GONE);
        }
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            username.setText(bundle.getString("username"));
            uid = bundle.getString("uid");
            System.out.println(bundle.getString("iconUrl"));
            if (bundle.getString("iconUrl") != null) {
                Picasso.get().load(bundle.getString("iconUrl")).into(iv);
            }
            else {
                iv.setImageResource(R.drawable.profile_icon);
            }
        }
        else {
            System.out.println("Wtf");
        }

        // check if this one is current user


        // check if this user has been followed by current user

    }

    private void followHim() {

    }


    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.PostInProfile:
                intent = new Intent(ProfileActivity.this, DisplayPostsActivity.class);
                startActivity(intent);
                break;
            case R.id.FollowerInProfile:
                intent = new Intent(ProfileActivity.this, FollowerActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.FollowingInProfile:
                intent= new Intent(ProfileActivity.this, FollowingActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.ChatInProfile:
                intent = new Intent(ProfileActivity.this, MainChatActivity.class);
                startActivity(intent);
                break;

            case R.id.FollowHim:
                followHim();
                break;
        }
    }
}