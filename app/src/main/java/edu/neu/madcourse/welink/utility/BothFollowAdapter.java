package edu.neu.madcourse.welink.utility;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.following.search.SearchResultHolder;
import edu.neu.madcourse.welink.profile.ProfileActivity;

public class BothFollowAdapter  extends RecyclerView.Adapter<BothFollowHolder> {
    private List<User> listOfUsers;
    private DatabaseReference ref;
    private Context context;
    private List<String> listOfUid;
    private boolean isFollowing;
    private String curUid;
    public BothFollowAdapter(DatabaseReference ref, Context context,  boolean isFollowing, String curUid ) {
        this.ref = ref;
        this.listOfUid = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        this.context = context;
        this.isFollowing = isFollowing;
        this.curUid = curUid;
        if (isFollowing) {
            ref.child("following_relation").child(curUid).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String followingUid = snapshot.getKey();
                        notifyDataSetChanged();
//                        ref.child("users").child(followingUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                listOfUsers.add(snapshot.getValue(User.class));
//                                notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                    ref.child("users").child(followingUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listOfUsers.add(snapshot.getValue(User.class));
                            notifyDataSetChanged();
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
                    String followingUid = snapshot.getKey();
                    ref.child("users").child(followingUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println("-----------------------");
                            System.out.println(snapshot.getValue(User.class).getUid());
                            for (int i = 0; i < listOfUsers.size(); i++) {
                                if (listOfUsers.get(i).getUid().equals(snapshot.getValue(User.class).getUid())) {
                                    listOfUsers.remove(i);
                                    break;
                                }
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        else {
            ref.child("follower_relation").child(curUid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String followingUid = snapshot.getKey();
                    ref.child("users").child(followingUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listOfUsers.add(snapshot.getValue(User.class));
                            notifyDataSetChanged();
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
                    String followerUid = snapshot.getKey();
                    ref.child("users").child(followerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println(snapshot.getValue(User.class).getUid());
                            for (int i = 0; i < listOfUsers.size(); i++) {
                                if (listOfUsers.get(i).getUid().equals(snapshot.getValue(User.class).getUid())) {
                                    listOfUsers.remove(i);
                                    break;
                                }
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }



    }
    @NonNull
    @Override
    public BothFollowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new BothFollowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BothFollowHolder holder, int position) {
        User u =  listOfUsers.get(position);
        String shortUserName = u.getDisplayName().length() >= 8 ? u.getDisplayName().substring(0,8) : u.getDisplayName();
        holder.displayName.setText(shortUserName);
        if (u.getIconUrl() != null) {
//            Picasso.with(context).load(u.getIconUrl()).into(holder.profileIcon);
            Picasso.get().load(u.getIconUrl()).into(holder.profileIcon);
        }
        else {
            holder.profileIcon.setImageResource(R.drawable.profile_icon);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("username", listOfUsers.get(position).getDisplayName());
                intent.putExtra("iconUrl", listOfUsers.get(position).getIconUrl());
                intent.putExtra("uid", listOfUsers.get(position).getUid());
                intent.putExtra("token", listOfUsers.get(position).getToken());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }
}
