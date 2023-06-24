package com.example.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsappclone.Adapters.UserAdapter;
import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    FragmentChatBinding binding;

    List<User>users= new  ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater, container, false);


        loadData();



        return  binding.getRoot();
    }

    private void loadData() {
        database=FirebaseDatabase.getInstance();



        UserAdapter adapter=new UserAdapter(getContext(),users);
        binding.recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren() )
                {
                    User user=dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());

                    if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        users.add(user);
                    }
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
      loadData();
    }
}