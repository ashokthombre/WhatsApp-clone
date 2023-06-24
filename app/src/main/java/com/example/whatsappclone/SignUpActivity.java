package com.example.whatsappclone;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
ActivitySignUpBinding binding;

FirebaseAuth auth;

FirebaseDatabase database;

ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating");
        progressDialog.setMessage("creating user..");

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.signup.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String name=binding.userName.getText().toString();
                    String email=binding.email.getText().toString();
                    String password=binding.password.getText().toString();

                    if (!name.equals("") && !email.equals("") && !password.equals(""))
                    {
                        progressDialog.show();
                        auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information

                                            User user=new User(name,email,password);

                                            String id=task.getResult().getUser().getUid();

                                            database.getReference().child("Users").child(id).setValue(user);


                                            Toast.makeText(SignUpActivity.this, "Usre Created", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.d("message",task.getException().getMessage());
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        binding.alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });




        }




    }
