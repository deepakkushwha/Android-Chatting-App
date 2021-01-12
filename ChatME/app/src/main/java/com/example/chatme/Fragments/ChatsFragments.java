package com.example.chatme.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatme.Adapter.UsersAdapter;
import com.example.chatme.Models.Users;
import com.example.chatme.databinding.FragmentChatsFragmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragments extends Fragment {

    FragmentChatsFragmentsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;

    public ChatsFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseDatabase = FirebaseDatabase.getInstance();
        binding = FragmentChatsFragmentsBinding.inflate(inflater, container, false);
        UsersAdapter adapter = new UsersAdapter(list,getContext());
        binding.chatrecyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatrecyclerview.setLayoutManager(layoutManager);
        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           list.clear();
           for (DataSnapshot dataSnapshot : snapshot.getChildren())
           {
               Users users = dataSnapshot.getValue(Users.class);
               users.setUserId(dataSnapshot.getKey());
               if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
               list.add(users);}
           }
           adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}