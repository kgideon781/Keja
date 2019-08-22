package com.example.keja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.keja.Fragments.BlankFragment;
import com.example.keja.Fragments.BlankFragment2;
import com.example.keja.Fragments.FavouritesFragment;
import com.example.keja.Fragments.ProfileFragment;
///import com.example.keja.Fragments.ProfileFragment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecycler;

    private DatabaseReference databaseReference;
    private DatabaseReference mdatabaseUsers;
    DatabaseReference mDatabaseLikes;
    TextView txtView;
    ImageButton btnEdit;
    CircleImageView circleImageView;
    BottomNavigationView bottom;

    DatabaseReference ref;
    private boolean mProcessLike = false;

    Toolbar toolbar;

    Fragment selectedFrag = null;

    private FirebaseAuth mAuth;
    FirebaseAuth auth;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        try {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {

                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);

                    }
                }
            };

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Shabee");
        mdatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");


        //Offline capabilities

        mdatabaseUsers.keepSynced(true);
        databaseReference.keepSynced(true);
        mDatabaseLikes.keepSynced(true);

       // FloatingActionButton fab = findViewById(R.id.fab);

        circleImageView = findViewById(R.id.imageView);
        txtView = findViewById(R.id.usernameField);
        btnEdit = findViewById(R.id.btnEditProfile);

        bottom = findViewById(R.id.bottom_navigation);


   /*     mRecycler = findViewById(R.id.myRecycler);
        mRecycler.setHasFixedSize(true);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
*/
       //Drawer Navigation
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String user = intent.getString("user");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("userid", user);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new ProfileFragment()).commit();


        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new BlankFragment()).commit();

        }


        //chekUserExists();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.ic_home:
                            toolbar.setVisibility(View.VISIBLE);
                            selectedFrag = new BlankFragment();
                            break;
                        case R.id.ic_fav:
                            toolbar.setVisibility(View.VISIBLE);
                            selectedFrag = new FavouritesFragment();

                            break;
                        case R.id.ic_profile:
                            toolbar.setVisibility(View.VISIBLE);
                            selectedFrag = new ProfileFragment();
                            break;
                        case R.id.ic_search:
                            toolbar.setVisibility(View.GONE);
                            selectedFrag = new BlankFragment2();

                            break;

                    }
                    if (selectedFrag != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, selectedFrag).commit();
                    }
                    return true;
                }

            };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mitem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = mitem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(i);
           // return true;
        }
        if (id == R.id.action_logout){
            logout();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(mitem);
    }

    private void logout() {
        mAuth.signOut();

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, SaverActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

            Toast.makeText(this, "To open maps", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(this,FavouriteActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "To Display all favourites", Toast.LENGTH_SHORT).show();

        }  else if (id == R.id.nav_send) {

            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   @Override
    protected void onStart() {
        super.onStart();



        loadProfile();

       bottom.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
       getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new BlankFragment()).commit();

      /*  mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<KejaHelper, KejaViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter <KejaHelper, KejaViewHolder>(KejaHelper.class,R.layout.keja_item,KejaViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(final KejaViewHolder viewHolder, final KejaHelper model, final int position) {
                final String house_id = getRef(position).getKey();

                //final String house_id = getRef(position).getKey();
                viewHolder.setPrice(model.getPrice());
                viewHolder.setPlace(model.getPlace());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setUid(model.getImage().toString());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setHouseid(model.getHouseid());


                viewHolder.setFavBtn(house_id);



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
                                            if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(house_id) ) {
                                                mDatabaseLikes.child(user).child(house_id).removeValue();
                                                mProcessLike = false;

                                            } else {


                                                KejaHelper keja = new KejaHelper(getApplicationContext());
                                                TextView loc = findViewById(R.id.txtLocation);
                                                TextView place = findViewById(R.id.txtPlace);
                                                TextView price = findViewById(R.id.txtPrice);
                                                TextView img = findViewById(R.id.txtKey);
                                            //    String imag = keja.getImage().toString();


                                                dataSnapshot.getValue();


                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("name", loc.getText().toString());
                                                map.put("place", place.getText().toString());
                                                map.put("price",price.getText().toString());
                                                map.put("image",img.getText().toString());
                                                map.put("fav", "love");
                                                //map.put("house_id", house_id);




                                                mDatabaseLikes.child(user).child(house_id).updateChildren(map);
                                               // Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
                                                mProcessLike = false;

                                                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);

                                                intent.putExtra("location", house_id);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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
                            Toast.makeText(MainActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }




                    }

                });
                return kejaViewHolder;//super.onCreateViewHolder(parent, viewType);


            }


        };

        mRecycler.setAdapter(firebaseRecyclerAdapter);



    }

    private void loadProfile() {
        if (mAuth.getCurrentUser().getUid() != null) {
            String user = mAuth.getCurrentUser().getUid();
            mDatabaseLikes.child(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String img = (String) dataSnapshot.child("image").getValue();
                    String username = (String) dataSnapshot.child("name").getValue();
                    final CircleImageView image_prof = findViewById(R.id.imageView);
                    TextView imgur = findViewById(R.id.textView);
                    Picasso.with(MainActivity.this).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image_prof, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(MainActivity.this).load(img).into(image_prof);
                        }
                    });
                    imgur.setText(username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public static class KejaViewHolder extends RecyclerView.ViewHolder{
        View mView;
            public ImageButton favBtn;
            DatabaseReference mDatabaseLike;
            FirebaseAuth mAuth;

            public KejaViewHolder(View itemView){

            super(itemView);
            mView = itemView;

            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

            favBtn = mView.findViewById(R.id.favBtn);


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

                            if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(house_id)) {
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
    private static MainActivity.ClickListener mclickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);


    }
    public void setOnClickListener(MainActivity.ClickListener clickListener){
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
                       Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                       setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(setupIntent);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }*/
    }
    private void loadProfile() {
        if (mAuth.getCurrentUser() != null) {
            String user = mAuth.getCurrentUser().getUid();


            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user);
            dbRef.keepSynced(true);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final String uern = dataSnapshot.child("name").getValue().toString();
                        final TextView textv = findViewById(R.id.usernameField);
                        textv.setText(uern);
                        final ImageButton btnEdits = findViewById(R.id.btnEditProfile);
                        btnEdits.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CircleImageView img = findViewById(R.id.imageView);

                                Drawable mdraw = img.getDrawable();
                                Bitmap bmp = ((BitmapDrawable) mdraw).getBitmap();

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] bytes = stream.toByteArray();

                                final TextView textv = findViewById(R.id.usernameField);
                                Intent i = new Intent(MainActivity.this, SetupActivity.class);
                                i.putExtra("User_name", textv.getText().toString());
                                i.putExtra("image", bytes);
                                startActivity(i);
                            }
                        });
                        final String image = dataSnapshot.child("image").getValue().toString();
                        final CircleImageView set_image = findViewById(R.id.imageView);
                        Picasso.with(MainActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(set_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(MainActivity.this).load(image).into(set_image);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
