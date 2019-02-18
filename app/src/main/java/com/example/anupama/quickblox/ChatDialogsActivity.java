package com.example.anupama.quickblox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatDialogsActivity extends AppCompatActivity implements ChatDialogsAdapter.RowClicked {

    private RecyclerView mChatRv ;
    private RecyclerView.LayoutManager mLayoutManager ;
    private ChatDialogsAdapter mChatDialogsAdapter;
    private FloatingActionButton mFloatingActionButton ;
    private ChatDialogViewModel mChatDialogViewModel ;
    private ProgressBar mChatDialogPb ;
    private ArrayList<QBChatDialog> mQBChatDialogList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialog);
        initComponent();

        mChatDialogViewModel.createChatSession();
        mChatDialogViewModel.loadChatDialogs();

        mChatDialogViewModel.getResponse().observe(this, new Observer<AsyncResponse>() {
            @Override
            public void onChanged(@Nullable AsyncResponse asyncResponse) {
                if(asyncResponse!=null)
                    consumeResponse(asyncResponse);
            }
        });


     mChatDialogViewModel.getPersistentChatDialogs().getList().observe(this, new Observer<ArrayList<QBChatDialog>>() {
         @Override
         public void onChanged(@Nullable ArrayList<QBChatDialog> qbChatDialogs) {
             Log.e("Rishabh","on change called: chat Dialog size: "+qbChatDialogs.size());
             mQBChatDialogList.clear();
             mQBChatDialogList.addAll(qbChatDialogs);
             mChatDialogsAdapter.notifyDataSetChanged();
         }
     });

    }


    void initComponent(){

        mChatDialogViewModel = ViewModelProviders.of(this).get(ChatDialogViewModel.class);
        mChatRv = findViewById(R.id.chat_rv);
        mFloatingActionButton = findViewById(R.id.floating_btn);
        mChatDialogPb = findViewById(R.id.chat_dialog_pb);
        mLayoutManager = new LinearLayoutManager(this);
        mChatRv.setLayoutManager(mLayoutManager);
        mChatRv.setHasFixedSize(true);
        mChatDialogPb.setVisibility(View.VISIBLE);
        mFloatingActionButton.setOnClickListener(onClickListener);
        mChatDialogsAdapter = new ChatDialogsAdapter(mQBChatDialogList,ChatDialogsActivity.this);
        mChatRv.setAdapter(mChatDialogsAdapter);
    }

    private void consumeResponse(AsyncResponse asyncResponse) {

        switch (asyncResponse.responseStatus){

            case LOADING:{
                break;
            }
            case SUCCESS:{
                Toast.makeText(ChatDialogsActivity.this,"Chat dialogs loaded.",Toast.LENGTH_SHORT).show();
                mChatDialogPb.setVisibility(View.INVISIBLE);
                break;
            }
            case ERROR: {
                mChatDialogPb.setVisibility(View.INVISIBLE);
                Toast.makeText(ChatDialogsActivity.this,asyncResponse.error.toString(),Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;

        }

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.floating_btn: {
                    Intent intent = new Intent(ChatDialogsActivity.this,ChatListActivity.class);
                    startActivity(intent);
                    break;
                }

                default: break;

            }
        }
    };

    @Override
    public void onTouch(QBChatDialog qbChatDialog) {
        Intent intent = new Intent(ChatDialogsActivity.this,ChatMessageActivity.class);
        intent.putExtra("chatDialog",qbChatDialog);
        startActivity(intent);
    }
}
