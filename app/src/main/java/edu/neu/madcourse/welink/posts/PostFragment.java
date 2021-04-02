package edu.neu.madcourse.welink.posts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;
import edu.neu.madcourse.welink.utility.UserDTO;

public class PostFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManger;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private String type;

    private String currUID;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public PostFragment(String type) {
        this.type = type;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            currUID = bundle.getString("currUID");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.posts_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createRecyclerView( view);
    }


    private void createRecyclerView(View view) {
        layoutManger = new LinearLayoutManager(getActivity().getApplication());
        recyclerView = view.findViewById(R.id.post_rv);
        recyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter(currUID, type);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManger);
    }

//    //TODO:dummy add post
//    private View.OnClickListener tempListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            LayoutInflater inflater = LayoutInflater.from(PostActivity.class);
//            View input = inflater.inflate(R.layout.new_post_prompt,null);
//            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
//            final EditText content = input.findViewById(R.id.new_post_content);
//            builder.setView(input)
//                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            addPost(content.getText().toString());
//                        }
//                    })
//                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    })
//                    .setCancelable(true);
//            builder.create();
//            builder.show();
//        }
//    };
//
//    private void addPost(String content) {
//        Date now = new Date();
//        PostDAO dummyPost = new PostDAO(content,"37_25_38_45", now.getTime(), "MxOJGG6VRuZPVaQKVJ9Dzth2omd2");
//        count++;
//        //add to posts
//        DatabaseReference res = ref.child("posts").push();
//        res.setValue(dummyPost);
//        String postId = res.getKey();
//        //add to self post
//        if(postId != null) {
//            ref.child("posts_self").child(currUserUID).child(postId).setValue(postId);
//            //add to follower's list
//            for(String followersUID : currUser.getFollowers()) {
//                ref.child("posts_followings").child(followersUID).child(postId).setValue(postId);
//            }
//            //add to location list
//            ref.child("posts_location").child(dummyPost.getLocation()).child(postId).setValue(postId);
//        }
//    }


//    ref.child("follower_relation").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
//            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
//            List<String> followers = new ArrayList<>();
//            while(iterator.hasNext()) {
//                DataSnapshot next = iterator.next();
//                followers.add(next.getKey());
//            }
//            currUser.setFollowers(followers);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });

}
