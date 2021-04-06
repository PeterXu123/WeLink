package edu.neu.madcourse.welink.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.fragmentActivity.FragmentActivity;
import edu.neu.madcourse.welink.utility.User;


public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button sign_in;
    private Button register;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        register = findViewById(R.id.register_button_in_login);
        sign_in = findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);
                password.setError(null);
                boolean success = true;
                View errorView = null;
                if (!checkPassword(password.getText().toString())) {
                    success = false;
                    password.setError("Password shouldn't be empty and length should not be less than 6");
                    errorView = password;
                }
                if (TextUtils.isEmpty(email.getText().toString())) {
                    success = false;
                    email.setError("This field is required");
                    errorView = email;
                }
                else if (!checkEmail(email.getText().toString())) {
                    success = false;
                    email.setError("Your email is invalid");
                    errorView = email;
                }

                if (success) {
                    toLogin();
                }
                else {
                    errorView.requestFocus();
                }


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean checkEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean checkPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    private void showError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(errorMessage)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void toLogin() {
        String login_email = email.getText().toString();
        String login_password = password.getText().toString();

        mAuth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user =  mAuth.getCurrentUser();
                    mDatabaseReference.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User myUser = snapshot.getValue(User.class);
//                            Intent intent = new Intent(MainActivity.this, FollowingActivity.class);
                            Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()) {
                                        myUser.setToken(task.getResult());
                                        mDatabaseReference.child("users").child(myUser.getUid()).child("token").setValue(task.getResult());
                                        intent.putExtra("displayName", myUser.getDisplayName());
                                        intent.putExtra("email", myUser.getEmail());
                                        intent.putExtra("token", myUser.getToken());
                                        intent.putExtra("uid", myUser.getUid());
                                        startActivity(intent);

                                    }
                                    else {
                                        showError("can't get new token");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    showError("Login failed, check password and email");
                }

            }
        });

    }


}