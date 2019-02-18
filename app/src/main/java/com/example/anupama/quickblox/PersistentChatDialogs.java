package com.example.anupama.quickblox;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class PersistentChatDialogs {

    private MutableLiveData<ArrayList<QBChatDialog>> livedata;
    private ArrayList<QBChatDialog> list;
    private static PersistentChatDialogs INSTANCE ;

    private PersistentChatDialogs(){
        livedata = new MutableLiveData<>();
        list = new ArrayList<>();
    }

    public static synchronized PersistentChatDialogs getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PersistentChatDialogs();
        }
        return INSTANCE;
    }

    public void setChatDialogsList(ArrayList<QBChatDialog> chatDialogsList) {
        list.addAll(chatDialogsList);
        livedata.postValue(list);

    }

    public void addDialogToList(QBChatDialog qbChatDialog){
        list.add(qbChatDialog);
        livedata.postValue(list);
    }

    public MutableLiveData<ArrayList<QBChatDialog>> getList(){
        return livedata;
    }

    public void clearDataset(){
        list.clear();
        livedata.postValue(null);
    }
}
