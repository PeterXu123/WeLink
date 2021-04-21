package edu.neu.madcourse.welink.posts;

import android.app.Activity;
import android.content.Intent;

public class OpenImageOnClickListener {
    private final Activity activity;
    public OpenImageOnClickListener(Activity activity) {
        this.activity = activity;
    }
    void onItemClick(String uri) {
        if(activity != null && uri != null) {
            Intent intent = new Intent(activity, OpenImageActivity.class);
            intent.putExtra("uri", uri);
            activity.startActivity(intent);
        }
    }
}
