package com.example.anupama.quickblox;

import android.content.Context;
import android.util.Log;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.ServiceZone;
import com.quickblox.core.request.QBRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;

import static com.example.anupama.quickblox.Constants.ACCOUNT_KEY;
import static com.example.anupama.quickblox.Constants.API_DOAMIN;
import static com.example.anupama.quickblox.Constants.CHAT_DOMAIN;

public class ChatSingleton {

    private static ChatSingleton chatSingleton = new ChatSingleton();
    private QBChatService qbChatService;
    private QBRequestGetBuilder qbRequestGetBuilder;

    private ChatSingleton(){
        QBSettings.getInstance().init(AppApplication.getMyApplicationContext(), Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBSettings.getInstance().setEndpoints(Constants.API_DOAMIN,Constants.CHAT_DOMAIN,ServiceZone.DEVELOPMENT);
        QBSettings.getInstance().setZone(ServiceZone.DEVELOPMENT);
        qbChatService = QBChatService.getInstance();
        qbRequestGetBuilder = new QBRequestGetBuilder();
    }

    public static ChatSingleton getChatInstance(){
        return chatSingleton ;
    }


    public QBChatService chatService(){
        return qbChatService;

    }

    public QBRequestGetBuilder qbRequestGetBuilder(){
        return qbRequestGetBuilder;

    }

}
