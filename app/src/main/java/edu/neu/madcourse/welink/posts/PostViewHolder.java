package edu.neu.madcourse.welink.posts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView username;
    TextView time;
    TextView content;
    ImageView image;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.post_card_user_name);
        this.time = itemView.findViewById(R.id.post_card_time);
        this.content = itemView.findViewById(R.id.post_card_text);
        this.image = itemView.findViewById(R.id.post_card_user_image);
    }
}
