<<<<<<< Updated upstream
package edu.neu.madcourse.welink.chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class ChatDetailViewHolder extends RecyclerView.ViewHolder {
    TextView sender;
    TextView message;
    public ChatDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        this.sender = itemView.findViewById(R.id.message_sender);
        this.message = itemView.findViewById(R.id.message_content);
    }
}
=======
package edu.neu.madcourse.welink.chat;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class ChatDetailViewHolder extends RecyclerView.ViewHolder {
    TextView sender;
    TextView message;
    public ChatDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        this.sender = itemView.findViewById(R.id.message_sender);
        this.message = itemView.findViewById(R.id.message_content);
    }
}
>>>>>>> Stashed changes
