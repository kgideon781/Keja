package com.example.keja.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keja.CommentModel;
import com.example.keja.Fragments.BlankFragment;
import com.example.keja.ProfileUpload;
import com.example.keja.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentModel> mComment;
    FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<CommentModel> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);


        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final CommentModel commentModel = mComment.get(position);
        holder.comment.setText(commentModel.getMessage());
        getUserInfo(holder.image_profile, holder.username, commentModel.getUser());

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, BlankFragment.class);
                i.putExtra("user", commentModel.getUser());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView image_profile;
        public TextView username, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.comment_pic);
            comment = itemView.findViewById(R.id.comment_message);
            username = itemView.findViewById(R.id.username_comment);



        }
    }
    public void getUserInfo(final ImageView image, final TextView username, String uid) {
        if (uid != null) {
            DatabaseReference rref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            rref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ProfileUpload profileUpload = dataSnapshot.getValue(ProfileUpload.class);
                    Picasso.with(mContext).load(profileUpload.getImage()).into(image);
                    username.setText(profileUpload.getName());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            rref.keepSynced(true);
        }
    }
}
