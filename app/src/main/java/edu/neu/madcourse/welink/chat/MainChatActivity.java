package edu.neu.madcourse.welink.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.ChatMessage;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private RecyclerView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatDetailViewAdapter mChatDetailViewAdapter;
    private String roomNumber;
    private String fromUser;
    private String keypair;
    private String chaterToken;
    private String senderUserID;
    private String CLIENT_REGISTRATION_TOKEN;

    @Override
    protected void onStart() {
        super.onStart();
        mChatDetailViewAdapter = new ChatDetailViewAdapter(mDatabaseReference, keypair,fromUser);
        mChatListView.setAdapter((RecyclerView.Adapter) mChatDetailViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mChatListView.setLayoutManager(layoutManager);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            fromUser = intent.getExtras().getString("fromUser");
            keypair = intent.getExtras().getString("pairKey");
            chaterToken = intent.getExtras().getString("curChaterToken");
            senderUserID = intent.getExtras().getString("curUserID");
            CLIENT_REGISTRATION_TOKEN = chaterToken;
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = findViewById(R.id.chat_list_view);
        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMessage();
                return true;
            }
        });


        // TODO: Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void setupDisplayName() {
//        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
//        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);
        mDisplayName = getIntent().getExtras().getString("curChaterName");
        if (mDisplayName == null) mDisplayName = "Anonymous";


    }


    private void sendMessage() {

        // TODO: Grab the text the user typed in and push the message to Firebase
        String message = mInputText.getText().toString();
        if (message == "") return;

        mDatabaseReference.child("message_record").child(keypair).push()
                .setValue(new ChatMessage(message, senderUserID,
                System.currentTimeMillis(), keypair));
//        Log.d("wtf", mDisplayName);
        mInputText.setText("");
        

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.


    @Override
    public void onStop() {
        super.onStop();
//        mChatDetailViewAdapter.cleanup();

    }

}
