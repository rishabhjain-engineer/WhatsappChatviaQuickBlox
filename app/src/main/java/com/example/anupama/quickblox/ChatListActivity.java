package com.example.anupama.quickblox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView mContactRv ;
    private Button mCreateChatDialogBtn ;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatListAdapter mChatListAdapter ;
    private ChatListViewModel mChatListViewModel;
    private ProgressBar mProgressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initComponent();

        mChatListViewModel.retrieveAllUser();
        mChatListViewModel.getQbUserListLiveData().observe(this, new Observer<ArrayList<QBUser>>() {
            @Override
            public void onChanged(@Nullable ArrayList<QBUser> qbUsers) {
                if(mChatListAdapter == null){
                    mChatListAdapter = new ChatListAdapter(qbUsers);
                    mContactRv.setAdapter(mChatListAdapter);
                }else {
                    mChatListAdapter.notifyDataSetChanged();
                }
            }
        });

        mChatListViewModel.getAsyncResponseMutableLiveData().observe(this, new Observer<AsyncResponse>() {
            @Override
            public void onChanged(@Nullable AsyncResponse asyncResponse) {
                consumeResponse(asyncResponse);
            }
        });


    }

    private void initComponent(){


        mChatListViewModel = ViewModelProviders.of(this).get(ChatListViewModel.class);
        mContactRv = findViewById(R.id.contact_list);
        mProgressBar = findViewById(R.id.contact_pb);
        mCreateChatDialogBtn = findViewById(R.id.create_dialog_btn);

        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mContactRv.setLayoutManager(mLinearLayoutManager);
        mContactRv.setHasFixedSize(true);




        mCreateChatDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Checked Userlist, to create chat dialog.
                int value = mChatListAdapter.getCheckeduserList().size() ;
                if(value==0){
                    Toast.makeText(ChatListActivity.this,"Select atleast 1 user, to create chat dialog.",Toast.LENGTH_SHORT).show();
                }else if(value==1){
                    mChatListViewModel.createPrivateChat(mChatListAdapter.getCheckeduserList().get(0));
                }else {
                    mChatListViewModel.createGroupChat();
                }
            }
        });
    }


    private void consumeResponse(AsyncResponse asyncResponse) {

        switch (asyncResponse.responseStatus){

            case LOADING:{
                break;
            }
            case SUCCESS:{
                mProgressBar.setVisibility(View.INVISIBLE);
                if(asyncResponse.data.getAsBoolean()){
                    Toast.makeText(ChatListActivity.this,"Private dialog created",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ChatListActivity.this,"All contacts are loaded.",Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case ERROR: {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ChatListActivity.this,asyncResponse.error.toString(),Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;

        }

    }




}
