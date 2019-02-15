package com.example.anupama.quickblox;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class MainViewModel extends ViewModel {

    public MutableLiveData<AsyncResponse> asyncResponseMutableLiveData = new MutableLiveData<>();

    public MainViewModel() {

    }

    public void signInToChat(String username ,String password){
        final QBUser user = new QBUser(username, password);

        Log.e("Rishabh","SignInToChat username: "+username);
        Log.e("Rishabh","SignInToChat password: "+password);

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

}
