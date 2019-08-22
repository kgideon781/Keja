package com.example.keja;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

public class SaverActivity extends AppCompatActivity {

    Button btnChoose,btnSave;
    private ImageButton setupImage;
    TextInputLayout edtLocation,edtPlace,edtPrice;
    StorageReference dbRef;
    ProgressBar pb;
     Uri mainImageURI = null;
     DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saver);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Post a House");
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        dbRef = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("Shabee");

        setupImage = findViewById(R.id.setupImage);
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    if(ContextCompat.checkSelfPermission(SaverActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SaverActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }
                    else{
                        // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SaverActivity.this);
                    }
                }
            }
        });
       /* btnChoose = findViewById(R.id.buttonChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    if(ContextCompat.checkSelfPermission(SaverActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SaverActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }
                    else{
                        // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SaverActivity.this);
                    }
                }
            }
        });*/
        edtLocation = findViewById(R.id.editLocation);
        edtPlace = findViewById(R.id.editPlace);
        pb = findViewById(R.id.pb);
        edtPrice = findViewById(R.id.editPrice);
      btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locate = edtLocation.getEditText().getText().toString();
                String price = edtPrice.getEditText().getText().toString();
                String place = edtPlace.getEditText().getText().toString();


                    if(setupImage != null){

                        pb.setVisibility(View.VISIBLE);
                    final StorageReference image_path = dbRef.child("Shabee").child(System.currentTimeMillis()+".jpg");
                    image_path.putFile(mainImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final String locate = edtLocation.getEditText().getText().toString().trim();
                             final String placet = edtPlace.getEditText().getText().toString().trim();
                            final String pricet = edtPrice.getEditText().getText().toString().trim();

                            image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String url = uri.toString();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(locate,placet,pricet, url);
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                    Toast.makeText(SaverActivity.this,"Saved.",Toast.LENGTH_SHORT).show();
                                    //System.out.println(taskSnapshot.getStorage().getDownloadUrl());
                                    pb.setVisibility(View.INVISIBLE);
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

                           /* addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){
                                // Get a URL to the uploaded content
                                Uri downloadUrl = task.getResult().getUploadSessionUri();
                                Toast.makeText(SaverActivity.this,"Saved.",Toast.LENGTH_SHORT).show();

                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(SaverActivity.this,"Error: "+error+" has ocurred.",Toast.LENGTH_SHORT).show();
                            }
                            pb.setVisibility(View.INVISIBLE);

                        }
                    });*/

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
