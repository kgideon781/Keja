package com.example.keja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.keja.Adapters.CommentsAdapter;
import com.example.keja.Adapters.CommentAdapter;
import com.example.keja.Model.Comments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {

    EditText editPost;
    TextView postComment;
    CircleImageView profileImg;
    private String house_key, name;
    TextView text_key;
    FirebaseAuth auth;
    DatabaseReference dbRef;
    RecyclerView recycler;
    private CommentAdapter commentAdapter;
    private List<CommentModel> commentList;

    DatabaseReference ref;
    DatabaseReference refer,reference;
    DatabaseReference imageRef;

    // firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setTitle("Comments");
        actionbar.setDefaultDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        refer = FirebaseDatabase.getInstance().getReference().child("Comments");
        reference = FirebaseDatabase.getInstance().getReference().child("Comments");



        profileImg = findViewById(R.id.commentsProfilePic);
        postComment = findViewById(R.id.postComment);
        editPost = findViewById(R.id.commentText);

        recycler = findViewById(R.id.commentsRecycler);
        recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearLayoutManager);



        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recycler.setAdapter(commentAdapter);


        auth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        house_key = i.getStringExtra("house_key");
        name = i.getStringExtra("username");


        dbRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(house_key);
        ref = FirebaseDatabase.getInstance().getReference().child("Comments").child(house_key);
        imageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());


        dbRef.keepSynced(true);
        refer.keepSynced(true);
        reference.keepSynced(true);
        ref.keepSynced(true);
        imageRef.keepSynced(true);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPost.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    addComment();

                }
            }
        });
        getImage();
        readComment();
    }

    private void addComment() {

        ref.keepSynced(true);

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", editPost.getText().toString());
        map.put("user", auth.getCurrentUser().getUid());

        ref.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CommentsActivity.this, "Commented", Toast.LENGTH_SHORT).show();
                    
                }else {
                    Toast.makeText(CommentsActivity.this, "Couldn't comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editPost.setText("");
        getImage();

        
    }
    private void getImage(){

        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileUpload img = dataSnapshot.getValue(ProfileUpload.class);
                Picasso.with(getApplicationContext()).load(img.getImage()).into(profileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageRef.keepSynced(true);
    }
    private void readComment(){

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CommentModel comments = snapshot.getValue(CommentModel.class);
                    commentList.add(comments);

                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbRef.keepSynced(true);
    }

}
