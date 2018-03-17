package ru.samara.mapapp.activities;

import android.app.Application;

import com.vk.sdk.VKSdk;

import ru.samara.mapapp.cache.Conservation;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Conservation.instance.load(getFilesDir());
        VKSdk.initialize(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        Conservation.instance.save(getFilesDir());
    }
}
