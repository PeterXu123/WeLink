package edu.neu.madcourse.welink.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private Uri uri;
    private Uri photoURI;
    private ChatDetailViewAdapter mChatDetailViewAdapter;
    private String roomNumber;
    private String fromUser;
    private String keypair;
    private String chaterToken;
    private String senderUserID;
    private Bitmap imageBitMap;
    private String CLIENT_REGISTRATION_TOKEN;
    private List<String> keyPairsInFirebase;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SAVE_IMAGE = 3;
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
        storage = FirebaseStorage.getInstance();
        mAuth =  FirebaseAuth.getInstance();

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
//                galleryAddPic();
            }
        });
    }
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
//            imageBitMap = (Bitmap) extras.get("data");
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
            imagesFolder.mkdirs();
//            File image = new File(imagesFolder, photoURI);
//            Uri uriSavedImage = Uri.fromFile(image);
//            Intent imageIntent = new Intent();

//            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            startActivityForResult(imageIntent, REQUEST_SAVE_IMAGE);
            uploadFile();
        }
//        else if (requestCode == REQUEST_SAVE_IMAGE) {
//            uploadFile(photoURI);
//        }
    }


    private Bitmap setPic() {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        return bitmap;
    }

    /**
     * To get image url and upload it to firebase. After uploaded, we will send it to firebase's
     * message_record to show another user the information.
     * Referece from:
     * https://stackoverflow.com/questions/40885860/how-to-save-bitmap-to-firebase
     */
    public void uploadFile() {
        if (photoURI != null) {

            StorageReference ref = storage.getReference().
                    child("messageImage/" + photoURI.getLastPathSegment().toString());
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
//                Bitmap bmp = imageBitMap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.flush();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDatabaseReference.child("message_record").child(keypair).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                User user = snapshot.getValue(User.class);
                                Task<Uri> messgae_img_url = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mDatabaseReference.child("message_record").child(keypair).push().setValue(
                                                new ChatMessage(uri.toString(), senderUserID, fromUser,
                                                        System.currentTimeMillis(), keypair));

//                                        mDatabaseReference.child("message_record").child(keypair).push().setValue(
//                                                new ChatMessage(uri.toString(), senderUserID, fromUser,
//                                                        System.currentTimeMillis(), keypair));
                                        System.out.println("uri after camera:"+uri);
//                                        finish();
                                    }
                                });
                                System.out.println("msg_img_url  after camera:" + messgae_img_url );
//                            mDatabaseReference.child("message_record").child(keypair).push()
//                                    .setValue(new ChatMessage(downloadUri.toString(), senderUserID, fromUser,
//                                    System.currentTimeMillis(), keypair));
////                            .setValue(new ChatMessage(imagUri.getLastPathSegment(), senderUserID, fromUser,
////                                    System.currentTimeMillis(), keypair));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.err.println("error here, upload canceled." + uri);
                            }
                        });

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
//        if (imagUri != null) {
//            sref = FirebaseStorage.getInstance().getReference();
//            final StorageReference imageRef = sref.child("android/media") // folder path in firebase storage
//                    .child(imagUri.getLastPathSegment());
//
//            imageRef.putFile(imagUri)
//                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> snapshotTask) {
//                            UploadTask.TaskSnapshot snapshot = snapshotTask.getResult();
//                            // Get the download URL
//                            Task<Uri> downloadUriTask = snapshot.getMetadata().getReference().getDownloadUrl();
//                            while ((!downloadUriTask.isComplete()));
//                            // loop until downloadUriTask gets value back.
//                            // otherwise it will be null.
//                            Uri downloadUri= downloadUriTask.getResult();
//                            // use this download url with imageview for viewing & store this linke to firebase message data
//                            mDatabaseReference.child("message_record").child(keypair).push()
//                                    .setValue(new ChatMessage(downloadUri.toString(), senderUserID, fromUser,
//                                    System.currentTimeMillis(), keypair));
////                            .setValue(new ChatMessage(imagUri.getLastPathSegment(), senderUserID, fromUser,
////                                    System.currentTimeMillis(), keypair));
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // show message on failure may be network/disk ?
//                        }
//                    });
//        }
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
        photoURI = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if(photoURI != null) {uploadFile();}
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
        String[] idPairInLexiOrder = keypair.split("_");
        String id1 = idPairInLexiOrder[0];
        String id2 = idPairInLexiOrder[1];
        mDatabaseReference.child("chater_relation").child(id1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mDatabaseReference.child("chater_relation").child(id1).child(id2)
                                    .setValue(new Date());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mDatabaseReference.child("chater_relation").child(id2).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(id1)) {
                            mDatabaseReference.child("chater_relation").child(id2).child(id1)
                                    .setValue(new Date());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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