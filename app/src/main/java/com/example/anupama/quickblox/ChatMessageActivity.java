package com.example.anupama.quickblox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class ChatMessageActivity extends AppCompatActivity {

    private QBChatDialog receivedQBChatDialog = new QBChatDialog();
    private Button mSendMessageBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message_sent);


        receivedQBChatDialog = (QBChatDialog) getIntent().getSerializableExtra("chatDialog");

        initChatDialog();
        retrieveMessages();

        mSendMessageBtn = findViewById(R.id.send_message_btn);

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody("");
                qbChatMessage.setSenderId(ChatSingleton.getChatInstance(ChatMessageActivity.this).chatService().getUser().getId());
                qbChatMessage.setSaveToHistory(true);

                try {
                    receivedQBChatDialog.sendMessage(qbChatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    void initChatDialog(){




        receivedQBChatDialog.initForChat(ChatSingleton.getChatInstance(this).chatService());
        ChatSingleton.getChatInstance(this).chatService().setUseStreamManagement(true);

            QBIncomingMessagesManager incomingMessagesManager =
                    ChatSingleton.getChatInstance(this).chatService().getIncomingMessagesManager() ;

            incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
                @Override
                public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                }

                @Override
                public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                }
            });



            // TODO: WORKS ONLY FOR GROUP DIALOGS

           /* receivedQBChatDialog.addMessageListener(new QBChatDialogMessageListener() {
                @Override
                public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                    Log.e("Rishabh","qbChatDialog messages: "+qbChatMessage.getBody());
                }

                @Override
                public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                    Log.e("Rishabh"," process error: "+e.toString()) ;
                }
            });*/



    }


    void retrieveMessages(){
        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder() ;
        qbMessageGetBuilder.setLimit(100);

        if(receivedQBChatDialog != null) {
            QBRestChatService.getDialogMessages(receivedQBChatDialog,qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {

                }

                @Override
                public void onError(QBResponseException e) {
                }
            });
        }
    }
}
