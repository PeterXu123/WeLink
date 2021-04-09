package edu.neu.madcourse.welink.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.ChatMessage;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private RecyclerView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private StorageReference sref;
    private ChatDetailViewAdapter mChatDetailViewAdapter;
    private String roomNumber;
    private String fromUser;
    private String keypair;
    private String chaterToken;
    private String senderUserID;
    private String CLIENT_REGISTRATION_TOKEN;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    String mCurrentPhotoPath;
//    private static final String SERVER_KEY = "key=AAAAt8f0ibQ:APA91bGIh8uWpUbSls39AqTV6oCLctbxlSwEZUA9mvbJlqEDmD67bzzwaWTgn8NavnMmQPLebI_--aBUF5yGZFNh3dUAaIdOmtdZqWp-R2ms8PYjiIf6INktP0JuHFxwRjNpXAgzr2H9";

    @Override
    protected void onStart() {
        super.onStart();
        mChatDetailViewAdapter = new ChatDetailViewAdapter(mDatabaseReference, keypair,fromUser,
                getApplicationContext(), this);
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


        FloatingActionButton fab = findViewById(R.id.chat_camera_fab);
        clickCameraFab(fab);

    }

    void clickCameraFab(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    public void uploadFile(Uri imagUri) {
        if (imagUri != null) {
            sref = FirebaseStorage.getInstance().getReference();
            final StorageReference imageRef = sref.child("android/media") // folder path in firebase storage
                    .child(imagUri.getLastPathSegment());

            imageRef.putFile(imagUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> snapshotTask) {
                            UploadTask.TaskSnapshot snapshot = snapshotTask.getResult();
                            // Get the download URL
                            Task<Uri> downloadUriTask = snapshot.getMetadata().getReference().getDownloadUrl();
                            while ((!downloadUriTask.isComplete()));
                            Uri downloadUri= downloadUriTask.getResult();
                            // use this download url with imageview for viewing & store this linke to firebase message data
                            mDatabaseReference.child("message_record").child(keypair).push()
                                    .setValue(new ChatMessage(downloadUri.toString(), senderUserID, fromUser,
                                    System.currentTimeMillis(), keypair));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // show message on failure may be network/disk ?
                        }
                    });
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void dispatchTakePictureIntent() {
        Uri photoURI = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            try {
                File photoFile = createImageFile();

                if (photoFile != null) {
                    if(Build.VERSION.SDK_INT >= 24){
                        photoURI = FileProvider.getUriForFile(this,
                                "com.camerademo.fileprovider",
                                photoFile);
                    }else {
                        photoURI = Uri.fromFile(photoFile);
                    }
                    System.out.println("photoURI!!!!!"+photoURI);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photoURI != null) {uploadFile(photoURI);}
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }
//    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void setupDisplayName() {
        mDisplayName = getIntent().getExtras().getString("curChaterName");
        if (mDisplayName == null) mDisplayName = "Anonymous";
    }


    private void sendMessage() {

        // TODO: Grab the text the user typed in and push the message to Firebase
        String message = mInputText.getText().toString();
        if (message == "") return;

        mDatabaseReference.child("message_record").child(keypair).push()
                .setValue(new ChatMessage(message, senderUserID, fromUser,
                System.currentTimeMillis(), keypair));
        mInputText.setText("");

        // todo: need to check if current chater and user 's key_pair is in the ChatListAdapter's list.
        //  If so, remove it and add it to the index 0. Otherwise, add it to index 0. --zzx
        

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.


    @Override
    public void onStop() {
        super.onStop();
    }



//    /**
//     * Used to get camera and take photos.
//     * Reference from
//     * https://stackoverflow.com/questions/17261834/android-camera-take-picture-and-save-or-send-to-next-activity
//     * @return
//     */
//    private Camera getCameraInstance() {
//        Camera camera = null;
//        try {
//            camera = Camera.open(0);
//            camera.setDisplayOrientation(90);
//        } catch (Exception e) {
//            // cannot get camera or does not exist
//        }
//        return camera;
//    }
//
//    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){
//        @Override
//        public void onShutter() {}
//    };
//
//    Camera.PictureCallback mPicture_RAW = new Camera.PictureCallback(){
//        @Override
//        public void onPictureTaken(byte[] arg0, Camera arg1) {}
//    };
//
//    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
//        @Override
//        public void onPictureTaken(byte[] data, Camera camera) {
//            File pictureFile = getOutputMediaFile();
//            if (pictureFile == null) {
//                return;
//            }
//            try {
//                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.flush();
//                fos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            Intent i = new Intent(getApplicationContext(), );
////            startActivity(i);
//        }
//    };
//
//    protected File getOutputMediaFile() {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"KWAlbum");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d("KWAlbum", "failed to create directory");
//                return null;
//            }
//        }
//        // Create a media file name
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "KW" + ".jpg");
//
//        return mediaFile;
//    }

}
