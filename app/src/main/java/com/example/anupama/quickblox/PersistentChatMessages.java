package com.example.anupama.quickblox;

import android.arch.lifecycle.MutableLiveData;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class PersistentChatMessages {

    private ArrayList<QBChatMessage> list = new ArrayList<>();
    private MutableLiveData<ArrayList<QBChatMessage>> livedata = new MutableLiveData<>();
    private static PersistentChatMessages Instance ;


    private PersistentChatMessages(){

    }

    public static synchronized PersistentChatMessages getInstance(){
        if(Instance==null){
            Instance = new PersistentChatMessages();
        }
        return Instance;
    }


    public void setList(ArrayList<QBChatMessage> list) {
        this.list = list;
        livedata.setValue(list);
    }

    public MutableLiveData<ArrayList<QBChatMessage>> getLivedata(){
        return livedata;
    }

    public void addToList(QBChatMessage qbChatMessage){
        list.add(qbChatMessage);
        livedata.setValue(list);
    }
}
