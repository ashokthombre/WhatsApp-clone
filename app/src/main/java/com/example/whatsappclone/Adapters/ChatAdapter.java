package com.example.whatsappclone.Adapters;

import static java.text.DateFormat.getDateTimeInstance;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    List<MessageModel>messageModels;

    Context context;
    int SENDER_VIEW_TYPE=1;

    int RECEIVER_VIEW_TYPE=2;

    String recId;

    public ChatAdapter(List<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(List<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      if (viewType==SENDER_VIEW_TYPE)
      {

          View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
          return new SenderViewHolder(view);

      }
      else
      {
          View view= LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
          return new RecieverViewHolder(view);
      }

    }

    @Override
    public int getItemViewType(int position) {

        String id=FirebaseAuth.getInstance().getUid();

        if (messageModels.get(position).getId().equals(id))
        {

            return SENDER_VIEW_TYPE;

        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel=messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete.");
                builder.setMessage("Are you want to delete message ?");
                builder.setIcon(R.drawable.baseline_delete_24);
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase databas=FirebaseDatabase.getInstance();

                        String sender=FirebaseAuth.getInstance().getUid()+recId;

                        databas.getReference().child("Chats")
                                .child(sender).child(messageModel.getMessageId())
                                .setValue(null);
                    }
                });

                builder.create();
                builder.show();
                return true;
            }
        });

        if (holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMessage.setText(messageModel.getMessage());

            ((SenderViewHolder)holder).senderTime.setText(getTimeDate(messageModel.getTimestamp()));

        }
        else
        {
            ((RecieverViewHolder)holder).reciverMessage.setText(messageModel.getMessage());
            ((RecieverViewHolder)holder).recieverTime.setText(getTimeDate(messageModel.getTimestamp()));
        }


    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        TextView reciverMessage,recieverTime;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            reciverMessage=itemView.findViewById(R.id.recieverText);

            recieverTime=itemView.findViewById(R.id.recieverTime);


        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView senderMessage,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);

            senderTime=itemView.findViewById(R.id.senderTime);


        }
    }

    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }


}
