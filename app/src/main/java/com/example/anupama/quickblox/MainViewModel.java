package com.example.anupama.quickblox;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    public MutableLiveData<AsyncResponse> asyncResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<QBChatDialog>> qbChatDialogLiveData = new MutableLiveData<>();

    private QBRequestGetBuilder mQBRequestGetBuilder ;

    public MainViewModel() {
    }

    public void signInToChat(String username ,String password){
        final QBUser user = new QBUser(username, password);

        QBUsers.signIn(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                // success
                asyncResponseMutableLiveData.postValue(AsyncResponse.success(null));

            }

            @Override
            public void onError(QBResponseException error) {
                // error
                asyncResponseMutableLiveData.postValue(AsyncResponse.error(new Throwable(error)));
            }
        });

    }

    public void createChatSession() {

        final QBUser qbUser = new QBUser("rishabhandroid", "quickblox");
        //final QBUser qbUser = new QBUser("ayush", "quickblox");

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
                        //asyncResponseMutableLiveData.setValue(AsyncResponse.success(null));
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        asyncResponseMutableLiveData.setValue(AsyncResponse.error(new Throwable(e)));
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                asyncResponseMutableLiveData.setValue(AsyncResponse.error(new Throwable(e)));
            }
        });

    }

    public void loadChatDialogs(){

        mQBRequestGetBuilder = ChatSingleton.getChatInstance().qbRequestGetBuilder();
        mQBRequestGetBuilder.setLimit(10);

        QBRestChatService.getChatDialogs(null,mQBRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                // notify adapter
                asyncResponseMutableLiveData.setValue(AsyncResponse.success(null));
                qbChatDialogLiveData.setValue(qbChatDialogs);
            }

            @Override
            public void onError(QBResponseException e) {
                asyncResponseMutableLiveData.setValue(AsyncResponse.error(new Throwable(e)));
            }
        });

    }

}
