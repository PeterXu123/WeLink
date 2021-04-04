package edu.neu.madcourse.welink.chat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.chat.ChatDetailActivity;
import edu.neu.madcourse.welink.utility.User;

public class ChatListActivity extends AppCompatActivity {
    private String username;
    private String currentUserToken;
    private EditText searchName;
    private RecyclerView mSearchResultListView;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mChatListAdapter;
    private Button goSearch;
    private Handler resultHandler;

    class backThread extends Thread {
        backThread() {

        }

        @Override
        public void run() {
            mChatListAdapter = new ChatListAdapter(mDatabaseReference, username, ChatListActivity.this);
//            mChatListAdapter.setContext(getApplicationContext());

            resultHandler.post(new Runnable() {
                @Override
                public void run() {
                    mSearchResultListView.setAdapter((RecyclerView.Adapter) mChatListAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mSearchResultListView.setLayoutManager(layoutManager);
                }
            });

        }
    }


    public String getCurrUser() {
        return this.username;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new backThread().start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // set username, and current user's token
        Bundle bundle = getIntent().getExtras();
        resultHandler = new Handler(Looper.myLooper());
        if (bundle != null) {

            username = bundle.getString("username");
            currentUserToken = bundle.getString("token");
        }
        searchName = findViewById(R.id.searchChatName);
        goSearch = findViewById(R.id.searchChatButton);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mSearchResultListView = findViewById(R.id.search_list_view);
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchName.getText().toString().equals("")) {
                    String newChatName = searchName.getText().toString();
                    mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(newChatName)) {
                                User current_user = snapshot.child(username).getValue(User.class);
                                if (current_user.getChats() != null && current_user.getChats().contains(newChatName)) {
                                    User newChat = snapshot.child(newChatName).getValue(User.class);
                                    String pairKey = username.compareTo(newChatName) < 0 ?
                                            username + "_" + newChatName : newChatName + "_" + username;

                                    Log.d("toUser Token from chatlist:", newChat.getToken());
                                    Log.d("toUser Token from chatlist:", newChat.getToken());

                                    Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
                                    intent.putExtra("fromUser", username);
                                    intent.putExtra("toUser", newChatName);
                                    intent.putExtra("pairKey", pairKey);
                                    intent.putExtra("chat_token", newChat.getToken());
                                    v.getContext().startActivity(intent);
                                } else {
                                    List<String> newChats = new ArrayList<>();
                                    if (current_user.getChats() != null) {
                                        newChats = current_user.getChats();
                                    }
                                    newChats.add(newChatName);
                                    current_user.setChats(newChats);
                                    // set chat's chat
                                    User newChat = snapshot.child(newChatName).getValue(User.class);

                                    List<String> chat_chats = newChat.getChats() == null ? new ArrayList<>() : newChat.getChats();
                                    chat_chats.add(username);
                                    newChat.setChats(chat_chats);
                                    DatabaseReference newChatRef = mDatabaseReference.child("users").child(newChatName);
                                    newChatRef.setValue(newChat);
                                    mDatabaseReference.child("users").child(username).setValue(current_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            String pairKey = current_user.getDisplayName().compareTo(newChatName) < 0 ?
                                                    current_user.getDisplayName() + "_" + newChatName : newChatName + "_" + current_user.getDisplayName();
                                            Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
                                            intent.putExtra("fromUser", username);
                                            intent.putExtra("toUser", newChatName);
                                            intent.putExtra("pairKey", pairKey);
                                            intent.putExtra("chat_token", newChat.getToken());
                                            Log.d("Wtf", "why");
                                            v.getContext().startActivity(intent);
                                        }
                                    });

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Username doesn't exist", Toast.LENGTH_LONG).show();
                                searchName.setText("");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }
}