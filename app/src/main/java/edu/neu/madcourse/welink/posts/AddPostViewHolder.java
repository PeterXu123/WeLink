package edu.neu.madcourse.welink.posts;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class AddPostViewHolder extends RecyclerView.ViewHolder{
    ImageView image;
    ImageButton deleteBtn;

    public AddPostViewHolder(@NonNull View itemView, final DeleteImageListener listener) {
        super(itemView);
        image = itemView.findViewById(R.id.new_post_image_card_image);
        deleteBtn = itemView.findViewById(R.id.new_post_image_card_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    int pos = getLayoutPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(pos);
                    }
                }
            }
        });
    }
}
