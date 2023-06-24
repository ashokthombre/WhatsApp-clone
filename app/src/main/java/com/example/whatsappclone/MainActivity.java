package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.Adapters.FragmentAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        auth=FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        binding.tab.setupWithViewPager(binding.viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.logout)
        {
            auth.signOut();
            Intent intent=new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }
        else if (item.getItemId()==R.id.setting)
        {
           Intent intent=new Intent(MainActivity.this,SettingActivity.class);
           startActivity(intent);

        } else {
            Intent intent=new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(intent);

        }
        return true;
    }


}
