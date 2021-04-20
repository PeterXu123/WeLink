package edu.neu.madcourse.welink.posts;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.welink.R;

public class AddPostAdapter extends RecyclerView.Adapter<AddPostViewHolder>{
    private final List<AddImageItem> images;
    private DeleteImageListener listener;

    public AddPostAdapter(List<AddImageItem> items) {
        this.images = items;
    }
    public void setOnItemClickListener(DeleteImageListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_post_image_card,parent,false);
        return new AddPostViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPostViewHolder holder, int position) {
        holder.image.setImageBitmap( images.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
