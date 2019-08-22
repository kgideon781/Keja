package com.example.keja;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class HouseDetailsActivity extends AppCompatActivity {

    TextView txtPrices, txtLocations, txtPlaces;
    ImageView imageHouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        //Actionbar
        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("House Details");
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        txtLocations = findViewById(R.id.txtLocations);
        txtPlaces = findViewById(R.id.txtPlaces);
        txtPrices = findViewById(R.id.txtPrices);
        imageHouse = findViewById(R.id.imageHouse);
        //
        byte[] bytes = getIntent().getByteArrayExtra("image");
        String place = getIntent().getStringExtra("place");
        String location = getIntent().getStringExtra("location");
        String price = getIntent().getStringExtra("price");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

        //
        txtPrices.setText(price);
        txtLocations.setText(location);
        txtPlaces.setText(place);
        imageHouse.setImageBitmap(bitmap);


        imageHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(HouseDetailsActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        txtLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HouseDetailsActivity.this,"This is your favourite",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
