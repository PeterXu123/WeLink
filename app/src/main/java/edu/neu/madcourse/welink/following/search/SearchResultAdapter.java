package edu.neu.madcourse.welink.following.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultHolder> {
    private List<User> listOfUsers;
    private DatabaseReference ref;
    private Context context;
    private String searchName;
    private List<String> listOfUid;

    public SearchResultAdapter(DatabaseReference ref, Context context, String searchName) {
        this.ref = ref;
        this.searchName = searchName;
        this.context = context;
        this.listOfUsers = new ArrayList<>();
        this.listOfUid = new ArrayList<>();
//        ref.child("username_to_uid").child(searchName).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                listOfUid.add(snapshot.getKey());
//                System.out.println(snapshot.getKey());
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
            ref.child("username_to_uid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(searchName)) {
                        for (DataSnapshot child : snapshot.child(searchName).getChildren()) {

                            listOfUid.add(child.getKey());
                        }
                    }
                    if (listOfUid.size() != 0) {

                        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot child : snapshot.getChildren()) {

                                    if (listOfUid.contains(child.getKey())) {
                                        User user = child.getValue(User.class);
                                        listOfUsers.add(child.getValue(User.class));

                                    }
                                }
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }


    @NonNull
    @Override
    public SearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new SearchResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultHolder holder, int position) {
        User u =  listOfUsers.get(position);
        String shortUserName = u.getDisplayName().length() >= 8 ? u.getDisplayName().substring(0,8) : u.getDisplayName();
        holder.displayName.setText(shortUserName);
        if (u.getIconUrl() != null) {
            Picasso.with(context).load(u.getIconUrl()).into(holder.profileIcon);
        }
        else {
            holder.profileIcon.setImageResource(R.drawable.profile_icon);
        }

    }

    @Override
    public int getItemCount() {
        System.out.println(listOfUsers.size());
        System.out.println(listOfUid.size());
        return listOfUsers.size();
    }
}
