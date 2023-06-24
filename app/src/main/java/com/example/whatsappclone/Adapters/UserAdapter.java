package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatDetailActivity;
import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

Context context;
List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        User user=userList.get(position);

        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.profile).into(holder.profile);
        holder.name.setText(user.getUserName());

        FirebaseDatabase.getInstance().getReference().child("Chats")
                        .child(FirebaseAuth.getInstance().getUid()+user.getUserId())
                                .orderByChild("timestamp")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        if (snapshot.hasChildren())
                                                        {
                                                            for (DataSnapshot dataSnapshot:snapshot.getChildren())
                                                            {
                                                                holder.lastMessage.setText(dataSnapshot.child("message").getValue(String.class));
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });



       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(context, ChatDetailActivity.class);

               intent.putExtra("name",user.getUserName());
               intent.putExtra("pic",user.getProfilepic());
               intent.putExtra("userId",user.getUserId());

               context.startActivity(intent);

           }
       });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,lastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             profile=itemView.findViewById(R.id.profileSetting);
             name=itemView.findViewById(R.id.name);
             lastMessage=itemView.findViewById(R.id.lastMessage);


        }
    }
}
