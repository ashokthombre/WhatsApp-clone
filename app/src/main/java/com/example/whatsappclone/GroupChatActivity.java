package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.Adapters.ChatAdapter;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.backGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        FirebaseDatabase database=FirebaseDatabase.getInstance();

        List<MessageModel>list=new ArrayList<>();

        String senderId= FirebaseAuth.getInstance().getUid();

        binding.groupName.setText("Group Chat");

        ChatAdapter chatAdapter=new ChatAdapter(list,this);
        binding.recyclerViewGroupChat.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        binding.recyclerViewGroupChat.setLayoutManager(linearLayoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    MessageModel model1=dataSnapshot.getValue(MessageModel.class);
                    list.add(model1);
                }

                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.sendGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=binding.etMessageGroupChat.getText().toString();

                MessageModel model=new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());

                database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
          @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
            });

                binding.etMessageGroupChat.setText("");

                database.getReference().child("Group Chat")
                        .push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });





            }
        });

    }
}