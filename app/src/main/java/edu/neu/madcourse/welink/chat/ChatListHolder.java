package edu.neu.madcourse.welink.chat;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;
// todo: currently we don't show text in chat list
//  if we need to show that, we should have another field:
//  TextView msgBrief
public class ChatListHolder extends RecyclerView.ViewHolder {
    ImageView profileIcon;
    TextView displayName;
    private String clickMessage;
    public ChatListHolder(@NonNull View itemView) {
        super(itemView);
        this.profileIcon = itemView.findViewById(R.id.profileImage);
        this.displayName = itemView.findViewById(R.id.displayName);
        try {
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // todo: will go to ChatDetail if clicked.
//                            Intent browserIntent ;
//                            context.startActivity(browserIntent);
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
