package ru.samara.mapapp.activities;

import android.app.Application;

import com.vk.sdk.VKSdk;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
