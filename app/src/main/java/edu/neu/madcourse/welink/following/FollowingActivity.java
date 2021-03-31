package edu.neu.madcourse.welink.following;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.welink.R;

public class FollowingActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String uid;
    private String displayName;
    private String email;
    private String token;
    private Button searchButton;
    private EditText searchName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth =  FirebaseAuth.getInstance();
        if (getIntent().getExtras() != null) {
            uid = getIntent().getExtras().getString("uid");
            displayName = getIntent().getExtras().getString("displayName");
            email = getIntent().getExtras().getString("email");
            token = getIntent().getExtras().getString("token");
        }
        if (uid == null || displayName == null || email == null) {
            FirebaseUser u = mAuth.getCurrentUser();
            uid = u.getUid();
            displayName = u.getDisplayName();
            email = u.getEmail();
        }
        searchName = findViewById(R.id.searchName);
        searchButton = findViewById(R.id.searchUserButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchName.getText().toString().equals("")) {
                    Intent intent = new Intent(FollowingActivity.this, SearchResultActivity.class);
                    intent.putExtra("search_name", searchName.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
//        mDatabaseReference.child("following").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (uid != null) {
//                    if (!snapshot.hasChild(uid)) {
//                        mDatabaseReference.child("following").child(uid).
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


}