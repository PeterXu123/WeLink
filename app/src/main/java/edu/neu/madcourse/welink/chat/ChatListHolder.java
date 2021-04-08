package edu.neu.madcourse.welink.chat;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

// todo: currently we don't show text in chat list
//  if we need to show that, we should have another field:
//  TextView msgBrief
public class ChatListHolder extends RecyclerView.ViewHolder {
    ImageView profileIcon;
    TextView displayName;
    Context context;
    private String clickMessage;
    String chaterId;
    User curChater;
    User curUser;
    public ChatListHolder(@NonNull View itemView, Context context, Intent intent) {
        super(itemView);
        this.profileIcon = itemView.findViewById(R.id.profileImage);
        this.displayName = itemView.findViewById(R.id.displayName);
        this.context = context;
        try {
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("curChaterID", curChater.getUid());
                            intent.putExtra("curChaterToken", curChater.getToken());
                            intent.putExtra("curChaterName", curChater.getDisplayName());
                            intent.putExtra("curUserID", curUser.getUid());
                            intent.putExtra("curUserToken", curUser.getToken());
                            intent.putExtra("curUserName", curUser.getDisplayName());
                            
                            String username = curUser.getDisplayName();
                            String chaterName = curChater.getDisplayName();

                            intent.putExtra("fromUser", username);
                            intent.putExtra("toUser", chaterName);
                            String pairKey = username.compareTo(chaterName) < 0 ?
                                    username + "_" + chaterName : chaterName + "_" + username;
                            intent.putExtra("pairKey", pairKey);
                            intent.putExtra("chater_token", curChater.getToken());

                            context.startActivity(intent);
                        }
                    }
            );
            clickMessage = "Succeeded to open the page";
        } catch (Exception exception){
            // catch all exception.
            clickMessage = "Failed to open the page";
        }
    }
}
