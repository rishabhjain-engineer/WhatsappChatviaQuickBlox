package com.example.anupama.quickblox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        receivedQBChatDialog = (QBChatDialog) getIntent().getSerializableExtra("chatDialog");

        initComponent();
        mChatMessageViewModel.initChatDialog(receivedQBChatDialog);
        mChatMessageViewModel.retrieveMessages(receivedQBChatDialog);



        mChatMessageViewModel.getChatMessageInstance().getLivedata().observe(this, new Observer<ArrayList<QBChatMessage>>() {
            @Override
            public void onChanged(@Nullable ArrayList<QBChatMessage> qbChatMessages) {

                Log.e("Rishabh","on Change: qbChatMessages size: "+qbChatMessages.size());
                if(mChatMessageAdapter == null){
                    mChatMessageAdapter = new ChatMessageAdapter(qbChatMessages);
                    mMessageRv.setAdapter(mChatMessageAdapter);
                    Log.e("Rishabh","on Change adapter set");
                }else {
                    Log.e("Rishabh","on Change adapter notify");
                    mChatMessageAdapter.notifyDataSetChanged();
                }
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
                    Log.e("Rishabh","Message sent: "+qbChatMessage.getBody());
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    Log.e("Rishabh","Message sent error: "+e.toString());
                }
            }
        });
    }


    void initComponent(){
        mChatMessageViewModel = ViewModelProviders.of(this).get(ChatMessageViewModel.class);
        mSendMessageBtn = findViewById(R.id.send_message_btn);
        mMessageRv = findViewById(R.id.message_rv);
        mMessageEt = findViewById(R.id.message_et);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mMessageRv.setLayoutManager(mLinearLayoutManager);
        mMessageRv.setHasFixedSize(true);
    }


}
