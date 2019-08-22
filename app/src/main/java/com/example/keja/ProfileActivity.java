package com.example.keja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView img;
    TextView userName,status;
    Button changeProfile;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Profile Activity");
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();

        img = (CircleImageView) findViewById(R.id.profileImg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    CircleImageView img = findViewById(R.id.profileImg);

                    Drawable mdraw = img.getDrawable();
                    Bitmap bmp = ((BitmapDrawable) mdraw).getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();

                    final TextView textv = findViewById(R.id.user_name);
                    Intent i = new Intent(ProfileActivity.this, SetupActivity.class);
                    i.putExtra("User_name", textv.getText().toString());
                    i.putExtra("image", bytes);
                    startActivity(i);
                }
            }
        });
        userName = findViewById(R.id.user_name);
        status = findViewById(R.id.text_status);
        changeProfile = findViewById(R.id.profile_pic_btn);
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    CircleImageView img = findViewById(R.id.profileImg);

                    Drawable mdraw = img.getDrawable();
                    Bitmap bmp = ((BitmapDrawable) mdraw).getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();

                    final TextView textv = findViewById(R.id.user_name);
                    Intent i = new Intent(ProfileActivity.this, SetupActivity.class);
                    i.putExtra("User_name", textv.getText().toString());
                    i.putExtra("image", bytes);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();

    }
    public void loadData(){
        if (mAuth.getCurrentUser() != null) {
            String user = mAuth.getCurrentUser().getUid();

            dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user);
            dbRef.keepSynced(true);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userName.setText(dataSnapshot.child("name").getValue().toString());
                    final String image = dataSnapshot.child("image").getValue().toString();
                    final CircleImageView set_image = findViewById(R.id.profileImg);
                    Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(set_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ProfileActivity.this).load(image).into(set_image);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Toast.makeText(ProfileActivity.this, ref., Toast.LENGTH_SHORT).show();


        }
    }

}
