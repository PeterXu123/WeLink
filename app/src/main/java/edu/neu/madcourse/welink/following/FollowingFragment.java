package edu.neu.madcourse.welink.following;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.follower.FollowerActivity;
import edu.neu.madcourse.welink.following.search.SearchResultActivity;
import edu.neu.madcourse.welink.utility.BothFollowAdapter;
import edu.neu.madcourse.welink.utility.UploadProfileIconActivity;


public class FollowingFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String uid;
    private String displayName;
    private String email;
    private String token;
    private ImageButton searchButton;
    private EditText searchName;
    private RecyclerView followingListView;
    private BothFollowAdapter mFollowingAdapter;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String test = "Test";
        Bundle bundle = new Bundle();
        bundle.putString("test", test);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        handler = new Handler(Looper.myLooper());
        mAuth =  FirebaseAuth.getInstance();
        if (uid == null || displayName == null || email == null) {
            FirebaseUser u = mAuth.getCurrentUser();
            uid = u.getUid();
            displayName = u.getDisplayName();
            email = u.getEmail();
        }
        searchName = view.findViewById(R.id.searchName1);
        searchButton = view.findViewById(R.id.searchUserButton1);
        followingListView = view.findViewById(R.id.followingList1);
//        view.findViewById(R.id.uploadButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UploadProfileIconActivity.class);
//                startActivity(intent);
//            }
//        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Firebase Database paths must not contain '.', '#', '$', '[', or ']'
                if (!searchName.getText().toString().equals("") && !searchName.getText().toString().contains(".") && !searchName.getText().toString().contains("#") && !searchName.getText().toString().contains("$")
                && !searchName.getText().toString().contains("[") && !searchName.getText().toString().contains("]")) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("search_name", searchName.getText().toString());
                    searchName.setText("");
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Search name is either empty or contains invalid character", Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseUser u = mAuth.getCurrentUser();
        mFollowingAdapter = new BothFollowAdapter(mDatabaseReference, getContext(), true, u.getUid());
        followingListView.setAdapter(mFollowingAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplication());
        followingListView.setLayoutManager(layoutManager);

    }

//    class showFollowingList extends Thread {
//        showFollowingList() {
//        }
//
//        @Override
//        public void run() {
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//
//
//        }
//
//    }

    @Override
    public void onStart() {
        super.onStart();
//        new showFollowingList().start();

    }

}