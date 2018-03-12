package ru.samara.mapapp.data;

import android.graphics.Bitmap;

public class MyProfile extends Profile {
    String token;

    public MyProfile(String name, String lastName, Bitmap avatar, String token, int id) {
        super(name, lastName, avatar);
        this.token = token;
        super.id = id;
    }

    public MyProfile() {
        super();
    }

    public String getToken() {
        return token;
    }
}
