package edu.neu.madcourse.welink.following.search;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class SearchResultHolder extends RecyclerView.ViewHolder {
    ImageView profileIcon;
    TextView displayName;
    Button detailButton;

    public SearchResultHolder(@NonNull View itemView) {
        super(itemView);
        this.profileIcon = itemView.findViewById(R.id.profileImage);
        this.displayName = itemView.findViewById(R.id.displayName);
        this.detailButton = itemView.findViewById(R.id.detailButton);
//        Picasso.with(getContext()).load(imgUrl).fit().into(contentImageView);


    }
}