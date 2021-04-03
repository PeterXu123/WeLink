package edu.neu.madcourse.welink.fragmentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.follower.FollowerFragment;
import edu.neu.madcourse.welink.following.FollowingFragment;
import edu.neu.madcourse.welink.posts.AddPostActivity;
import edu.neu.madcourse.welink.posts.PostFragment;
import edu.neu.madcourse.welink.utility.User;
import edu.neu.madcourse.welink.utility.UserDTO;

public class FragmentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference ref;
    private String currUID;
    private FloatingActionButton addPost;
    private androidx.appcompat.widget.Toolbar iconToolBar;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FollowingFragment()).commit();
        getCurrentUserUID();
        addPost = findViewById(R.id.add_post_button);
        iconToolBar = findViewById(R.id.iconToolbar);
        icon = findViewById(R.id.profileIcon);
        loadImageToIcon();
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luanchAddPostActivity();
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_following:
                    fragment = new FollowingFragment();
                    break;
                case R.id.nav_follower:
                    fragment = new FollowerFragment();
                    break;
                case R.id.nav_nearby:
                    fragment = new PostFragment("nearby");
                    break;
                case R.id.nav_posts:
                    fragment = new PostFragment("friends");
                    break;
                case R.id.nav_self:
                    fragment = new PostFragment("self");
            }
            Bundle bundle = new Bundle();
            bundle.putString("currUID", currUID);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            return true;
        }
    };

    private void getCurrentUserUID() {
        mAuth =  FirebaseAuth.getInstance();
        FirebaseUser u = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        currUID = u.getUid();
    }

    private void loadImageToIcon() {
        ref.child("users").child(currUID).child("iconUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue(String.class) != null) {
                    Picasso.with(getApplicationContext()).load(snapshot.getValue(String.class)).into(icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ref.child("users").child(currUID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void luanchAddPostActivity() {
        Intent intent = new Intent(this, AddPostActivity.class);
        intent.putExtra("currUID", currUID);
        startActivity(intent);
    }
}