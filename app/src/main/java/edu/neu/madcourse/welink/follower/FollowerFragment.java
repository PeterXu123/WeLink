package edu.neu.madcourse.welink.follower;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.following.FollowingFragment;
import edu.neu.madcourse.welink.following.search.SearchResultActivity;
import edu.neu.madcourse.welink.utility.BothFollowAdapter;


public class FollowerFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String uid;
    private String displayName;
    private String email;

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
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        handler = new Handler(Looper.myLooper());
        mAuth =  FirebaseAuth.getInstance();
        if (uid == null || displayName == null || email == null) {
            FirebaseUser u = mAuth.getCurrentUser();
            uid = u.getUid();
            displayName = u.getDisplayName();
            email = u.getEmail();
        }
//        followingListView = view.findViewById(R.id.followerList1);
        FirebaseUser u = mAuth.getCurrentUser();
        mFollowingAdapter = new BothFollowAdapter(mDatabaseReference, getContext(), false, u.getUid());
        followingListView = view.findViewById(R.id.followerList1);
        followingListView.setAdapter(mFollowingAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplication());
        followingListView.setLayoutManager(layoutManager);

    }

//    class showFollowerList extends Thread {
//        View view;
//        showFollowerList(View view) {
//            this.view = view;
//        }
//
//        @Override
//        public void run() {
//            FirebaseUser u = mAuth.getCurrentUser();
//            mFollowingAdapter = new BothFollowAdapter(mDatabaseReference, getContext(), false, u.getUid());
//            followingListView = this.view.findViewById(R.id.followerList1);
//            followingListView.setAdapter(mFollowingAdapter);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplication());
//            followingListView.setLayoutManager(layoutManager);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//
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
    }

}