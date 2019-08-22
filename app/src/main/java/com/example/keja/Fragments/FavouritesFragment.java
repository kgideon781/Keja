package com.example.keja.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.keja.FavouriteActivity;
import com.example.keja.KejaHelper;
import com.example.keja.LoginActivity;
import com.example.keja.Model.FavModel;
import com.example.keja.ProfileUpload;
import com.example.keja.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class FavouritesFragment extends Fragment {

    View v;
    private RecyclerView mRecycler;
    String location;

    private DatabaseReference databaseReference,ref;
    FirebaseRecyclerAdapter<FavModel, FavViewHolder> firebaseRecyclerAdapter;


    private DatabaseReference mdatabaseUsers;

    private Query mQuery;

    private FirebaseAuth mAuth;

    DatabaseReference dbRef;
    String house;

    //FavHelper favh;
    private FirebaseAuth.AuthStateListener mAuthListener;


    FirebaseAuth.AuthStateListener auth;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favourites, container, false);

        Intent i = getActivity().getIntent();
        location = i.getStringExtra("location");


        mAuth = FirebaseAuth.getInstance();
        final String current_user = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("nLikes").child(current_user);
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Shabee");
       mQuery = databaseReference.orderByChild(current_user);

        mRecycler = v.findViewById(R.id.recyleFavs);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        if (current_user != null) {

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FavModel, FavViewHolder>(
                    FavModel.class,
                    R.layout.favourite_item,
                    FavViewHolder.class,
                    databaseReference
            ) {
                @NonNull
                @Override
                protected void populateViewHolder(final FavViewHolder viewHolder, final FavModel model, int position) {

                  databaseReference.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          FavModel mod = dataSnapshot.getValue(FavModel.class);

                          viewHolder.setPlace(mod.getPrice());
                          viewHolder.setPrice(mod.getPlace());
                          viewHolder.setLocation(mod.getLocation());
                          viewHolder.setImage(getActivity(), mod.getImage());
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });

                    }





                @Override
                @NonNull
                public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    FavViewHolder favViewHolder = super.onCreateViewHolder(parent, viewType);
                    favViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();


                        }

                    });
                    return favViewHolder;//super.onCreateViewHolder(parent, viewType);


                }


            };

            mRecycler.setAdapter(firebaseRecyclerAdapter);

        }

        return v;
    }



    public static class FavViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton favBtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        TextView txtPlace, txtPrice, txtLocation;
        ImageView imgKeja;

        public FavViewHolder(View itemView){

            super(itemView);
            mView = itemView;

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("nLikes");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

            favBtn = mView.findViewById(R.id.favBtn_retrieve);
            txtPlace = mView.findViewById(R.id.txtPlace_fav);
            txtLocation= mView.findViewById(R.id.txtLocation_fav);
            txtPrice = mView.findViewById(R.id.txtPrice_fav);
            imgKeja = mView.findViewById(R.id.imageFav_retrieve);



        }
        public void setPlace(String place){
            TextView places = mView.findViewById(R.id.txtPlace_fav);
            places.setText(place);

        }
        public void setFavBtn(final String house_id){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String housekey = dataSnapshot.getRef().getKey();
                    if (mAuth.getCurrentUser()!= null) {
                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(house_id)){


                            favBtn.setImageResource(R.drawable.ic_favourited_24dp);

                        } else {
                            favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        public void setLocation(String location){
            TextView locate = mView.findViewById(R.id.txtLocation_fav);
            locate.setText(location);

        }
        public void setPrice(String price){
            TextView set_price = mView.findViewById(R.id.txtPrice_fav);
            set_price.setText(price);


        }
        public void setImage(final Context ctx, final String image){
            final ImageView set_image = mView.findViewById(R.id.imageFav_retrieve);

            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(set_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(set_image);
                }
            });
        }

        void setFav(String fav){
            TextView set_fav = mView.findViewById(R.id.favBtn_retrieve);
            set_fav.setText(fav);
        }
        void setUid(String uid){
            TextView set_uid = mView.findViewById(R.id.txtuid);
            set_uid.setText(uid);
        }

    }
    public void getUserInfo(final ImageView image, final TextView username, final TextView price, final TextView place, String uid) {

        if (uid != null && mAuth.getCurrentUser().getUid() != null) {
            DatabaseReference rref = FirebaseDatabase.getInstance().getReference().child("nLikes").child(uid);
            Query qry = rref.orderByChild("usid").equalTo(mAuth.getCurrentUser().getUid());

                        FavModel profileUpload = new FavModel();

                        Picasso.with(getContext()).load(profileUpload.getImage()).into(image);
                        username.setText(profileUpload.getLocation());
                        price.setText(profileUpload.getPlace());
                        place.setText(profileUpload.getPrice());



            rref.keepSynced(true);
        }
    }



}


