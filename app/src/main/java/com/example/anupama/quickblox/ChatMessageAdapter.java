package com.example.anupama.quickblox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {


    private ArrayList<QBChatMessage> qbChatMessages;
    private int SENDER = 0;
    private int RECEIVER = 1;


    public ChatMessageAdapter(ArrayList<QBChatMessage> qbChatMessages) {
        this.qbChatMessages = qbChatMessages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        Log.e("Rishabh","view type: "+i);

        switch (i){

            case 0: {
                Log.e("Rishabh","SENDER LAYOUT INFLATED");
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_message_sender,viewGroup,false);
                return new ChatMessageSentViewHolder(view);
            }

            case 1: {
                Log.e("Rishabh","RECEIVER LAYOUT INFLATED");
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_message_received,viewGroup,false);
                return new ChatMessageReceiveViewHolder(view);
            }

            default: {
                Log.e("Rishabh","DEFAULT");
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_message_received,viewGroup,false);
                return new ChatMessageSentViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int id = qbChatMessages.get(i).getSenderId() ;
        String message = qbChatMessages.get(i).getBody().toString();


        Log.e("Rishabh","on Bindview Holder: id: "+id);
        Log.e("Rishabh","on Bindview Holder: message: "+message);

        switch (id) {

            case 79735345: {
                ((ChatMessageSentViewHolder)viewHolder).sentBubbleTextView.setText(message);
                break;
            }

            case 80758063: {
                ((ChatMessageReceiveViewHolder)viewHolder).receiveBubbleTextView.setText(message);
                break;
            }

            default: break;
        }
    }


    @Override
    public int getItemViewType(int position) {


        int senderId = qbChatMessages.get(position).getSenderId();
        Log.e("Rishabh","get item view type : sender id: "+senderId);
        if(senderId == 79735345){
            return SENDER;
        }else {
            return RECEIVER;
        }

        //return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return qbChatMessages.size();
    }

    public class ChatMessageSentViewHolder extends RecyclerView.ViewHolder{

        BubbleTextView sentBubbleTextView;

        public ChatMessageSentViewHolder(@NonNull View itemView) {
            super(itemView);
            sentBubbleTextView = itemView.findViewById(R.id.message_send_content) ;
        }
    }


    public class ChatMessageReceiveViewHolder extends RecyclerView.ViewHolder{

        BubbleTextView receiveBubbleTextView ;


        public ChatMessageReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveBubbleTextView = itemView.findViewById(R.id.message_received_content) ;
        }
    }
}
