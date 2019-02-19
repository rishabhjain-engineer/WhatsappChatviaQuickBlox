package com.example.anupama.quickblox;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import java.util.ArrayList;

public class ChatMessageViewModel extends ViewModel{

    private PersistentChatMessages persistentChatMessages;

    public ChatMessageViewModel(){
        persistentChatMessages = PersistentChatMessages.getInstance();
    }

    public void initChatDialog(final QBChatDialog qbChatDialog){

        qbChatDialog.initForChat(ChatSingleton.getChatInstance().chatService());
        ChatSingleton.getChatInstance().chatService().setUseStreamManagement(true);

        QBIncomingMessagesManager incomingMessagesManager =
                ChatSingleton.getChatInstance().chatService().getIncomingMessagesManager() ;

        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                Log.e("Rishabh","incoming chat message: "+qbChatMessage.getBody());
                Log.e("Rishabh","incoming chat string: "+s);
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

    public void retrieveMessages(final QBChatDialog qbChatDialog){
        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder() ;
        qbMessageGetBuilder.setLimit(100);

        if(qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog,qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {

                  /*  for(QBChatMessage qbChatMessage:qbChatMessages){
                        Log.e("Rishabh","\n\n *************************************");
                        Log.e("Rishabh","retrieved messages body: "+qbChatMessage.getBody());
                        Log.e("Rishabh","retrieved messages property names: "+qbChatMessage.getPropertyNames());
                        Log.e("Rishabh","retrieved messages sender Id: "+qbChatMessage.getSenderId());
                        Log.e("Rishabh","retrieved messages id: "+qbChatMessage.getId());
                        Log.e("Rishabh","retrieved messages delivered Ids: "+qbChatMessage.getDeliveredIds());
                        Log.e("Rishabh","\n\n *************************************");
                    }*/

                    persistentChatMessages.setList(qbChatMessages);
                }

                @Override
                public void onError(QBResponseException e) {
                }
            });
        }
    }

    public PersistentChatMessages getChatMessageInstance(){
        return persistentChatMessages;
    }

}
