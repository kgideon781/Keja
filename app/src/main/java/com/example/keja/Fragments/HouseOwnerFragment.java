package com.example.keja.Fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keja.Adapters.SearchAdapter;
import com.example.keja.HouseOwnerActivity;
import com.example.keja.Model.Search;
import com.example.keja.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HouseOwnerFragment extends Fragment {
    Button btnCall;
    TextView txtOwner_phone, txtOwner_Whatsapp,btnKey;

    DatabaseReference ref;



    public View v;

    public HouseOwnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  String user_id = getArguments().getString("user_id");
        v = inflater.inflate(R.layout.fragowner, container, false);
       // Bundle bundle = this.getArguments();

        HouseOwnerActivity activity = (HouseOwnerActivity) getActivity();
        String user = activity.getMyData();


        btnKey = v.findViewById(R.id.btnkey);

        btnKey.setText(user);
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(btnKey.getText().toString());

        btnCall = v.findViewById(R.id.btnCall);
        txtOwner_phone = v.findViewById(R.id.txtOwner_Phone_Number);
        txtOwner_Whatsapp = v.findViewById(R.id.txtOwner_Phone_Number_whatsapp);



        // toolbar1= v.findViewById(R.id.toolbar1);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                txtOwner_phone.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


}
