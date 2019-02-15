package com.example.anupama.quickblox;

import android.content.Context;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.request.QBRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;

import static com.example.anupama.quickblox.Constants.ACCOUNT_KEY;

public class ChatSingleton {

    private static ChatSingleton chatSingleton = new ChatSingleton();
    private QBChatService qbChatService;
    private QBRequestGetBuilder qbRequestGetBuilder;

    private ChatSingleton(){
        QBSettings.getInstance().init(AppApplication.getMyApplicationContext(), Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
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
