package com.example.anupama.quickblox;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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
    private MutableLiveData<AsyncResponse> mutableAsyncResponseLiveData = new MutableLiveData<>();

    public ChatMessageViewModel(){
        persistentChatMessages = PersistentChatMessages.getInstance();
    }

    public void initChatDialog(final QBChatDialog qbChatDialog){

        qbChatDialog.initForChat(ChatSingleton.getChatInstance().chatService());

        JsonElement element = new JsonPrimitive(true);
        mutableAsyncResponseLiveData.postValue(AsyncResponse.success(element));
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
                Log.e("Rishabh","incoming message error: "+e.toString());
                mutableAsyncResponseLiveData.postValue(AsyncResponse.error(new Throwable(e)));
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

    public void  retrieveMessages(final QBChatDialog qbChatDialog){
        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder() ;
        qbMessageGetBuilder.setLimit(100);

        if(qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog,qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    persistentChatMessages.setList(qbChatMessages);
                    JsonElement element = new JsonPrimitive(false);
                    mutableAsyncResponseLiveData.postValue(AsyncResponse.success(element));
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("Rishabh","retrieve message error: "+e.toString());
                    mutableAsyncResponseLiveData.postValue(AsyncResponse.error(new Throwable(e)));
                }
            });
        }
    }

    public PersistentChatMessages getChatMessageInstance(){
        return persistentChatMessages;
    }

    public MutableLiveData<AsyncResponse> getMutableAsyncResponseLiveData() {
        return mutableAsyncResponseLiveData;
    }
}
