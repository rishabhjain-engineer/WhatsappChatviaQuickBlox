package com.example.anupama.quickblox;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this ;
    }

    public static Context getMyApplicationContext() {
        return context;
    }
}
