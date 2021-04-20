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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.PostDAO;
import edu.neu.madcourse.welink.utility.PostDTO;
import edu.neu.madcourse.welink.utility.User;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private ArrayList<PostDTO> postDTOs;
    private RecyclerView rv;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    PostAdapter(String uid, String type,String location) {
        postDTOs = new ArrayList<>();
        switch (type) {
            case "nearby":
                ref.child("posts_location").child(location).addListenerForSingleValueEvent(getChildrenOnceListener);
                break;
            case "self":
                ref.child("posts_self").child(uid).addChildEventListener(childAddListener);
                break;
            case "user":
                ref.child("posts_self").child(uid).addListenerForSingleValueEvent(getChildrenOnceListener);
            case "followings":
                ref.child("posts_followings").child(uid).addListenerForSingleValueEvent(getChildrenOnceListener);
                break;
            default:
        }
    }

//    public void clear() {
//        int size = postDTOs.size();
//        postDTOs.clear();
//        notifyItemRangeRemoved(0,size);
//    }

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
        String iconUrl = post.getAuthor().getIconUrl();
        if (iconUrl != null) {
            Picasso.get().load(iconUrl).into(holder.userImage);
//                    Picasso.with(getApplicationContext()).load(snapshot.getValue(User.class).getIconUrl()).into(icon);
        }
        String imageUrl = post.getImageUrl();
        if(imageUrl != null) {
            Picasso.get().load(imageUrl).into(holder.postImage);
        }
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
                if(post != null) {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setByPostDAO(post);
                    ref.child("users").child(post.getAuthorUID()).addListenerForSingleValueEvent(findUserById(postDTO));
                }
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

    private ValueEventListener getChildrenOnceListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
            List<String> posts = new ArrayList<>();
            while(iterator.hasNext()) {
                DataSnapshot next = iterator.next();
                posts.add(next.getKey());
            }
            for(String postId : posts) {
                ref.child("posts").child(postId).addListenerForSingleValueEvent(findPostById());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ChildEventListener childAddListener = new ChildEventListener() {
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
