package com.example.keja.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keja.Fragments.BlankFragment;
import com.example.keja.Model.Search;
import com.example.keja.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context ctx;
    private List<Search> msearch;
    private FirebaseUser mFirebaseUser;

    public SearchAdapter(Context ctx, List<Search> msearch) {
        this.ctx = ctx;
        this.msearch = msearch;
        this.mFirebaseUser = mFirebaseUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.keja_item, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Search search = msearch.get(position);
        holder.fav.setVisibility(View.INVISIBLE);
        holder.mtextPrice.setText(search.getPrice());
        holder.mtextLocation.setText(search.getLocation());
        holder.mtextPlace.setText(search.getPlace());
        Picasso.with(ctx).load(search.getImage()).into(holder.mimageKeja);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = ctx.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("house_id", search.getHouseid());
                editor.apply();

                ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new BlankFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return msearch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mimageKeja,fav;
        public TextView mtextPrice, mtextLocation, mtextPlace;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mimageKeja = itemView.findViewById(R.id.imageKeja);
            fav = itemView.findViewById(R.id.favBtn);
            mtextPrice = itemView.findViewById(R.id.txtPrice);
            mtextLocation = itemView.findViewById(R.id.txtLocation);
            mtextPlace = itemView.findViewById(R.id.txtPlace);
        }
    }
}
