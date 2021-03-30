package edu.neu.madcourse.welink.following;

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

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingHolder> {
    private List<User> listOfUsers;
    private DatabaseReference ref;
    private Context context;
    private String searchName;
    private List<String> listOfUid;
    public FollowingAdapter(DatabaseReference ref, Context context, String searchName) {
        this.ref = ref;
        this.searchName = searchName;
        this.listOfUsers = new ArrayList<>();
        this.listOfUid = new ArrayList<>();
        ref.child("username_to_uid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(searchName)) {
                    for (DataSnapshot child : snapshot.child(searchName).getChildren()) {
                        listOfUid.add(child.toString());
                    }
                }
                if (listOfUid.size() != 0) {

                    ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                               if (listOfUid.contains(child.toString())) {
                                   listOfUsers.add(snapshot.child(child.toString()).getValue(User.class));
                               }
                            }
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
    public FollowingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new FollowingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingHolder holder, int position) {
        holder.displayName.setText(listOfUsers.get(position).getDisplayName());
        holder.profileIcon.setImageResource(R.drawable.profile_icon);

    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }
}
