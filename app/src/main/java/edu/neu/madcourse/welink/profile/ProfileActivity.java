package edu.neu.madcourse.welink.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.follower.FollowerActivity;
import edu.neu.madcourse.welink.following.FollowingActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView username;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.username_textView);
        if (getIntent().getExtras() != null) {
            username.setText(getIntent().getExtras().getString("username"));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_img_button:
//                Intent intent1 = new Intent(ProfileActivity.this, UploadProfileIconActivity.class);
//                startActivity(intent1);
//                break;
            case R.id.profile_posts_entry:
                Intent intent2 = new Intent(ProfileActivity.this, DisplayPostsActivity.class);
                startActivity(intent2);
                break;
            case R.id.profile_followers_entry:
                Intent intent3 = new Intent(ProfileActivity.this, FollowerActivity.class);
                intent3.putExtra("uid", uid);
                startActivity(intent3);
                break;
            case R.id.profile_following_entry:
                Intent intent4 = new Intent(ProfileActivity.this, FollowingActivity.class);
                startActivity(intent4);
        }
    }
}