package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Updating..");
        progressDialog.setMessage("updating information..");


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user=snapshot.getValue(User.class);
               if (user!=null)
               {
                   Log.d("User",user.getProfilepic());
                   Picasso.get().load(user.getProfilepic())
                           .placeholder(R.drawable.profile)
                           .into(binding.profileSetting);
                   binding.etname.setText(user.getUserName());

                   binding.etstatus.setText(user.getAbout());
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // */*
                startActivityForResult(intent,33);
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           progressDialog.show();
                 String status=binding.etstatus.getText().toString();

                 String username=binding.etname.getText().toString();

                Map<String,Object> obj=new HashMap<>();
                obj.put("About",status);
                obj.put("userName",username);
                database.getReference().child("Users").child(auth.getUid())
                        .updateChildren(obj);
                progressDialog.dismiss();
                Toast.makeText(SettingActivity.this, "Updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (resultCode !=RESULT_CANCELED)
       {
           if (requestCode==33)
           {
               if (data.getData()!=null)
               {
                   Uri uri=data.getData();
                   binding.profileSetting.setImageURI(uri);

                   final StorageReference reference= storage.getReference().child("profile picture")
                           .child(FirebaseAuth.getInstance().getUid());
                   progressDialog.show();

                   reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           Toast.makeText(SettingActivity.this, "Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();

                           progressDialog.dismiss();
                           reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {

                                   database.getReference().child("Users").child(auth.getUid()).child("profilepic").setValue(uri.toString());
                               }
                           });
                       }

                   });
               }

           }
       }

    }
}