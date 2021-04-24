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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText displayName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.displayName = findViewById(R.id.register_displayName);
        this.email = findViewById(R.id.register_email);
        this.password = findViewById(R.id.register_password);
        this.confirmPassword = findViewById(R.id.register_confirm_password);
        this.registerButton = findViewById(R.id.register_button);
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        findViewById(R.id.register_button_in_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean checkEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean checkPassword() {
        String confirm = confirmPassword.getText().toString();
        String pass = password.getText().toString();

        return pass.length() >= 6
                && confirm.equals(pass);
    }
    //                Firebase Database paths must not contain '.', '#', '$', '[', or ']'
    private boolean checkUsername(String username) {
        return
                !TextUtils.isEmpty(username) && !username.contains(".") && !username.contains("#") && !username.contains("$") && !username.contains("[") && !username.contains("]");
    }

    private void register() {
        displayName.setError(null);
        email.setError(null);
        password.setError(null);
        String username = displayName.getText().toString();
        String email_ = email.getText().toString();
        String password_ = password.getText().toString();
        boolean success = true;
        View errorView = null;
        if (TextUtils.isEmpty(password_) || !checkPassword()) {
            password.setError("Password should be same as confirm " +
                    "password and the length shouldn't be less than 6");
            errorView = password;
            success = false;
        }

        if (TextUtils.isEmpty(email_)) {
            email.setError("This field is required");
            errorView = email;
            success = false;
        }
        else if (!checkEmail(email_)) {
            email.setError("Your email is invalid");
            errorView = email;
            success = false;
        }

        if (!checkUsername(username)) {
            displayName.setError("This field is required and it shouldn't contain '.', '#', '$', '[', or ']'");
            errorView = displayName;
            success = false;
        }

        if (success) {
            createUser();
        }
        else {
            errorView.requestFocus();
        }


    }

    private void showError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(errorMessage)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void createUser() {
        final String username = displayName.getText().toString();
        final String email_ = email.getText().toString();
        final String password_ = password.getText().toString();
        if (checkEmail(email_) && checkPassword()) {
            mAuth.createUserWithEmailAndPassword(email_, password_).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        showError("Registration failed and the email has been used for registration");
                    }
                    else {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build();
                        mDatabaseReference.child("username_to_uid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(username)) {
                                    if (!snapshot.child(username).hasChild(user.getUid())) {
                                        mDatabaseReference.child("username_to_uid").child(username).child(user.getUid()).setValue(user.getUid());
                                    }
                                }
                                else {
                                    mDatabaseReference.child("username_to_uid").child(username).child(user.getUid()).setValue(user.getUid());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            User user1 = new User(username);

                                            user1.setLocation(null);
                                            user1.setUid(user.getUid());
                                            user1.setEmail(email_);
                                            user1.setIconUrl(null);
                                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (task.isSuccessful()) {
                                                        user1.setToken(task.getResult());
                                                        mDatabaseReference.child("users").child(user1.getUid()).setValue(user1);
                                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                }
                                            });


                                        }
                                    }
                                });

                    }

                }
            });
        }
        else {
            showError("Registration failed");
        }

    }

}