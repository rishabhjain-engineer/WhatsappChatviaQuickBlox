package com.example.anupama.quickblox;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatDialogViewModel extends ViewModel {

    private MutableLiveData<AsyncResponse> asyncResponseMutableLiveData = new MutableLiveData<>();
    private QBRequestGetBuilder mQBRequestGetBuilder;
    private PersistentChatDialogs persistentChatDialogs;


    public ChatDialogViewModel() {
        persistentChatDialogs = PersistentChatDialogs.getInstance();

    }

    public void createChatSession(String username, String password) {

        final QBUser qbUser = new QBUser(username, password);

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                ChatSingleton.getChatInstance().chatService().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e("Rishabh","Login success ");
                        JsonElement element = new JsonPrimitive(true);
                        asyncResponseMutableLiveData.postValue(AsyncResponse.success(element));
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Rishabh","Login error: "+e.toString());
                        asyncResponseMutableLiveData.postValue(AsyncResponse.error(new Throwable(e)));
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                asyncResponseMutableLiveData.postValue(AsyncResponse.error(new Throwable(e)));
            }
        });

    }

    public void loadChatDialogs() {

        mQBRequestGetBuilder = ChatSingleton.getChatInstance().qbRequestGetBuilder();
        mQBRequestGetBuilder.setLimit(10);

        QBRestChatService.getChatDialogs(null, mQBRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                Log.e("Rishabh","Load chat dialog success: ");
                JsonElement element = new JsonPrimitive(false);
                persistentChatDialogs.setChatDialogsList(qbChatDialogs);
                asyncResponseMutableLiveData.postValue(AsyncResponse.success(element));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Rishabh","Load chat dialog error: "+e.toString());
                asyncResponseMutableLiveData.postValue(AsyncResponse.error(new Throwable(e)));
            }
        });

    }

    public MutableLiveData<AsyncResponse> getResponse() {
        return asyncResponseMutableLiveData;
    }

    public PersistentChatDialogs getPersistentChatDialogs() {
        return persistentChatDialogs;
    }

    public void clearOff() {
        persistentChatDialogs.clearDataset();
        persistentChatDialogs = null;
    }
}
