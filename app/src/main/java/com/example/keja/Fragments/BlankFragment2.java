package com.example.keja.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.keja.Adapters.SearchAdapter;
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


public class BlankFragment2 extends Fragment {
    protected RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<Search> mSearch;

    private EditText search_bar;

    private Toolbar toolbar1;

    public View v;

    public BlankFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       // toolbar1= v.findViewById(R.id.toolbar1);


        search_bar = v.findViewById(R.id.search_bar);

        mSearch = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(),mSearch);

        recyclerView.setAdapter(searchAdapter);


        readData();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchIT(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return v;
    }
    private void searchIT(final String s){
        Query qry = FirebaseDatabase.getInstance().getReference("Shabee").orderByChild("place")
                .startAt(s)
                .endAt(s+"\uf8ff");
        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSearch.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Search search = snapshot.getValue(Search.class);
                    mSearch.add(search);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void readData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shabee");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_bar.getText().toString().equals("")){
                    mSearch.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Search search = snapshot.getValue(Search.class);
                        mSearch.add(search);

                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
