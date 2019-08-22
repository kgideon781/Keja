package com.example.keja.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keja.CommentsActivity;
import com.example.keja.FavouriteActivity;
import com.example.keja.HouseDetailsActivity;
import com.example.keja.KejaHelper;
import com.example.keja.LoginActivity;
import com.example.keja.MainActivity;
import com.example.keja.ProfileUpload;
import com.example.keja.R;
import com.example.keja.SetupActivity;
import com.example.keja.house_id;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.isDebugEnabled;


public class BlankFragment extends Fragment  {

    private RecyclerView mRecycler;
    View view;
    private DatabaseReference databaseReference;
    private DatabaseReference mdatabaseUsers;
    DatabaseReference mDatabaseLikes;
    DatabaseReference dbRetrieve;
    TextView textView;
    CircleImageView circleImageView;
    BottomNavigationView bottom;
    DatabaseReference refined;

    private String house_k;

    private DrawerLayout mDrawer;

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    DatabaseReference ref;
    private boolean mProcessLike = false;

    Fragment selectedFrag = null;

    private FirebaseAuth mAuth;
    FirebaseAuth auth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    public BlankFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank, container, false);
       auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
       /* mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                }
            }
        };*/


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Shabee");


        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        dbRetrieve = FirebaseDatabase.getInstance().getReference().child("nLikes");





        //Offline capabilities

        mdatabaseUsers.keepSynced(true);
        databaseReference.keepSynced(true);
        mDatabaseLikes.keepSynced(true);
        dbRetrieve.keepSynced(true);

        circleImageView = view.findViewById(R.id.imageView);
        //textView = view.findViewById(R.id.textView);
        bottom = view.findViewById(R.id.bottom_navigation);

        mRecycler = view.findViewById(R.id.myRecycler);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        //Drawer Navigation
        mToolbar = view.findViewById(R.id.tool_bar);




        //String user =  getActivity().getIntent().getExtras().getString("user");


        //loadProfile();
        fireAdapter();
        //chekUserExists();

        return view;

            }

    private void fireAdapter() {
        //mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<KejaHelper, KejaViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter <KejaHelper, KejaViewHolder>(
                KejaHelper.class,
                R.layout.keja_item,
                KejaViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final KejaViewHolder viewHolder, final KejaHelper model, final int position) {
                final String house_id = getRef(position).getKey();

                databaseReference.child(house_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            KejaHelper kj = dataSnapshot.getValue(KejaHelper.class);
                            viewHolder.setPrice(kj.getPrice());
                            viewHolder.setPlace(kj.getPlace());
                            viewHolder.setLocation(kj.getLocation());
                            viewHolder.setUid(kj.getImage().toString());
                            viewHolder.setImage(getActivity(), kj.getImage());
                            viewHolder.setHouseid(kj.getHouseid());

                            viewHolder.setFavBtn(house_id);

                            countLikes(viewHolder.text, house_id);
                            getComments(house_id,viewHolder.txtComments);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //final String house_id = getRef(position).getKey();
               /* viewHolder.setPrice(model.getPrice());
                viewHolder.setPlace(model.getPlace());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setUid(model.getImage().toString());
                viewHolder.setImage(getActivity(),model.getImage());
                viewHolder.setHouseid(model.getHouseid());


                viewHolder.setFavBtn(house_id);

               countLikes(viewHolder.text, house_id);
*/

                viewHolder.favBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessLike = true;

                        mDatabaseLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (mProcessLike) {
                                    if (mAuth.getCurrentUser() != null) {
                                        String user = mAuth.getCurrentUser().getUid();
                                        if (dataSnapshot.child(house_id).hasChild(user)) {
                                            mDatabaseLikes.child(house_id).child(user).removeValue();
                                            dbRetrieve.child(user).removeValue();
                                            mProcessLike = false;


                                        } else {


                                            KejaHelper keja = new KejaHelper(getActivity());
                                            TextView loc = view.findViewById(R.id.txtLocation);
                                            TextView place = view.findViewById(R.id.txtPlace);
                                            TextView price = view.findViewById(R.id.txtPrice);
                                            TextView img = view.findViewById(R.id.txtKey);
                                            //    String imag = keja.getImage().toString();


                                            dataSnapshot.getValue();


                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("name", loc.getText().toString());
                                            map.put("place", place.getText().toString());
                                            map.put("price",price.getText().toString());
                                            map.put("image",img.getText().toString());
                                            map.put("fav", "love");
                                            map.put("usid", user);
                                            map.put("house_id", house_id);




                                            mDatabaseLikes.child(house_id).child(user).setValue(map);
                                            dbRetrieve.child(user).updateChildren(map);
                                            // Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
                                            mProcessLike = false;

                                            Intent intent = new Intent(getContext(), FavouriteActivity.class);

                                            intent.putExtra("location", house_id);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
        /*                dbRetrieve.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (mProcessLike) {
                                    if (mAuth.getCurrentUser() != null) {
                                        String user = mAuth.getCurrentUser().getUid();
                                        if (dataSnapshot.child(user).hasChild(house_id) ) {
                                            dbRetrieve.child(user).child(house_id).removeValue();
                                            mProcessLike = false;


                                        } else {


                                            KejaHelper keja = new KejaHelper(getActivity());
                                            TextView loc = view.findViewById(R.id.txtLocation);
                                            TextView place = view.findViewById(R.id.txtPlace);
                                            TextView price = view.findViewById(R.id.txtPrice);
                                            TextView img = view.findViewById(R.id.txtKey);
                                            //    String imag = keja.getImage().toString();


                                            dataSnapshot.getValue();


                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("name", loc.getText().toString());
                                            map.put("place", place.getText().toString());
                                            map.put("price",price.getText().toString());
                                            map.put("image",img.getText().toString());
                                            map.put("fav", "love");
                                            map.put("usid", user);
                                            map.put("house_id", house_id);




                                            //mDatabaseLikes.child(house_id).child(user).setValue(map);
                                            dbRetrieve.child(user).child(house_id).updateChildren(map);
                                            // Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
                                            mProcessLike = false;

                                            Intent intent = new Intent(getContext(), FavouriteActivity.class);

                                            intent.putExtra("location", house_id);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/

                    }
                });
                viewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent i = new Intent(getContext(), CommentsActivity.class);
                        i.putExtra("house_key", house_id);

                        mDatabaseLikes.child(mAuth.getCurrentUser().getUid()).child(house_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    ProfileUpload profileUpload = dataSnapshot.getValue(ProfileUpload.class);
                                    i.putExtra("username", profileUpload.getName());
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        startActivity(i);
                    }

                });
                viewHolder.txtComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent i = new Intent(getContext(), CommentsActivity.class);
                        i.putExtra("house_key", house_id);

                        mDatabaseLikes.child(mAuth.getCurrentUser().getUid()).child(house_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    ProfileUpload profileUpload = dataSnapshot.getValue(ProfileUpload.class);
                                    i.putExtra("username", profileUpload.getName());
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        startActivity(i);
                    }

                });



            }


            @Override
            public KejaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                KejaViewHolder kejaViewHolder = super.onCreateViewHolder(parent, viewType);
               kejaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            TextView txtpl = v.findViewById(R.id.txtPlace);
                            txtpl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Snackbar.make(v, "This is your favourite", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                            TextView txtl = v.findViewById(R.id.txtLocation);
                            TextView txtpr = v.findViewById(R.id.txtPrice);
                            ImageView img = v.findViewById(R.id.imageKeja);
                            //

                            String plaice = txtpl.getText().toString();
                            String locaite = txtl.getText().toString();
                            String praice = txtpr.getText().toString();
                            Drawable mdraw = img.getDrawable();
                            Bitmap bmp = ((BitmapDrawable) mdraw).getBitmap();
                            //

                            Intent intent = new Intent(v.getContext(), HouseDetailsActivity.class);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] bytes = stream.toByteArray();
                            intent.putExtra("location", locaite);
                            intent.putExtra("place", plaice);
                            intent.putExtra("price", praice);
                            intent.putExtra("image", bytes);
                            startActivity(intent);
                        }
                        catch (Exception e){
                            Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }




                    }

                });
                return kejaViewHolder;//super.onCreateViewHolder(parent, viewType);


            }


        };

        mRecycler.setAdapter(firebaseRecyclerAdapter);


    }
    private void getComments(String house_id, final TextView tv){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comments").child(house_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 1){
                    tv.setText("No Comments on this post");
                    tv.setEnabled(false);
                    tv.setClickable(false);
                }
                else if (dataSnapshot.getChildrenCount() == 1)
                {
                    tv.setText("View "+dataSnapshot.getChildrenCount()+" comment");
                    tv.setEnabled(true);
                    tv.setClickable(true);

                }else if (dataSnapshot.getChildrenCount() > 1){

                    tv.setText("View all  " + dataSnapshot.getChildrenCount() + " comments");
                    tv.setEnabled(true);
                    tv.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void countLikes(final TextView likes,String house_id){
      //  FirebaseUser user = F;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(house_id);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String like = " Likes";
                    likes.setText(dataSnapshot.getChildrenCount() + like);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

 /*   private void loadProfile() {
        if (mAuth.getCurrentUser().getUid() != null) {
            String user = mAuth.getCurrentUser().getUid();
            mDatabaseLikes.child(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String img = (String) dataSnapshot.child("image").getValue();
                    String username = (String) dataSnapshot.child("name").getValue();
                    final CircleImageView image_prof = view.findViewById(R.id.imageView);
                    TextView imgur = view.findViewById(R.id.textView);
                    Picasso.with(getContext()).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image_prof, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext()).load(img).into(image_prof);
                        }
                    });
                    imgur.setText(username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



}*/

    public static class KejaViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageButton favBtn, commentBtn;
        DatabaseReference mDatabaseLike, mDatabasenLikes;
        FirebaseAuth mAuth;
        TextView text,txtComments;

        public KejaViewHolder(View itemView){

            super(itemView);
            mView = itemView;

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabasenLikes = FirebaseDatabase.getInstance().getReference().child("nLikes");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

            favBtn = mView.findViewById(R.id.favBtn);
            text = mView.findViewById(R.id.likes);
            commentBtn = mView.findViewById(R.id.commentBtn);

            txtComments = mView.findViewById(R.id.txtComments);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mclickListener.onItemClick(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mclickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }
        public void setPlace(String place){
            TextView places = mView.findViewById(R.id.txtPlace);
            places.setText(place);

        }
        public void setFavBtn(final String house_id){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (mAuth.getCurrentUser()!= null) {

                        if (dataSnapshot.child(house_id).hasChild(mAuth.getCurrentUser().getUid())) {
                            int counts = (int)dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildrenCount();
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
        public void setFavBtns(final String house_is){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (mAuth.getCurrentUser()!= null) {

                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(house_is)) {
                            int counts = (int)dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildrenCount();
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
            TextView locate = mView.findViewById(R.id.txtLocation);
            locate.setText(location);

        }
        public void setPrice(String price){
            TextView set_price = mView.findViewById(R.id.txtPrice);
            set_price.setText(price);


        }
        public void setUid(String uid){
            TextView set_uid = mView.findViewById(R.id.txtKey);
            set_uid.setText(uid);


        }
        public void setHouseid(String houseid){
            TextView txt = mView.findViewById(R.id.txth_id);
            txt.setText(houseid);
        }
        public void setImage(final Context ctx,final String image){
            final ImageView set_image = mView.findViewById(R.id.imageKeja);

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
            TextView set_fav = mView.findViewById(R.id.favBtn);
            set_fav.setText(fav);
        }

    }
    private static ClickListener mclickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);


    }
    public void setOnClickListener(ClickListener clickListener){
        mclickListener = clickListener;
    }
    private void chekUserExists() {
        if(mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();

            // final String user_id = mAuth.getCurrentUser().getUid();
            mdatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_id)) {
                        Intent setupIntent = new Intent(getContext(), SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
