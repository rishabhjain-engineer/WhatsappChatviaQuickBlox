package com.example.anupama.quickblox;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatListViewModel extends ViewModel {

    private ArrayList<QBUser> qbUserArrayList = new ArrayList<>();
    private PersistentChatDialogs persistentChatDialogs;
    private MutableLiveData<ArrayList<QBUser>> qbUserListLiveData = new MutableLiveData<>();
    private MutableLiveData<AsyncResponse> asyncResponseMutableLiveData = new MutableLiveData<>();

    public ChatListViewModel(){
        persistentChatDialogs = PersistentChatDialogs.getInstance();
    }

    public void retrieveAllUser(){
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                for(QBUser u : qbUsers){
                    if(!u.getLogin().equalsIgnoreCase(ChatSingleton.getChatInstance().chatService().getUser().getLogin())){
                        qbUserArrayList.add(u);
                    }
                }

                JsonElement element = new JsonPrimitive(false);
                qbUserListLiveData.postValue(qbUserArrayList);
                asyncResponseMutableLiveData.postValue(AsyncResponse.success(element));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Rishabh","retrieve all user error: "+e.toString());
                asyncResponseMutableLiveData.postValue(AsyncResponse.error(new Throwable(e)));
            }
        });

    }

    public void createPrivateChat(QBUser qbUser){

        asyncResponseMutableLiveData.postValue(AsyncResponse.loading());
        QBChatDialog dialog = DialogUtils.buildPrivateDialog(qbUser.getId());
        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                persistentChatDialogs.addDialogToList(qbChatDialog);
                JsonElement element = new JsonPrimitive(true);
                asyncResponseMutableLiveData.postValue(AsyncResponse.success(element));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Rishabh","create pvt chat error: "+e.toString());
                asyncResponseMutableLiveData.postValue(AsyncResponse.error(new Throwable(e)));
            }
        });
    }

    public void createGroupChat(){

    }


    public MutableLiveData<ArrayList<QBUser>> getQbUserListLiveData(){
        return qbUserListLiveData ;
    }

    public MutableLiveData<AsyncResponse> getAsyncResponseMutableLiveData() {
        return asyncResponseMutableLiveData;
    }
}
