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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class ChatMessageActivity extends AppCompatActivity {

    private QBChatDialog receivedQBChatDialog = new QBChatDialog();
    private Button mSendMessageBtn ;
    private RecyclerView mMessageRv ;
    private EditText mMessageEt ;
    private LinearLayoutManager mLinearLayoutManager ;
    private ChatMessageViewModel mChatMessageViewModel;
    private ChatMessageAdapter mChatMessageAdapter;
    private ProgressBar mProgressBar;
    private int mLoggedInUserId ;
    private int mRecipientId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        initComponent();

        receivedQBChatDialog = (QBChatDialog) getIntent().getSerializableExtra("chatDialog");
        mLoggedInUserId = ChatSingleton.getChatInstance().chatService().getUser().getId() ;
        mChatMessageViewModel.initChatDialog(receivedQBChatDialog);

        for(int i : receivedQBChatDialog.getOccupants()){
            if(i != mLoggedInUserId){
                mRecipientId = i ;
            }
        }

        mChatMessageViewModel.getChatMessageInstance().getLivedata().observe(this, new Observer<ArrayList<QBChatMessage>>() {
            @Override
            public void onChanged(@Nullable ArrayList<QBChatMessage> qbChatMessages) {

                if(mChatMessageAdapter == null){
                    mChatMessageAdapter = new ChatMessageAdapter(qbChatMessages,
                            mLoggedInUserId,
                            mRecipientId);
                    mMessageRv.setAdapter(mChatMessageAdapter);
                }else {
                    mChatMessageAdapter.notifyDataSetChanged();
                }
            }
        });

        mChatMessageViewModel.getMutableAsyncResponseLiveData().observe(this, new Observer<AsyncResponse>() {
            @Override
            public void onChanged(@Nullable AsyncResponse asyncResponse) {
                consumeResponse(asyncResponse);
            }
        });

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(mMessageEt.getText().toString());
                qbChatMessage.setSenderId(ChatSingleton.getChatInstance().chatService().getUser().getId());
                qbChatMessage.setSaveToHistory(true);

                try {
                    receivedQBChatDialog.sendMessage(qbChatMessage);
                } catch (SmackException.NotConnectedException e) {
                    Log.e("Rishabh","send message error: "+e.toString());
                    e.printStackTrace();
                }
            }
        });
    }


    void initComponent(){

        mChatMessageViewModel = ViewModelProviders.of(this).get(ChatMessageViewModel.class);

        mSendMessageBtn = findViewById(R.id.send_message_btn);
        mMessageRv = findViewById(R.id.message_rv);
        mMessageEt = findViewById(R.id.message_et);
        mProgressBar = findViewById(R.id.message_pb);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mMessageRv.setLayoutManager(mLinearLayoutManager);
        mMessageRv.setHasFixedSize(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void consumeResponse(AsyncResponse asyncResponse) {

        switch (asyncResponse.responseStatus){

            case LOADING:{
                break;
            }
            case SUCCESS:{

                if(asyncResponse.data.getAsBoolean()){

                    mChatMessageViewModel.retrieveMessages(receivedQBChatDialog);
                }else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case ERROR: {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ChatMessageActivity.this,asyncResponse.error.toString(),Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;

        }

    }


}
