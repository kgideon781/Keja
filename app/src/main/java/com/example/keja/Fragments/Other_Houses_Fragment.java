package com.example.keja.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keja.HouseOwnerActivity;
import com.example.keja.KejaHelper;
import com.example.keja.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Other_Houses_Fragment extends Fragment {

    CircleImageView img_house;
    TextView txt_apartment_name;

    public View v;
    RecyclerView mRecycler;
    String user_id;
    DatabaseReference dbRef;
    Query query;
    public Other_Houses_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_other__houses, container, false);

        HouseOwnerActivity activity = (HouseOwnerActivity) getActivity();
        String user = activity.getMyData();

        mRecycler = v.findViewById(R.id.OwnerRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setHasFixedSize(true);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Shabee");
        query = dbRef.orderByChild("user_id").equalTo(user);


        img_house = v.findViewById(R.id.img_house);
        txt_apartment_name = v.findViewById(R.id.txt_apartment_name);

        FirebaseRecyclerAdapter<KejaHelper, HouseViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<KejaHelper, HouseViewHolder>(
                KejaHelper.class,
                R.layout.owner_houses_item,
                HouseViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(HouseViewHolder houseViewHolder, KejaHelper kejaHelper, int i) {
                houseViewHolder.setImage(getContext(), kejaHelper.getImage());
                houseViewHolder.setSomething(kejaHelper.getLocation());
            }

            @Override
            public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                HouseViewHolder houseViewHolder1 = super.onCreateViewHolder(parent, viewType);
                return houseViewHolder1;
            }
        };

     mRecycler.setAdapter(firebaseRecyclerAdapter);

        return v;
    }
    public static class HouseViewHolder extends RecyclerView.ViewHolder {
        ImageView img_house;
        TextView txt_apartment_name;
        public HouseViewHolder(View itemView){
            super(itemView);

            img_house = itemView.findViewById(R.id.img_house);
            txt_apartment_name = itemView.findViewById(R.id.txt_apartment_name);

        }
        void setSomething( String name){
            //DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("");
            txt_apartment_name.setText(name);
        }
        void setImage(final Context ctx,String image){

            Picasso.with(ctx).load(image).placeholder(R.drawable.ic_profile_holder).into(img_house);
        }
    }

}
