package edu.neu.madcourse.welink.follower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.following.FollowingActivity;
import edu.neu.madcourse.welink.utility.BothFollowAdapter;

public class FollowerActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private Handler handler;
    private String uid;
    private RecyclerView followerListView;
    private BothFollowAdapter mFollowerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        handler = new Handler(Looper.myLooper());
        followerListView = findViewById(R.id.followerList);
        if (getIntent().getExtras() != null) {
            uid = getIntent().getExtras().getString("uid");

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        new showFollowerList().start();

    }


    class showFollowerList extends Thread {
        @Override
        public void run() {

            mFollowerAdapter = new BothFollowAdapter(mDatabaseReference,
                    FollowerActivity.this, false, uid);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    followerListView.setAdapter(mFollowerAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
                    followerListView.setLayoutManager(layoutManager);
                }
            });
        }

    }
}