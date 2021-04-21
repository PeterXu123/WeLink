package edu.neu.madcourse.welink.fragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.Stack;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.chat.ChatListFragment;
import edu.neu.madcourse.welink.follower.FollowerFragment;
import edu.neu.madcourse.welink.following.FollowingFragment;
import edu.neu.madcourse.welink.posts.AddPostActivity;
import edu.neu.madcourse.welink.posts.FollowingPostFragment;
import edu.neu.madcourse.welink.posts.NearbyPostFragment;
import edu.neu.madcourse.welink.profile.ProfileActivity;
import edu.neu.madcourse.welink.utility.User;

public class FragmentActivity extends AppCompatActivity implements NearbyPostFragment.DeleteFragmentCallBack{

    private FirebaseAuth mAuth;
    DatabaseReference ref;
    private String currUID;
    private String displayName;
    private FloatingActionButton addPost;
    private androidx.appcompat.widget.Toolbar iconToolBar;
    private ImageView icon;
    private Stack<Integer> backStackForID = new Stack<>();
    private BottomNavigationView bottomNavigationView;
    static final String STATE_MENU_ID = "menuId";
    private int currSelectedMenuID;
    Fragment fragment = null;
    private String iconUrl;
    private TextView logout;
    private AlertDialog exitAlert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FollowingFragment()).commit();
            backStackForID.push(R.id.nav_following);
        }
        else {
            backStackForID.push(R.id.nav_following);
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(f instanceof NearbyPostFragment){
                backStackForID.push(R.id.nav_nearby);
            } else if(f instanceof  FollowingFragment) {
                backStackForID.push(R.id.nav_following);
            } else if(f instanceof  FollowerFragment) {
                backStackForID.push(R.id.nav_follower);
            } else if(f instanceof FollowingPostFragment) {
                backStackForID.push(R.id.nav_posts);
            } else if(f instanceof ChatListFragment) {
                backStackForID.push(R.id.nav_chat);
            }
        }
        getCurrentUserUID();
        addPost = findViewById(R.id.add_post_button);
        iconToolBar = findViewById(R.id.iconToolbar);
        icon = findViewById(R.id.profileIcon);
        loadImageToIcon();
        TextView tv = findViewById(R.id.displayNameInToolbar);
        tv.setText(displayName);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luanchAddPostActivity();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mAuth.getCurrentUser().getDisplayName();
                Intent intent = new Intent(FragmentActivity.this, ProfileActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("iconUrl", iconUrl);
                intent.putExtra("uid", currUID);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildExitAlert(null).show();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //System.out.println("onStart-------------------------");
//    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int currSelectedMenuID = item.getItemId();

            FragmentTransaction ft = null;
            switch (currSelectedMenuID) {
                case R.id.nav_following:
                    fragment = new FollowingFragment();
                    break;
                case R.id.nav_follower:
                    fragment = new FollowerFragment();
                    break;
                case R.id.nav_nearby:
                    fragment = new NearbyPostFragment();
                    break;
                case R.id.nav_posts:
                    fragment = new FollowingPostFragment();
                    break;
                case R.id.nav_chat:
                    // todo: because we don't have the profile page and the chat button,
                    //  we need to add one sample user here.
                    //  After we have the profile page and can chat with people, those lines
                    //  except the "fragment = new ChatListFragment();" need to be deleted  -- zzx
//                    List<String> list = new LinkedList<>();
//                    if(list.isEmpty()) {
//                        list.add("5gWtXF86ZofKPoX89yL3QMcZGFD2_"+System.currentTimeMillis());
//                    }
//
//                    ref.child("chater_relation").child("1NjvX2d1k3eQqQpPGUVyt0JZLBC2")
//                            .setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                        }
//                    });
                    fragment = new ChatListFragment();

            }
            Bundle bundle = new Bundle();
            bundle.putString("currUID", currUID);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();

            backStackForID.push(currSelectedMenuID);
            return true;
        }
    };

    private AlertDialog buildExitAlert(Integer currId) {
        if(exitAlert == null) {
            exitAlert = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(currId != null) {
                                backStackForID.push(currId);
                            }
                        }
                    })
                    .show();
        }
        return exitAlert;
    }

    @Override
    public void onBackPressed() {
        int currId = backStackForID.pop();
        if(backStackForID.isEmpty()) {
            buildExitAlert(currId).show();
        } else {
            while(!backStackForID.isEmpty() && backStackForID.peek().equals(currId)) {
                backStackForID.pop();
            }
            if(backStackForID.isEmpty()) {
                buildExitAlert(currId).show();
            } else {
                int currSelectedMenuID = backStackForID.pop();
                bottomNavigationView.setSelectedItemId(currSelectedMenuID);
            }
        }
    }

    private void getCurrentUserUID() {
        mAuth =  FirebaseAuth.getInstance();
        FirebaseUser u = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        currUID = u.getUid();
        displayName = u.getDisplayName();
    }

    private void loadImageToIcon() {
        ref.child("users").child(currUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                iconUrl = snapshot.getValue(User.class).getIconUrl();
                if (snapshot.getValue(User.class).getIconUrl() != null) {
                    Picasso.get().load(snapshot.getValue(User.class).getIconUrl()).into(icon);

//                    Picasso.with(getApplicationContext()).load(snapshot.getValue(User.class).getIconUrl()).into(icon);
                }
                else {
                    icon.setImageResource(R.drawable.profile_icon);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (fragment != null) {
            System.out.println("this hsoudld output");
            fragment = new ChatListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("currUID", currUID);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();

            backStackForID.push(currSelectedMenuID);
        }
    }

    @Override
    public void backToPreviousFragment() {
        onBackPressed();
    }
}