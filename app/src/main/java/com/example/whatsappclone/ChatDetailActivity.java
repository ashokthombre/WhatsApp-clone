package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.whatsappclone.Adapters.ChatAdapter;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;

    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        String senderId=auth.getUid();




      final String recievedId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("name");
        String profilePic=getIntent().getStringExtra("pic");


        binding.groupName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.profile).into(binding.profileSetting);


        binding.backGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =new Intent(ChatDetailActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

     List<MessageModel> messageModels=new ArrayList<>();

         final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this,recievedId);

         binding.recyclerViewGroupChat.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        binding.recyclerViewGroupChat.setLayoutManager(linearLayoutManager);

        final String senderRoom=senderId+recievedId;
        final  String receiverRoom=recievedId+senderId;

           database.getReference().child("Chats")
                   .child(senderRoom)
                           .addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {

                                   messageModels.clear();

                                   for (DataSnapshot dataSnapshot: snapshot.getChildren())
                                   {
                                       MessageModel model=dataSnapshot.getValue(MessageModel.class);

                                       model.setMessageId(dataSnapshot.getKey());
                                       messageModels.add(model);

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

                if (message.isEmpty())
                {
                    binding.etMessageGroupChat.setError("Enter Message");
                    return;
                }
                final MessageModel model=new MessageModel(senderId,message);
               model.setTimestamp(new Date().getTime());
               binding.etMessageGroupChat.setText("");

                database.getReference().child("Chats")
                        .child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                database.getReference().child("Chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                            }
                        });


            }
        });



    }
}