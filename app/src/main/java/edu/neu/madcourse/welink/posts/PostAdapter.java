package edu.neu.madcourse.welink.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.PostDAO;
import edu.neu.madcourse.welink.utility.PostDTO;
import edu.neu.madcourse.welink.utility.User;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private ArrayList<PostDTO> postDTOs;
    private RecyclerView rv;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private OpenProfileOnClickListener openProfileOnClickListener;
    private OpenImageOnClickListener openImageOnClickListener;
    private Map<String, PostDTO> postIdDTOMap;
    private int totalPostCount = 0;
    private int loadedPostCount = 0;
    private User currUser;

    PostAdapter(String uid, String type,String location) {
        postDTOs = new ArrayList<>();
        postIdDTOMap = new HashMap<>();
        switch (type) {
            case "nearby":
                ref.child("posts_location").child(location).orderByKey().addListenerForSingleValueEvent(getChildrenOnceListener);
                break;
            case "self":
                ref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currUser = snapshot.getValue(User.class);
                        ref.child("posts_self").child(uid).orderByKey().addChildEventListener(childAddListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "user":
                ref.child("posts_self").child(uid).orderByKey().addListenerForSingleValueEvent(getChildrenOnceListener);
                break;
            case "followings":
                ref.child("posts_followings").child(uid).orderByKey().addListenerForSingleValueEvent(getChildrenOnceListener);
                break;
            default:
        }
    }

    public void setOpenProfileListener(OpenProfileOnClickListener openProfileOnClickListener, OpenImageOnClickListener openImageOnClickListener) {
        this.openProfileOnClickListener = openProfileOnClickListener;
        this.openImageOnClickListener = openImageOnClickListener;
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
        return new PostViewHolder(v, openProfileOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostDTO post = postDTOs.get(position);
        holder.user = post.getAuthor();
        holder.username.setText(post.getAuthor().getDisplayName());
        holder.time.setText(post.getTime());
        holder.content.setText(post.getText());
        String iconUrl = post.getAuthor().getIconUrl();
        if (iconUrl != null) {
            Picasso.get().load(iconUrl).placeholder(R.drawable.loading_place_holder).into(holder.userImage);
        }
        else {
            holder.userImage.setImageResource(R.drawable.profile_icon);
        }
        List<String> imageUrls = post.getImageUrls();
        if(imageUrls != null) {
            for (ImageView iv: holder.postImages) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(0);
            }
            for(int i = 0; i < imageUrls.size(); i++) {
                Picasso.get().load(imageUrls.get(i)).placeholder(R.drawable.loading_place_holder).into(holder.postImages.get(i));
                holder.postImages.get(i).setOnClickListener(getOpenImageOnClickListener(imageUrls.get(i)));
            }
        }
        else {
            for (ImageView iv: holder.postImages) {
                iv.setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener getOpenImageOnClickListener(String uri) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openImageOnClickListener != null) {
                    openImageOnClickListener.onItemClick(uri);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return postDTOs.size();
    }

    private ValueEventListener findPostById(String listenerType) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostDAO postDAO = snapshot.getValue(PostDAO.class);
                String postId = snapshot.getKey();
                if(postDAO != null) {
                    if(listenerType.equals("getChildrenOnceListener")) {
                        PostDTO postDTO= postIdDTOMap.get(postId);
                        if(postDTO != null) {
                            postDTO.setByPostDAO(postDAO);
                            ref.child("users").child(postDAO.getAuthorUID()).addListenerForSingleValueEvent(findUserById(postDTO, listenerType));
                        }
                    } else if(listenerType.equals("childAddListener")) {
                        PostDTO postDTO = new PostDTO();
                        postDTO.setByPostDAO(postDAO);
                        postDTO.setAuthor(currUser);
                        addNewPostToList(postDTO, "childAddListener");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private ValueEventListener findUserById(PostDTO postDTO, String listenerType) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User author = snapshot.getValue(User.class);
                postDTO.setAuthor(author);
                loadedPostCount++;
                addNewPostToList(postDTO, listenerType);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void addNewPostToList(PostDTO post, String listenerType) {
        if(listenerType.equals("getChildrenOnceListener")) {
            if(loadedPostCount == totalPostCount) {
                notifyDataSetChanged();
            }
        } else if(listenerType.equals("childAddListener")) {
            postDTOs.add(0,post);
            notifyItemInserted(0);
            rv.smoothScrollToPosition(0);
        }
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
            totalPostCount = posts.size();
            for(String postId : posts) {
                PostDTO newPost= new PostDTO(postId);
                postIdDTOMap.put(postId, newPost);
                postDTOs.add(newPost);
                ref.child("posts").child(postId).addListenerForSingleValueEvent(findPostById("getChildrenOnceListener"));
            }
            Collections.reverse(postDTOs);
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
                ref.child("posts").child(postId).addListenerForSingleValueEvent(findPostById("childAddListener"));
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
