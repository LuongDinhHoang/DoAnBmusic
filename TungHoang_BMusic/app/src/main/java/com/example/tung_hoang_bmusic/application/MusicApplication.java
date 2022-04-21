package com.example.tung_hoang_bmusic.application;

import com.firebase.client.Firebase;

public class MusicApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
