package com.example.keja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keja.Model.FavModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView mRecycler;

    private DatabaseReference databaseReference,ref, dbCurrentUser;


    private DatabaseReference mdatabaseUsers;

    private Query mQuery;
    String house_key;

    private FirebaseUser currentUser;
    FirebaseAuth mAuth;

    DatabaseReference dbRef;


//    String house = getIntent().getStringExtra("house_id");
    private FirebaseAuth.AuthStateListener mAuthListener;


    FirebaseAuth.AuthStateListener auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        String f = getIntent().getStringExtra("house_id");

        //final String current_user = mAuth.getCurrentUser().getUid();
        mAuth = FirebaseAuth.getInstance();

        final String currentUser = mAuth.getCurrentUser().getUid();

        dbCurrentUser = FirebaseDatabase.getInstance().getReference().child("nLikes").child(currentUser);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("nLikes");
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Shabee");
        mQuery = dbCurrentUser.orderByChild("usid").equalTo(currentUser);


       // mQuery = dbCurrentUser.orderByKey().equalTo(current_user);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(FavouriteActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                }
            }
        };





        mRecycler = findViewById(R.id.recyleFav);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        if (currentUser != null) {

            FirebaseRecyclerAdapter<FavModel, FavouriteActivity.FavViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FavModel, FavouriteActivity.FavViewHolder>(
                    FavModel.class,
                    R.layout.favourite_item,
                    FavouriteActivity.FavViewHolder.class,
                    dbCurrentUser
            ) {
                @NonNull
                @Override
                protected void populateViewHolder(final FavouriteActivity.FavViewHolder viewHolder, final FavModel fv, int position) {
                    String user_id = getRef(position).getKey();


                    dbCurrentUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FavModel model = dataSnapshot.getValue(FavModel.class);
                            viewHolder.setPlace(model.getPrice());
                            viewHolder.setPrice(model.getPlace());
                            viewHolder.setLocation(model.getLocation());
                            viewHolder.setImage(getApplicationContext(), model.getImage());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                        //viewHolder.setUid(helper.getHouse_id());





                }


                @Override
                @NonNull
                public FavouriteActivity.FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    FavouriteActivity.FavViewHolder favViewHolder = super.onCreateViewHolder(parent, viewType);
                    favViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Toast.makeText(FavouriteActivity.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();


                        }

                    });
                    return favViewHolder;//super.onCreateViewHolder(parent, viewType);


                }


            };

            mRecycler.setAdapter(firebaseRecyclerAdapter);


        }
    }




    @Override
    protected void onStart() {
        super.onStart();



        mAuth.addAuthStateListener(mAuthListener);

        //Toast.makeText(this, mQuery.getRef().toString()+ " "+mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
        final String user_id = mAuth.getCurrentUser().getUid();


    }
    public static class FavViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton favBtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public FavViewHolder(View itemView){

            super(itemView);
            mView = itemView;
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("nLikes").child(mAuth.getCurrentUser().getUid());
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

            favBtn = mView.findViewById(R.id.favBtn_retrieve);



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
                        if (dataSnapshot.child((mAuth.getCurrentUser().getUid())).hasChild(house_id)){


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
        public void setFavBtns(final String house_id){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String housekey = dataSnapshot.getRef().getKey();
                    if (mAuth.getCurrentUser()!= null) {
                        if (dataSnapshot.child((mAuth.getCurrentUser().getUid())).hasChild(house_id)){


                            //favBtn.setImageResource(R.drawable.ic_favourited_24dp);

                        } else {
                           // favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);

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
    }
