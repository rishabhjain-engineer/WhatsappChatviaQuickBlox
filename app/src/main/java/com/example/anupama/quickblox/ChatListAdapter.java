package com.example.anupama.quickblox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {


    private ArrayList<QBUser> qbUserArrayList;
    private ArrayList<QBUser> qbCheckedUserArrayList = new ArrayList<>();

    public ChatListAdapter(ArrayList<QBUser> qbUserArrayList) {
        this.qbUserArrayList = qbUserArrayList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatlist_single_row,viewGroup,false);
        ChatListViewHolder holder = new ChatListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder chatListViewHolder, final int i) {
        chatListViewHolder.contactName.setText(qbUserArrayList.get(i).getFullName());

        chatListViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    // Add this user to checkList.
                    qbCheckedUserArrayList.add(qbUserArrayList.get(i));
                }else {
                    // Remove this user from checkList
                    qbCheckedUserArrayList.remove(qbUserArrayList.get(i));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return qbUserArrayList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView contactName ;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.contact_checkbox);
            contactName = itemView.findViewById(R.id.contact_name);

        }
    }

    public ArrayList<QBUser> getCheckeduserList(){
        return qbCheckedUserArrayList;
    }
}
