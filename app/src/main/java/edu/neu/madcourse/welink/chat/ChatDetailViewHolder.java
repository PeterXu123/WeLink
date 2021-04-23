package edu.neu.madcourse.welink.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class ChatDetailViewHolder extends RecyclerView.ViewHolder {
    TextView sender;
    TextView message;
    ImageView iv;
    CardView msgFrame;

    public ChatDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        this.sender = itemView.findViewById(R.id.message_sender);
        this.message = itemView.findViewById(R.id.message_content);
        this.iv = itemView.findViewById(R.id.messageImage);
        this.msgFrame = itemView.findViewById(R.id.message_content_frame);

    }
}