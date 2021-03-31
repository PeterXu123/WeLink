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
import edu.neu.madcourse.welink.utility.TimeFormatter;
import edu.neu.madcourse.welink.utility.User;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private ArrayList<DataSnapshot> postSnapshots;
    private ArrayList<PostDTO> postDTOs;
    RecyclerView rv;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String currUser = "MxOJGG6VRuZPVaQKVJ9Dzth2omd2";

    PostAdapter() {
        ref.child("post_self").child(currUser).addChildEventListener(listener);
        postSnapshots = new ArrayList<>();
        postDTOs = new ArrayList<>();
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


    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            postSnapshots.add(0,snapshot);
            PostDAO postDAO = snapshot.getValue(PostDAO.class);
            String postId = snapshot.getKey();
            String authorUID = postDAO.getAuthorUID();
            ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User author = snapshot.child(authorUID).getValue(User.class);
                    PostDTO postDTO = new PostDTO(postId,postDAO.getText(),postDAO.getLocation(), TimeFormatter.STORAGE_TIME_FORMATTER.format(postDAO.getTime()),author);
                    postDTOs.add(0,postDTO);
                    notifyItemInserted(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
