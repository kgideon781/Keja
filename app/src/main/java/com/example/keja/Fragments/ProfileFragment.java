package com.example.keja.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.keja.ProfileActivity;
import com.example.keja.R;
import com.example.keja.SetupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView img;
    TextView userName,status;
    Button changeProfile;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;

    View v;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        img = (CircleImageView) v.findViewById(R.id.profileImgs);
        userName = v.findViewById(R.id.user_names);
        status = v.findViewById(R.id.text_statuss);
        changeProfile = v.findViewById(R.id.profile_pic_btns);
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    Intent i = new Intent(getContext(), SetupActivity.class);
                    startActivity(i);
                }
            }
        });
        loadData();
        return v;
    }

    private void loadData() {
        if (mAuth.getCurrentUser() != null) {
            String user = mAuth.getCurrentUser().getUid();

            dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user);
            dbRef.keepSynced(true);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userName.setText(dataSnapshot.child("name").getValue().toString());
                    final String image = dataSnapshot.child("image").getValue().toString();
                    final CircleImageView set_image = v.findViewById(R.id.profileImgs);
                    Picasso.with(getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(set_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext()).load(image).into(set_image);
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
