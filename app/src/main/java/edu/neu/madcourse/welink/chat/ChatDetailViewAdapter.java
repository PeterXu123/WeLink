package edu.neu.madcourse.welink.chat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.ChatMessage;
import edu.neu.madcourse.welink.utility.User;

public class ChatDetailViewAdapter extends RecyclerView.Adapter<ChatDetailViewHolder>{
    private ArrayList<DataSnapshot> msgSnapshots;
    RecyclerView rv;
    String currUserName;
    User currUser;
    User curChater;
    Context context;
    Activity activity;
    Target curTarget;
    Intent intent;
    Resources resources;
    ChatDetailViewAdapter(DatabaseReference ref, String keypair, User currUser, User curChater, Context context
            , Activity activity, Intent intent, Resources resources) {
        this.currUserName = currUser.getDisplayName();
        this.currUser = currUser;
        this.context = context;
        ref.child("message_record").child(keypair).addChildEventListener(listener);
        msgSnapshots = new ArrayList<>();
        this.activity = activity;
        this.intent = intent;
        this.curChater = curChater;
        this.resources = resources;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        rv = recyclerView;
    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            msgSnapshots.add(snapshot);
            notifyItemInserted(msgSnapshots.size()-1);
            rv.smoothScrollToPosition(msgSnapshots.size() -1);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    @NonNull
    @Override
    public ChatDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent,false);
        return new ChatDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailViewHolder holder, int position) {
        try {
//            holder.sender.();
            holder.sender.setText("");
            holder.message.setText("");
            DataSnapshot newMsgSnapshot = msgSnapshots.get(position);
            String name = newMsgSnapshot.getValue(ChatMessage.class).getSenderUserName();
            String msg = newMsgSnapshot.getValue(ChatMessage.class).getMessage();
            boolean isImage;
            isImage = msg.startsWith("https://firebasestorage.googleapis.com") || msg.startsWith("JPEG_");

            if (isImage) {
//                ssb = new SpannableStringBuilder("1");
                int msgLenBuf = msg.trim().length() - 1;
//                int imgStartIndex = msgLenBuf < 0 ? 0 : msgLenBuf;
                // todo: we can also use image url or bitmap to construct the ImageSpan!! -- zzx
//                StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
//                String curStoragePath = Uri.parse(msg).getLastPathSegment();
//                StorageReference ref = mImageStorage.child("messageImage").child(curStoragePath);
//                        .child(msg);

                curTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
                        drawable.setBounds(0, 0, 1, 1);
                        Bitmap bitmapbuf = ((BitmapDrawable) drawable).getBitmap();
                        Drawable d = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmapbuf, 50, 50, true));
                        holder.message.setText("");
                        holder.message.setCompoundDrawablesWithIntrinsicBounds(d, null,null,null);
                    }

                    @Override
                    public void onBitmapFailed(Exception exception, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.get()
                        .load(msg)   // todo: replace youUrl by message when it has an image format.
                        .into(curTarget);

            } else {
//                ssb = new SpannableStringBuilder(msg);
//                holder.message.setText(ssb, TextView.BufferType.SPANNABLE);
                holder.message.setText("");
                holder.message.setText(msg);
            }

//        holder.message.setText(message);
            if (name == null) {
                name = "Anonymous";
            }
            if (name.equals(currUserName)) {
                changeDisplayForSelfMessage(holder, name);
                holder.sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("uid", currUser.getUid());
                        intent.putExtra("token", currUser.getToken());
                        intent.putExtra("username", currUser.getDisplayName());
                        intent.putExtra("iconUrl", currUser.getIconUrl());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            } else {
                changeDisplayForFriendMessage(holder, name);
                holder.sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("uid", curChater.getUid());
                        intent.putExtra("token", curChater.getToken());
                        intent.putExtra("username", curChater.getDisplayName());
                        intent.putExtra("iconUrl", curChater.getIconUrl());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
            holder.sender.setText(formatName(name));

        } catch (Exception exception) {
            System.err.println("error when get msg from firebase:" + exception.getMessage() +"   "
                    + exception.getStackTrace());
        }
    }


    @Override
    public int getItemCount() {
        return msgSnapshots.size();
    }

    private void changeDisplayForSelfMessage(ChatDetailViewHolder holder,String name) {
        RelativeLayout.LayoutParams senderParams = (RelativeLayout.LayoutParams)holder.sender.getLayoutParams();
        senderParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        senderParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        holder.sender.setLayoutParams(senderParams);

        RelativeLayout.LayoutParams msgParams = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
        msgParams.removeRule(RelativeLayout.END_OF);
        msgParams.addRule(RelativeLayout.START_OF, R.id.message_sender);
        holder.message.setLayoutParams(msgParams);
    }

    private void changeDisplayForFriendMessage(ChatDetailViewHolder holder,String name) {
        RelativeLayout.LayoutParams senderParams = (RelativeLayout.LayoutParams)holder.sender.getLayoutParams();
        senderParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        senderParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        holder.sender.setLayoutParams(senderParams);

        RelativeLayout.LayoutParams msgParams = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
        msgParams.removeRule(RelativeLayout.START_OF);
        msgParams.addRule(RelativeLayout.END_OF, R.id.message_sender);
        holder.message.setLayoutParams(msgParams);
    }



    private String formatName(String name) {
        if(name.length() > 8) {
            name = name.substring(0,8) + "...";
        }
        return name;
    }
}