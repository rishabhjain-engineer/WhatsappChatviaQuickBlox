package com.example.anupama.quickblox;

import android.content.Context;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.request.QBRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;

import static com.example.anupama.quickblox.Constants.ACCOUNT_KEY;

public class ChatSingleton {

    private static ChatSingleton chatSingleton;
    private QBChatService qbChatService;
    private QBRequestGetBuilder qbRequestGetBuilder;

    private ChatSingleton(Context context){
        QBSettings.getInstance().init(context.getApplicationContext(), Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        qbChatService = QBChatService.getInstance();
        qbRequestGetBuilder = new QBRequestGetBuilder();
    }

    public static synchronized ChatSingleton getChatInstance(Context context){

        if(chatSingleton == null){
            chatSingleton = new ChatSingleton(context);
        }
        return chatSingleton ;
    }


    public QBChatService chatService(){
        return qbChatService;

    }

    public QBRequestGetBuilder qbRequestGetBuilder(){
        return qbRequestGetBuilder;

    }

}
