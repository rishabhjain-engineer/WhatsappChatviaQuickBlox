package com.example.anupama.quickblox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogsAdapter extends RecyclerView.Adapter<ChatDialogsAdapter.ChatDialogsViewHolder> {

    private ArrayList<QBChatDialog> qbChatDialogs;
    private RowClicked listener ;

    public ChatDialogsAdapter(ArrayList<QBChatDialog> qbChatDialogs,RowClicked listener) {
        this.qbChatDialogs = qbChatDialogs;
        this.listener = listener ;
    }

    @NonNull
    @Override
    public ChatDialogsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatdialogs_single_row,viewGroup,false);
        ChatDialogsViewHolder holder = new ChatDialogsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDialogsViewHolder chatListViewHolder, final int i) {
        chatListViewHolder.chatNameTv.setText(qbChatDialogs.get(i).getName());

        chatListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTouch(qbChatDialogs.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return qbChatDialogs.size();
    }

    public class ChatDialogsViewHolder extends RecyclerView.ViewHolder{

        TextView chatNameTv ;
        ImageView chatprofileIv ;

        public ChatDialogsViewHolder(@NonNull View itemView) {
            super(itemView);
            chatNameTv = itemView.findViewById(R.id.chat_name);
            chatprofileIv = itemView.findViewById(R.id.chat_profile);
        }
    }

    public interface RowClicked{
        void onTouch(@NonNull QBChatDialog qbChatDialog);
    }
}
