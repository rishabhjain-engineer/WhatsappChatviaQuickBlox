package com.example.anupama.quickblox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {


    private ArrayList<QBChatMessage> qbChatMessages;
    private int senderId, recipientId;

    public ChatMessageAdapter(ArrayList<QBChatMessage> qbChatMessages, int senderId, int recipientId) {
        this.qbChatMessages = qbChatMessages;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        switch (i) {

            case 0: {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_message_sender, viewGroup, false);
                return new ChatMessageSentViewHolder(view);
            }

            case 1: {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_message_received, viewGroup, false);
                return new ChatMessageReceiveViewHolder(view);
            }

            default: {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_chat_message_received, viewGroup, false);
                return new ChatMessageSentViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int id = qbChatMessages.get(i).getSenderId();
        String message = qbChatMessages.get(i).getBody();

        if (id == senderId) {
            ((ChatMessageSentViewHolder) viewHolder).sentBubbleTextView.setText(message);
        } else if (id == recipientId) {
            ((ChatMessageReceiveViewHolder) viewHolder).receiveBubbleTextView.setText(message);
        }

    }


    @Override
    public int getItemViewType(int position) {


        int id = qbChatMessages.get(position).getSenderId();
        if (senderId == id) {
            return 0;
        } else if (recipientId == id) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int getItemCount() {
        return qbChatMessages.size();
    }

    public class ChatMessageSentViewHolder extends RecyclerView.ViewHolder {

        BubbleTextView sentBubbleTextView;

        public ChatMessageSentViewHolder(@NonNull View itemView) {
            super(itemView);
            sentBubbleTextView = itemView.findViewById(R.id.message_send_content);
        }
    }


    public class ChatMessageReceiveViewHolder extends RecyclerView.ViewHolder {

        BubbleTextView receiveBubbleTextView;

        public ChatMessageReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveBubbleTextView = itemView.findViewById(R.id.message_received_content);
        }
    }
}
