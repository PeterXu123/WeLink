package edu.neu.madcourse.welink.chat;


import android.app.Activity;
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

import java.util.ArrayList;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListHolder> {


    private ArrayList<String> listOfChat;
    private DatabaseReference ref;
    private String currentUser;
    private Context context;
    public ChatListAdapter(DatabaseReference ref,  String currentUser, Context context) {
        this.ref = ref;
        ref.child("users").child(currentUser).child("chats").addChildEventListener(mListener);
        listOfChat = new ArrayList<>();
        this.currentUser = currentUser;
        this.context = context;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String chatName = snapshot.getValue(String.class);
            listOfChat.add(chatName);
            notifyDataSetChanged();


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


    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {
        holder.displayName.setText(listOfChat.get(position));
        holder.profileIcon.setImageResource(R.drawable.profile_icon);
        holder.profileIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pairKey = currentUser.compareTo(listOfChat.get(position)) < 0 ?
                        currentUser +  "_" +listOfChat.get(position) : listOfChat.get(position) + "_" + currentUser;
                ref.child("users").child(listOfChat.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(context, ChatDetailActivity.class);
                        intent.putExtra("fromUser", currentUser);
                        intent.putExtra("toUser", listOfChat.get(position));
                        intent.putExtra("pairKey", pairKey);
                        intent.putExtra("chat_token", snapshot.getValue(User.class).getToken());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ((Activity) context).startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        holder.displayName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pairKey = currentUser.compareTo(listOfChat.get(position)) < 0 ?
                        currentUser +  "_" +listOfChat.get(position) : listOfChat.get(position) + "_" + currentUser;
                ref.child("users").child(listOfChat.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(context, ChatDetailActivity.class);
                        intent.putExtra("fromUser", currentUser);
                        intent.putExtra("toUser", listOfChat.get(position));
                        intent.putExtra("pairKey", pairKey);
                        intent.putExtra("chat_token", snapshot.getValue(User.class).getToken());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ((Activity) context).startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });


    }

    @Override
    public int getItemCount() {
        return listOfChat.size();
    }
}
