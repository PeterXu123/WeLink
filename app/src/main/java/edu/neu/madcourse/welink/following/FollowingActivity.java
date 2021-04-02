package edu.neu.madcourse.welink.following;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.follower.FollowerActivity;
import edu.neu.madcourse.welink.following.search.SearchResultActivity;
import edu.neu.madcourse.welink.following.search.SearchResultAdapter;
import edu.neu.madcourse.welink.utility.BothFollowAdapter;

public class FollowingActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String uid;
    private String displayName;
    private String email;
    private String token;
    private Button searchButton;
    private EditText searchName;
    private RecyclerView followingListView;
    private BothFollowAdapter mFollowingAdapter;
    private Handler handler;
    private Button deleted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        deleted = findViewById(R.id.deleted);
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FollowingActivity.this, FollowerActivity.class);
                startActivity(intent);
            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        handler = new Handler(Looper.myLooper());
        mAuth =  FirebaseAuth.getInstance();
        if (getIntent().getExtras() != null) {
            uid = getIntent().getExtras().getString("uid");
            displayName = getIntent().getExtras().getString("displayName");
            email = getIntent().getExtras().getString("email");
            token = getIntent().getExtras().getString("token");
        }
        if (uid == null || displayName == null || email == null) {
            FirebaseUser u = mAuth.getCurrentUser();
            uid = u.getUid();
            displayName = u.getDisplayName();
            email = u.getEmail();
        }

        searchName = findViewById(R.id.searchName);
        searchButton = findViewById(R.id.searchUserButton);
        followingListView = findViewById(R.id.followingList);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchName.getText().toString().equals("")) {
                    Intent intent = new Intent(FollowingActivity.this, SearchResultActivity.class);
                    intent.putExtra("search_name", searchName.getText().toString());
                    startActivity(intent);
                }
            }
        });



    }

    class showFollowingList extends Thread {
        showFollowingList() {
        }

        @Override
        public void run() {
            FirebaseUser u = mAuth.getCurrentUser();
            mFollowingAdapter = new BothFollowAdapter(mDatabaseReference, FollowingActivity.this, true, u.getUid());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    followingListView.setAdapter(mFollowingAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
                    followingListView.setLayoutManager(layoutManager);
                }
            });


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        new showFollowingList().start();

    }


}