package edu.neu.madcourse.welink.chat;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.ChatMessage;

public class ChatDetailViewAdapter extends RecyclerView.Adapter<ChatDetailViewHolder>{
    private ArrayList<DataSnapshot> msgSnapshots;
    RecyclerView rv;
    String currUser;

    ChatDetailViewAdapter(DatabaseReference ref, String keypair, String currUser) {
        this.currUser = currUser;
        ref.child("messages").child(keypair).addChildEventListener(listener);
        msgSnapshots = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        rv = recyclerView;
    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            msgSnapshots.add(snapshot);
            notifyItemInserted(msgSnapshots.size()-1);
            rv.smoothScrollToPosition(msgSnapshots.size() -1);
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
    public ChatDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent,false);
        return new ChatDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailViewHolder holder, int position) {
        DataSnapshot newMsgSnapshot = msgSnapshots.get(position);
        String name = newMsgSnapshot.getValue(ChatMessage.class).getSenderUserName();
        String message = newMsgSnapshot.getValue(ChatMessage.class).getMessage();
        holder.message.setText(message);
        if(name.equals(currUser)) {
            changeDisplayForSelfMessage(holder,name);
        } else {
            changeDisplayForFriendMessage(holder,name);
        }
        holder.sender.setText(formatName(name));
    }

    @Override
    public int getItemCount() {
        return msgSnapshots.size();
    }

    private void changeDisplayForSelfMessage(ChatDetailViewHolder holder,String name) {
        RelativeLayout.LayoutParams senderParams = (RelativeLayout.LayoutParams)holder.sender.getLayoutParams();
        senderParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        senderParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        holder.sender.setLayoutParams(senderParams);

        RelativeLayout.LayoutParams msgParams = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
        msgParams.removeRule(RelativeLayout.END_OF);
        msgParams.addRule(RelativeLayout.START_OF, R.id.message_sender);
        holder.message.setLayoutParams(msgParams);
    }

    private void changeDisplayForFriendMessage(ChatDetailViewHolder holder,String name) {
        RelativeLayout.LayoutParams senderParams = (RelativeLayout.LayoutParams)holder.sender.getLayoutParams();
        senderParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        senderParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        holder.sender.setLayoutParams(senderParams);

        RelativeLayout.LayoutParams msgParams = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
        msgParams.removeRule(RelativeLayout.START_OF);
        msgParams.addRule(RelativeLayout.END_OF, R.id.message_sender);
        holder.message.setLayoutParams(msgParams);
    }



    private String formatName(String name) {
        if(name.length() > 8) {
            name = name.substring(0,8) + "...";
        }
        return name;
    }
}
