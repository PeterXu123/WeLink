package edu.neu.madcourse.welink.chat;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;

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
    Context context;
    Intent intent;
    public ChatListAdapter(Context context, Intent intent) {
        listOfChat = new LinkedList<>();
        this.context = context;
        this.intent = intent;


    }



    public void addNewChaterToAdapter(User curChater, User curUser) {
        listOfChat.add(0, new ChatListRow(curChater, curUser));

        notifyItemInserted(0);

    }

    public boolean chatListRowEqual(ChatListRow curChatListRow1, ChatListRow curChatListRow2){
        String id1Chater = curChatListRow1.getCurChater().getUid();
        String id1User = curChatListRow1.getCurUser().getUid();
        String id2Chater = curChatListRow2.getCurChater().getUid();;
        String id2User = curChatListRow1.getCurUser().getUid();
        return (id1Chater.equals(id2Chater) && id1User.equals(id2User))
                || (id1Chater.equals(id2User) && id1User.equals(id2Chater));
    }

    public void removeChaterFromAdapter(User curChater, User curUser) {
        ChatListRow curChatListRow = new ChatListRow(curChater, curUser);
        for(int i=0; i<listOfChat.size(); i++) {
            if(chatListRowEqual(listOfChat.get(i), curChatListRow)){
                listOfChat.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }

    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        ChatListHolder viewHolder = new ChatListHolder(view, context, new Intent(intent));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {
        ChatListRow chatListRow = listOfChat.get(position);
        User curChater = chatListRow.getCurChater();
        User curUser = chatListRow.getCurUser();
        String chaterImgUrl = curChater.getIconUrl();
        try {
            if(chaterImgUrl != null && chaterImgUrl.length() > 0) {
                Picasso.get().load(chaterImgUrl).into(holder.profileIcon);
            }
            else {
                holder.profileIcon.setImageResource(R.drawable.profile_icon);
            }
        } catch (Exception exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        holder.curChater = curChater;
        holder.curUser = curUser;
        holder.displayName.setText(curChater.getDisplayName());
    }


    @Override
    public int getItemCount() {
        return listOfChat.size();
    }

}
