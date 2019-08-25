package com.example.keja;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    CircleImageView mImageBtn;
    EditText mSetupField;
    Button mSubmitBtn;
    Uri mImageUri = null;

    ProgressDialog mProgress;
    TextInputLayout txtInput;

    DatabaseReference mDatabaseUsers;

    StorageReference mStorageImage;

    FirebaseAuth mAuth;
    String downloadUrl;

    static final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Setup Your Profile");
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        byte[] bytes = getIntent().getByteArrayExtra("image");
        String user = getIntent().getStringExtra("User_name");

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);



        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mStorageImage = FirebaseStorage.getInstance().getReference().child("profile_picures");

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mImageBtn = findViewById(R.id.profileImageBtn);
        mImageBtn.setImageBitmap(bitmap);
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent();
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(gallery_intent,GALLERY_REQUEST_CODE);
            }
        });
        mSetupField = findViewById(R.id.profileNameField);
        mSetupField.setText(user);
        mSubmitBtn = findViewById(R.id.submitBtn);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSetupAccount();

            }
        });
    }

    private void startSetupAccount() {
        final String name = mSetupField.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(name) && mImageUri != null){

            mProgress.setMessage("Saving Info...");
            mProgress.show();
            final StorageReference image_path = mStorageImage.child("Users").child(System.currentTimeMillis()+".jpg");
            image_path.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final String locate = mSetupField.getText().toString().trim();
                    /*final String placet = edtPlace.getEditText().getText().toString().trim();
                    final String pricet = edtPrice.getEditText().getText().toString().trim();*/

                    image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String uid = mAuth.getCurrentUser().getUid();
                            String url = uri.toString();
                            ProfileUpload imageUploadInfo = new ProfileUpload(locate, url);
                            String ImageUploadId = mDatabaseUsers.push().getKey();
                            mDatabaseUsers.child(uid).setValue(imageUploadInfo);
                            Toast.makeText(SetupActivity.this,"Saved.",Toast.LENGTH_SHORT).show();
                            //System.out.println(taskSnapshot.getStorage().getDownloadUrl());
                            mProgress.dismiss();
                        }
                    });
                           /* ImageUploadInfo imageUploadInfo = new ImageUploadInfo(locate,placet,pricet, taskSnapshot.getUploadSessionUri().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                            Toast.makeText(SaverActivity.this,"Saved.",Toast.LENGTH_SHORT).show();
                            System.out.println(taskSnapshot.getStorage().getDownloadUrl());
                            pb.setVisibility(View.INVISIBLE);
                            */
                }
            });
           /* if(mImageUri.getLastPathSegment() != null) {
                final StorageReference filePath = mStorageImage.child(mImageUri.getLastPathSegment());
                final UploadTask uploadTask = filePath.putFile(mImageUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                downloadUrl = filePath.getStorage().getReference().getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()){
                                    Uri url = task.getResult();
                                    mDatabaseUsers.child(user_id).child("name").setValue(name);
                                    mDatabaseUsers.child(user_id).child("image").setValue(url);

                                    mProgress.dismiss();

                                    Intent setupIntent = new Intent(SetupActivity.this, MainActivity.class);
                                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(setupIntent);
                                }
                            }
                        });


                    }
                });
             *//*   filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUri = taskSnapshot.
                        mDatabaseUsers.child(user_id).child("name").setValue(name);
                        mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);

                        mProgress.dismiss();

                        Intent setupIntent = new Intent(SetupActivity.this, MainActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }
                });*//*
            }

*/
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                mImageUri= result.getUri();
                mImageBtn.setImageURI(mImageUri);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();

            }
        }
    }
}
