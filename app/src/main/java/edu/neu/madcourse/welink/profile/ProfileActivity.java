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
import edu.neu.madcourse.welink.posts.SelfPostActivity;
import edu.neu.madcourse.welink.utility.UploadProfileIconActivity;
import edu.neu.madcourse.welink.utility.User;

public class ProfileActivity extends AppCompatActivity  {
    TextView username;
    String uid;
    ImageView iv;
    Button uploadImage;
    Button chat;
    FirebaseAuth auth;
    DatabaseReference mDatabaseReference;
    String token;
    Button followHim;
    String currentUserId;
    Button unfollow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.displayNameInProfile);
        iv = findViewById(R.id.profileIconInProfile);
        uploadImage = findViewById(R.id.uploadImageInProfile);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        chat = findViewById(R.id.ChatInProfile);
        followHim = findViewById(R.id.FollowHim);
        unfollow = findViewById(R.id.unfollowInProfile);
        unfollow.setVisibility(View.GONE);
        // if it's current user's profile, token will be null
        if (getIntent().getExtras().getString("token") != null) {
            token = getIntent().getExtras().getString("token");
        }
        if (getIntent().getExtras().getString("uid") != null && getIntent().getExtras().getString("uid").equals(auth.getCurrentUser().getUid())) {
            chat.setVisibility(View.GONE);
            followHim.setVisibility(View.GONE);
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
            if (getIntent().getExtras().getString("uid") != null){
            mDatabaseReference.child("following_relation").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(getIntent().getExtras().getString("uid"))) {
                        followHim.setVisibility(View.GONE);
                        unfollow.setVisibility(View.VISIBLE);
                    }
                    else {
                        followHim.setVisibility(View.VISIBLE);
                        unfollow.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }

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
        String targetUid = uid;
        String currentUid = currentUserId;
        mDatabaseReference.child("following_relation").child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(targetUid)) {
                    mDatabaseReference.child("following_relation").child(currentUid).child(targetUid).setValue(targetUid);
                    mDatabaseReference.child("follower_relation").child(targetUid).child(currentUid).setValue(currentUid);
                    mDatabaseReference.child("posts_self").child(targetUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                System.out.println(ds.getKey());
                                mDatabaseReference.child("posts_followings").child(currentUid).child(ds.getKey()).setValue(ds.getValue(String.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void unFollowHim() {
        String targetUid = uid;
        String currentUid = currentUserId;
        mDatabaseReference.child("following_relation").child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(targetUid)) {
                    mDatabaseReference.child("following_relation").child(currentUid).child(targetUid).removeValue();
                    mDatabaseReference.child("follower_relation").child(targetUid).child(currentUid).removeValue();
                    mDatabaseReference.child("posts_self").child(targetUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                mDatabaseReference.child("posts_followings").child(currentUid)
                                        .child(ds.getKey()).setValue(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.PostInProfile:
                intent = new Intent(ProfileActivity.this, SelfPostActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("currentUID", currentUserId);
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
                intent.putExtra("fromUser", auth.getCurrentUser().getDisplayName());
                intent.putExtra("curChaterToken", token);
                intent.putExtra("curUserName", auth.getCurrentUser().getDisplayName());
                String displayname = auth.getCurrentUser().getDisplayName();
                intent.putExtra("curUserID", currentUserId);
                intent.putExtra("curChaterID", uid);

                String fromUser = auth.getCurrentUser().getProviderId();
                String toUser = this.username.getText().toString();
                intent.putExtra("curChaterName", this.username.getText().toString());
                String pairKey = currentUserId.compareTo(uid) < 0 ?
                        currentUserId + "_" + uid : uid + "_" + currentUserId;
                intent.putExtra("pairKey", pairKey);
                startActivity(intent);
                break;
            case R.id.FollowHim:
                followHim();
                break;
            case R.id.unfollowInProfile:
                unFollowHim();
                break;
        }
    }
}