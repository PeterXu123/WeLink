package edu.neu.madcourse.welink.utility;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;

public class BothFollowHolder extends RecyclerView.ViewHolder{
    ImageView profileIcon;
    TextView displayName;

    public BothFollowHolder(@NonNull View itemView) {
        super(itemView);
        this.profileIcon = itemView.findViewById(R.id.profileImage);
        this.displayName = itemView.findViewById(R.id.displayName);
    }
}
