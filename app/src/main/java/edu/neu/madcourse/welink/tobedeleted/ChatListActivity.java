//package edu.neu.madcourse.welink.chat;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import edu.neu.madcourse.welink.R;
//import edu.neu.madcourse.welink.utility.ChaterRelation;
//import edu.neu.madcourse.welink.utility.User;
//
//public class ChatListActivity extends AppCompatActivity {
////    private String curUserName;
////    private User curUser;
////    private String currentUserToken;
////    private EditText searchName;
//    private RecyclerView chatListRecyclerView;
//    private DatabaseReference mDatabaseReference;
//    private ChatListAdapter chatListAdapter;
////    private Button goSearch;
//    private Handler resultHandler;
//    private String currUID;
//    private User curUser;
//    ChaterRelation curChaterRelation;
//    List<String> curChatersIDOfCurrentUser;
//    List<User> curChatersOfCurrentUser;
//    class backThread extends Thread {
//        backThread() {
//
//        }
//
//        @Override
//        public void run() {
//            chatListAdapter = new ChatListAdapter(mDatabaseReference, curUser,
//                    curChatersOfCurrentUser);
//            resultHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    chatListRecyclerView.setAdapter((RecyclerView.Adapter) chatListAdapter);
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                    chatListRecyclerView.setLayoutManager(layoutManager);
//                }
//            });
//
//        }
//    }
//
//
////    public String getCurrUser() {
////        return this.curUserName;
////    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        new backThread().start();
//    }
//
//    private void getChatersOfCurrentUser() {
//        DatabaseReference ref = mDatabaseReference.child("users");
//        ref.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        //Get map of users in datasnapshot
//                        Map<String, User> curUserBuf = (Map<String,User>) dataSnapshot.getValue();
//                        if(curUserBuf != null) {
//                            curChatersOfCurrentUser = new LinkedList<>();
//                            for (String chaterId: curChatersIDOfCurrentUser) {
//                                if (curUserBuf.containsKey(chaterId)) {
//                                    User curChater = curUserBuf.get(chaterId);
//                                    if( curChater != null) {
//                                        curChatersOfCurrentUser.add(curChater);
//                                        chatListAdapter.addNewChaterToAdapter(curChater.getDisplayName(),
//                                                curChater.getIconUrl());
//                                    }
//                                }
//                            }
//                        }
//                        // todo: now we get all users obj, we can render them on layout.
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        //handle databaseError
//                    }
//                });
//    }
//
//    /**
//     * To get id of current chaters.
//     * Inner function will do next step.
//     */
//    private void getChatersIDOfCurrentUser() {
//        String curUserName = curUser.getDisplayName();
//        mDatabaseReference.child("chater_relation").addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChild(currUID)) {
//                            // todo: get chaters' id from firebase.
//                            //  in next function, get chaters' names and img and time using the id
//                            //  from User table so we can render it.
//                            //  (and sort the chaters' list according to the time in Adapter class)
//                            //  When user clicks one user row, we will get in next Activity
//                            //  (i.e. ChatDetail), so we need to pass the list during this time.
//
//                            curChaterRelation = snapshot.child(currUID).getValue(ChaterRelation.class);
//                            // todo: can we use .getValue(ChaterRelation.class); here??? Is it legal
//                            if (curChaterRelation != null) {
//                                curChatersIDOfCurrentUser = curChaterRelation.getChatersId();
//                                getChatersOfCurrentUser();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                }
//        );
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat_list);
//
//        Intent intent = getIntent();
//        if(intent.getExtras() != null) {
//            currUID = intent.getExtras().getString("currUID");
//        }
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        chatListRecyclerView = findViewById(R.id.chat_list_recycler_view);
//        if(!currUID.isEmpty()) {
//            mDatabaseReference.child("users").addListenerForSingleValueEvent(
//                    new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.hasChild(currUID)) {
//                                curUser = snapshot.child(currUID).getValue(User.class);
//                                getChatersIDOfCurrentUser();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//        }
////        goSearch.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (!searchName.getText().toString().equals("")) {
////                    String newChaterName = searchName.getText().toString();
////                    mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                            if (snapshot.hasChild(newChaterName)) {
////                                User current_user = snapshot.child(curUserName).getValue(User.class);
////                                if (current_user.getChaters() != null && current_user.getChaters().contains(newChaterName)) {
////                                    User newChat = snapshot.child(newChaterName).getValue(User.class);
////                                    String pairKey = curUserName.compareTo(newChaterName) < 0 ?
////                                            curUserName + "_" + newChaterName : newChaterName + "_" + curUserName;
////
////                                    Log.d("toUser Token from chatlist:", newChat.getToken());
////                                    Log.d("toUser Token from chatlist:", newChat.getToken());
////
////                                    Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
////                                    intent.putExtra("fromUser", curUserName);
////                                    intent.putExtra("toUser", newChaterName);
////                                    intent.putExtra("pairKey", pairKey);
////                                    intent.putExtra("chat_token", newChat.getToken());
////                                    v.getContext().startActivity(intent);
////                                } else {
////                                    List<String> newChats = new ArrayList<>();
////                                    if (current_user.getChaters() != null) {
////                                        newChats = current_user.getChaters();
////                                    }
////                                    newChats.add(newChaterName);
////                                    current_user.setChaters(newChats);
////                                    // set chat's chat
////                                    User newChat = snapshot.child(newChaterName).getValue(User.class);
////
////                                    List<String> chater_chaters = newChat.getChaters() == null ? new ArrayList<>() : newChat.getChaters();
////                                    chater_chaters.add(curUserName);
////                                    newChat.setChaters(chater_chaters);
////                                    DatabaseReference newChatRef = mDatabaseReference.child("users").child(newChaterName);
////                                    newChatRef.setValue(newChat);
////                                    mDatabaseReference.child("users").child(curUserName).setValue(current_user).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                        @Override
////                                        public void onComplete(@NonNull Task<Void> task) {
////                                            String pairKey = current_user.getDisplayName().compareTo(newChaterName) < 0 ?
////                                                    current_user.getDisplayName() + "_" + newChaterName : newChaterName + "_" + current_user.getDisplayName();
////                                            Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
////                                            intent.putExtra("fromUser", curUserName);
////                                            intent.putExtra("toUser", newChaterName);
////                                            intent.putExtra("pairKey", pairKey);
////                                            intent.putExtra("chat_token", newChat.getToken());
////                                            Log.d("Wtf", "why");
////                                            v.getContext().startActivity(intent);
////                                        }
////                                    });
////
////                                }
////
////                            } else {
////                                Toast.makeText(getApplicationContext(), "Username doesn't exist", Toast.LENGTH_LONG).show();
////                                searchName.setText("");
////
////                            }
////
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////
////                        }
////                    });
////                }
////            }
////        });
//
//
//    }
//}