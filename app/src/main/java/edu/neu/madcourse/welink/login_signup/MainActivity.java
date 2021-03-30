package edu.neu.madcourse.welink.login_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.posts.NearbyActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: this is the temp button for testing purpose. move to header tabs
        Button nearBy = findViewById(R.id.temp_nearby_posts);
        nearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNearbyActivity();
            }
        });
    }


    private void launchNearbyActivity() {
        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }
}