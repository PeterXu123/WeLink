package edu.neu.madcourse.welink.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.PostDAO;
import edu.neu.madcourse.welink.utility.PostDTO;
import edu.neu.madcourse.welink.utility.User;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private ArrayList<PostDTO> postDTOs;
    RecyclerView rv;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    //TODO: dummy current user uid and location
    String currUser = "MxOJGG6VRuZPVaQKVJ9Dzth2omd2";
    String currLocation = "37_25_38_45";

    PostAdapter(String type) {
        switch (type) {
            case "nearby":
                ref.child("posts_location").child(currLocation).addChildEventListener(listener);
                break;
            case "self":
                ref.child("posts_self").child(currUser).addChildEventListener(listener);
                break;
            case "friends":
                ref.child("posts_followings").child(currUser).addChildEventListener(listener);
                break;
            default:
        }
        postDTOs = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        rv = recyclerView;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent,false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostDTO post = postDTOs.get(position);
        holder.username.setText(post.getAuthor().getDisplayName());
        holder.time.setText(post.getTime());
        holder.content.setText(post.getText());


    }

    @Override
    public int getItemCount() {
        return postDTOs.size();
    }

    private ValueEventListener findPostById() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostDAO post = snapshot.getValue(PostDAO.class);
                PostDTO postDTO = new PostDTO();
                postDTO.setByPostDAO(post);
                ref.child("users").child(post.getAuthorUID()).addListenerForSingleValueEvent(findUserById(postDTO));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private ValueEventListener findUserById(PostDTO postDTO) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User author = snapshot.getValue(User.class);
                postDTO.setAuthor(author);
                addNewPostToList(postDTO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void addNewPostToList(PostDTO post) {
        postDTOs.add(0,post);
        notifyItemInserted(0);
        rv.smoothScrollToPosition(0);
    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String postId = snapshot.getKey();
            //get Post and author information
            if(postId != null) {
                ref.child("posts").child(postId).addListenerForSingleValueEvent(findPostById());
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}
