package edu.neu.madcourse.welink.chat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.ChaterRelation;
import edu.neu.madcourse.welink.utility.User;

public class ChatListFragment extends Fragment {
    private RecyclerView chatListRecyclerView;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter chatListAdapter;
    //    private Button goSearch;
    private Handler resultHandler;
    private String uid;
    private User curUser;
    ChaterRelation curChaterRelation;
    Map<String, Date> curChatersIDOfCurrentUser;
    List<User> curChatersOfCurrentUser;
    Intent curIntent;
    List<String> keyPairsInFirebase;

    class backThread extends Thread {
        backThread() {

        }

        @Override
        public void run() {
//            curIntent = new Intent(getActivity(), MainChatActivity.class);
//            chatListAdapter = new ChatListAdapter(getContext(), new Intent(curIntent));
//            resultHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    chatListRecyclerView.setAdapter((RecyclerView.Adapter) chatListAdapter);
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    chatListRecyclerView.setLayoutManager(layoutManager);
//                }
//            });

        }
    }


//    private String getChaterIdFromKeyPair(String keyPair) {
//        String curId = curUser.getUid();
//        String chaterId = "";
//        String[] idLexiPair = keyPair.split("_");
//        if(idLexiPair[0].equals(curId)) {
//            chaterId = idLexiPair[1];
//        } else {
//            chaterId = idLexiPair[0];
//        }
//        return chaterId;
//    }
//

//    void changeChaterHelper(DataSnapshot snapshot, String chaterId, boolean shouldRemove) {
//        if (snapshot.hasChild(chaterId)) {
//            User curChater = snapshot.child(chaterId).getValue(User.class);
//            if(shouldRemove){
//                chatListAdapter.removeChaterFromAdapter(curChater, curUser);
//            }
//            chatListAdapter.addNewChaterToAdapter(curChater, curUser);
//        }
//    }
//    private void changeChaterInList(String keyPair, boolean shouldRemove) {
//        String chaterId = getChaterIdFromKeyPair(keyPair);
//        if(!chaterId.isEmpty()) {
//            mDatabaseReference.child("message_record").addChildEventListener(
//                    new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                            changeChaterInList(dataSnapshot.getKey(), true);
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                            changeChaterInList(dataSnapshot.getKey(), true);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//        }
//    }
//
//    private void chatListListenToMessageRecords() {
//
//        mDatabaseReference.child("message_record").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
////                changeChaterInList(dataSnapshot.getKey(), false);
//                String keyPair = dataSnapshot.getKey();
//                String chaterId = getChaterIdFromKeyPair(keyPair);
//                changeChaterHelper(dataSnapshot, chaterId, false);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////                changeChaterInList(dataSnapshot.getKey(), true);
//                String keyPair = dataSnapshot.getKey();
//                String chaterId = getChaterIdFromKeyPair(keyPair);
//                changeChaterHelper(dataSnapshot, chaterId, true);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
////                changeChaterInList(dataSnapshot.getKey(), true);
//                String keyPair = dataSnapshot.getKey();
//                String chaterId = getChaterIdFromKeyPair(keyPair);
//                changeChaterHelper(dataSnapshot, chaterId, true);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    @Override
    public void onStart() {
        super.onStart();
//        new backThread().start();
    }

    private void getChatersOfCurrentUser(Context context) {
        DatabaseReference ref = mDatabaseReference.child("users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String, Map<String, String>> curUserBuf
                                = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                        if (curUserBuf != null && curIntent != null && curChatersIDOfCurrentUser != null) {  // && curIntent != null means don't trigger after take photo
                            curChatersOfCurrentUser = new LinkedList<>();
                            for (String chaterId : curChatersIDOfCurrentUser.keySet()) {
                                if (curUserBuf.containsKey(chaterId)) {
                                    Map<String, String> curChaterMap = curUserBuf.get(chaterId);
                                    if (curChaterMap != null) {
                                        User curChater = new User();
                                        curChater.setDisplayName(curChaterMap.getOrDefault("displayName", "Anonymous"));
                                        curChater.setIconUrl(curChaterMap.getOrDefault("iconUrl", "no image"));
                                        curChater.setLocation(curChaterMap.getOrDefault("location", "no location"));
                                        curChater.setEmail(curChaterMap.getOrDefault("email", "no email"));
                                        curChater.setToken(curChaterMap.getOrDefault("token", "no token"));
                                        curChater.setUid(chaterId);
                                        curChatersOfCurrentUser.add(curChater);
                                        if (chatListAdapter == null) {
//                                            Intent intent = new Intent(getActivity(), MainChatActivity.class);
                                            chatListAdapter = new ChatListAdapter(context, new Intent(curIntent));
                                        }
                                        mDatabaseReference.child("chater_relation").child(curUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot innerSnapshot) {
                                                chatListAdapter.addNewChaterToAdapter(curChater, curUser, innerSnapshot.getValue(Date.class));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

//                                        --todo: why add chater here???  4/21/2020 zzx
                                    }
                                }
                            }
                        }
                        // todo: now we get all users obj, we can render them on layout.
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }


    /**
     * To get id of current chaters.
     * Inner function will do next step.
     */
    private void getChatersIDOfCurrentUser(Context context) {
        String curUserName = curUser.getDisplayName();
//        https://cdn.pixabay.com/photo/2016/06/15/16/16/man-1459246_1280.png
        mDatabaseReference.child("chater_relation").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(uid)) {
                            // todo: get chaters' id from firebase.
                            //  in next function, get chaters' names and img and time using the id
                            //  from User table so we can render it.
                            //  (and sort the chaters' list according to the time in Adapter class)
                            //  When user clicks one user row, we will get in next Activity
                            //  (i.e. ChatDetail), so we need to pass the list during this time.

//                            curChaterRelation = snapshot.child(uid).getValue(ChaterRelation.class);
//                            // todo: can we use .getValue(ChaterRelation.class); here??? Is it legal
//                            if (curChaterRelation != null) {
//                                curChatersIDOfCurrentUser = curChaterRelation.getChatersId();
//                                getChatersOfCurrentUser();
//                            }
                            curChatersIDOfCurrentUser = (Map<String, Date>) snapshot.child(uid).getValue();
                            chatListRecyclerView.setAdapter((RecyclerView.Adapter) chatListAdapter);
                            getChatersOfCurrentUser(context);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void helper() {

        if (chatListAdapter == null) {
//                                            Intent intent = new Intent(getActivity(), MainChatActivity.class);
            chatListAdapter = new ChatListAdapter(getContext(), new Intent(curIntent));
        }

//        chatListListenToMessageRecords();
        mDatabaseReference.child("chater_relation").child(uid).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               mDatabaseReference.child("users").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot innerSnapshot) {
                       User u = innerSnapshot.getValue(User.class);
                       chatListAdapter.removeChaterFromAdapter(u, curUser);
                       chatListAdapter.addNewChaterToAdapter(u, curUser, snapshot.getValue(Date.class));
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
                // snapshot.getKey() is chater id  .child(snapshot.getKey())
//                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                Date date = new Date();
//                System.out.println("snapshot.getKey()   " + snapshot.getKey()+"  date:" +formatter.format(snapshot.getValue(Date.class)));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mDatabaseReference.child("users").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot innerSnapshot) {
                        User u  =innerSnapshot.getValue(User.class);
                        chatListAdapter.removeChaterFromAdapter(u, curUser);
                        chatListAdapter.addNewChaterToAdapter(u, curUser, snapshot.getValue(Date.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                mDatabaseReference.child("users").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User u  =snapshot.getValue(User.class);
                        chatListAdapter.removeChaterFromAdapter(u, curUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        resultHandler = new Handler(Looper.myLooper());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_chat_list, container, false);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent.getExtras() != null) {
            uid = intent.getExtras().getString("uid");
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if (!uid.isEmpty()) {
            mDatabaseReference.child("users").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(uid)) {
                                curUser = snapshot.child(uid).getValue(User.class);
                                helper();
//                                chatListListenToMessageRecords();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            curIntent = new Intent(getActivity(), MainChatActivity.class);
            chatListAdapter = new ChatListAdapter(getContext(), new Intent(curIntent));
        chatListRecyclerView = getView().findViewById(R.id.chat_list_recycler_view);
        curIntent = new Intent(getActivity(), MainChatActivity.class);

        chatListRecyclerView.setAdapter((RecyclerView.Adapter) chatListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatListRecyclerView.setLayoutManager(layoutManager);


    }


}