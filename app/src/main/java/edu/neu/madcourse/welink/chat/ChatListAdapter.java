package edu.neu.madcourse.welink.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

// todo: (After we finish the ChatDetailActivity) ChatListAdapter is used to update layout using
//  the data from database. There are 2 situations:
//  1. a new chater chats, so we add it on top.
//  2. an existing chater chats, so we put it on top.
//  both of them needs listener on message_record, so we can know a new message comes
//  and then to see if we need to add it to list's first position or to delete it from original
//  position and add it to list's first position.
public class ChatListAdapter extends RecyclerView.Adapter<ChatListHolder> {
    private LinkedList<ChatListRow> listOfChat;  // we need a lot of inserts and deletes
//    private DatabaseReference ref;
//    private User currentUser;
    private Context context;
    public ChatListAdapter(DatabaseReference ref, User currentUser,
                           List<User> curChatersOfCurrentUser, Context context) {
//        this.ref = ref;
//        String curUserId = currentUser.getUid();
//        ref.child("message_record").addChildEventListener(someListener);
//        ref.child("chater_relation").addChildEventListener(mListener);
        listOfChat = new LinkedList<>();
//        this.currentUser = currentUser;
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addNewChaterToAdapter(String imgUrl, String username) {
        listOfChat.add(0, new ChatListRow(imgUrl, username));
//        notifyItemInserted(listOfChat.size()-1);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new ChatListHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {
        ChatListRow chatListRow = listOfChat.get(position);
        String imgUrl = chatListRow.getImgUrl();
        try {
            if(imgUrl != null && imgUrl.length() > 0) {
                Picasso.get().load(imgUrl).into(holder.profileIcon);
            }
        } catch (Exception exception) {
            System.out.println(Arrays.toString(exception.getStackTrace())
            );
        }
        holder.displayName.setText(chatListRow.getUsername());
    }


    @Override
    public int getItemCount() {
        return listOfChat.size();
    }
}
