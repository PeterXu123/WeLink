package edu.neu.madcourse.welink.posts;

import android.app.Activity;
import android.content.Intent;

import edu.neu.madcourse.welink.profile.ProfileActivity;
import edu.neu.madcourse.welink.utility.User;

public class OpenProfileOnClickListener {
    private final Activity activity;
    public OpenProfileOnClickListener(Activity activity) {
        this.activity = activity;
    }
    void onItemClick( User user) {
        if(user != null) {
            Intent intent = new Intent(activity, ProfileActivity.class);
            intent.putExtra("username", user.getDisplayName());
            intent.putExtra("iconUrl", user.getIconUrl());
            intent.putExtra("uid", user.getUid());
            activity.startActivity(intent);
        }
    }
}
