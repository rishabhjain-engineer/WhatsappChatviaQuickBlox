package com.example.anupama.quickblox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogsActivity extends AppCompatActivity implements ChatDialogsAdapter.RowClicked {

    private RecyclerView mChatRv;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatDialogsAdapter mChatDialogsAdapter;
    private FloatingActionButton mFloatingActionButton;
    private ChatDialogViewModel mChatDialogViewModel;
    private ProgressBar mChatDialogPb;
    private ArrayList<QBChatDialog> mQBChatDialogList = new ArrayList<>();
    private String receivedUsername, receivedPassword ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialog);
        initComponent();

        if(getIntent() != null){
            receivedUsername = getIntent().getStringExtra("username");
            receivedPassword = getIntent().getStringExtra("password");
            mChatDialogViewModel.createChatSession(receivedUsername,receivedPassword);
        }



        mChatDialogViewModel.getResponse().observe(this, new Observer<AsyncResponse>() {
            @Override
            public void onChanged(@Nullable AsyncResponse asyncResponse) {
                if (asyncResponse != null)
                    consumeResponse(asyncResponse);
            }
        });


        mChatDialogViewModel.getPersistentChatDialogs().getList().observe(this, new Observer<ArrayList<QBChatDialog>>() {
            @Override
            public void onChanged(@Nullable ArrayList<QBChatDialog> qbChatDialogs) {
                if (qbChatDialogs != null) {
                    mQBChatDialogList.clear();
                    mQBChatDialogList.addAll(qbChatDialogs);
                    mChatDialogsAdapter.notifyDataSetChanged();
                }

            }
        });

    }


    void initComponent() {

        mChatDialogViewModel = ViewModelProviders.of(this).get(ChatDialogViewModel.class);
        mChatRv = findViewById(R.id.chat_rv);
        mFloatingActionButton = findViewById(R.id.floating_btn);
        mChatDialogPb = findViewById(R.id.chat_dialog_pb);
        mLayoutManager = new LinearLayoutManager(this);
        mChatRv.setLayoutManager(mLayoutManager);
        mChatRv.setHasFixedSize(true);
        mChatDialogPb.setVisibility(View.VISIBLE);
        mFloatingActionButton.setOnClickListener(onClickListener);
        mChatDialogsAdapter = new ChatDialogsAdapter(mQBChatDialogList, ChatDialogsActivity.this);
        mChatRv.setAdapter(mChatDialogsAdapter);
    }

    private void consumeResponse(AsyncResponse asyncResponse) {

        switch (asyncResponse.responseStatus) {

            case LOADING: {
                break;
            }
            case SUCCESS: {
                if(asyncResponse.data.getAsBoolean()){
                    mChatDialogViewModel.loadChatDialogs();
                }else {
                    Toast.makeText(ChatDialogsActivity.this, "Chat dialogs loaded.", Toast.LENGTH_SHORT).show();
                    mChatDialogPb.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case ERROR: {
                mChatDialogPb.setVisibility(View.INVISIBLE);
                Toast.makeText(ChatDialogsActivity.this, asyncResponse.error.toString(), Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;

        }

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.floating_btn: {
                    Intent intent = new Intent(ChatDialogsActivity.this, ChatListActivity.class);
                    startActivity(intent);
                    break;
                }

                default:
                    break;

            }
        }
    };

    @Override
    public void onTouch(QBChatDialog qbChatDialog) {
        Intent intent = new Intent(ChatDialogsActivity.this, ChatMessageActivity.class);
        intent.putExtra("chatDialog", qbChatDialog);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatDialogViewModel.clearOff();
    }
}
