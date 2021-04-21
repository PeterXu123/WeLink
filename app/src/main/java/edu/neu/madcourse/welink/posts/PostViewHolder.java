package edu.neu.madcourse.welink.posts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView username;
    TextView time;
    TextView content;
    ImageView userImage;
    List<ImageView> postImages;
    User user;
    OpenProfileListener listener;

    public PostViewHolder(@NonNull View itemView, final OpenProfileListener listener) {
        super(itemView);
        this.listener = listener;
        this.username = itemView.findViewById(R.id.post_card_user_name);
        this.time = itemView.findViewById(R.id.post_card_time);
        this.content = itemView.findViewById(R.id.post_card_text);
        this.userImage = itemView.findViewById(R.id.post_card_user_image);
        postImages = new ArrayList<>();
        postImages.add(itemView.findViewById(R.id.post_card_post_image1));
        postImages.add(itemView.findViewById(R.id.post_card_post_image2));
        postImages.add(itemView.findViewById(R.id.post_card_post_image3));

        this.username.setOnClickListener(openProfile);
        this.userImage.setOnClickListener(openProfile);
    }

    private View.OnClickListener openProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(user);
            }
        }
    };
}
