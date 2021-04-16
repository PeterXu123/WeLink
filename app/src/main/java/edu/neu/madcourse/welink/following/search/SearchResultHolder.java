package edu.neu.madcourse.welink.following.search;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.profile.ProfileActivity;

public class SearchResultHolder extends RecyclerView.ViewHolder {
    ImageView profileIcon;
    TextView displayName;

    String uri;
    String uid;
    public SearchResultHolder(@NonNull View itemView) {
        super(itemView);
        this.profileIcon = itemView.findViewById(R.id.profileImage);
        this.displayName = itemView.findViewById(R.id.displayName);
//        Picasso.with(getContext()).load(imgUrl).fit().into(contentImageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {
//                        listener.onButtonClick(position);
                    Intent intent = new Intent(itemView.getContext(), ProfileActivity.class);
//                    intent.putExtra("profile_icon", profileIcon)
                    intent.putExtra("username", displayName.getText());
                    intent.putExtra("iconUrl", uri);
                    intent.putExtra("uid", uid);
                    itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
