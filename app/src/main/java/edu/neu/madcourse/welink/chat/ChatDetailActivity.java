package edu.neu.madcourse.welink.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.neu.madcourse.welink.R;

//import androidx.fragment.app.FragmentContainerView;

public class ChatDetailActivity extends AppCompatActivity implements EmojiSelectionsFragment.ButtonCallback{
    private String fromUser;
    //    private String toUser;
    private String keypair;
    private String friendToken;

    private RecyclerView.LayoutManager layoutManger;
    private RecyclerView recyclerView;
    private ChatDetailViewAdapter chatDetailViewAdapter;
    private DatabaseReference dbRef;

    private static final String TAG = ChatDetailActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the following format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAI6WaV_U:APA91bHjfKaFw8qeNCVPpe57qxEj3FAgDJBGlOGbHIqzE2DIJ785kXD1IYtJa2eD_Cm9F2LMWLcoWa90OMNXxatmdr_AYz7enmKq42Q6bjI-UjVJdlwpNxZvaIbrw1bOe_GxuMjqe57S";

    // This is the client registration token
    private String CLIENT_REGISTRATION_TOKEN = "";

    private int singleChatStickerCount = 0;

//    @Override
//    protected void onStop() {
//        super.onStop();
//        dbRef.child("sentCount").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Long newCount = 0L;
//                if(snapshot.hasChild(fromUser)) {
//                    newCount = (Long)snapshot.child(fromUser).getValue();
//                }
//                newCount += singleChatStickerCount;
//                dbRef.child("sentCount").child(fromUser).setValue(newCount);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    protected void onStart() {
        super.onStart();
        singleChatStickerCount = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            fromUser = intent.getExtras().getString("fromUser");
            keypair = intent.getExtras().getString("pairKey");
            friendToken = intent.getExtras().getString("friend_token");

            CLIENT_REGISTRATION_TOKEN = friendToken;
        }
        dbRef = FirebaseDatabase.getInstance().getReference();
        createRecyclerView();
    }

    private void createRecyclerView() {
        layoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.chat_detail_rv);
        recyclerView.setHasFixedSize(true);
        chatDetailViewAdapter = new ChatDetailViewAdapter(dbRef, keypair,fromUser);
        recyclerView.setAdapter(chatDetailViewAdapter);
        recyclerView.setLayoutManager(layoutManger);
    }

    @Override
    public void launchAction(String selected) {
//        singleChatStickerCount++;
//        dbRef.child("messages").child(keypair).push().setValue(new ChatMessage(selected, fromUser));
//        sendMessageToDevice(fromUser, selected);
    }

    public void sendMessageToDevice(String fromWhom, String selected) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessageToDevice(CLIENT_REGISTRATION_TOKEN, fromWhom,selected);
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void sendMessageToDevice(String targetToken, String fromWhom, String emo) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "From " + fromWhom);
            jNotification.put("body", "Message: " + emo);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
            jdata.put("title","From " + fromWhom);
            jdata.put("content","Message: " + emo);

            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data",jdata);


            /***
             * The Payload object is now populated.
             * Send it to Firebase to send the message to the appropriate recipient.
             */
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
//                    Toast.makeText(ChatDetailActivity.this,resp,Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
