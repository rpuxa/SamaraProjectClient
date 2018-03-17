package ru.samara.mapapp.data;

import android.graphics.Bitmap;

public class MyProfile extends Profile {
    private String token;

    public MyProfile(String name, String lastName, Bitmap avatar, String token, int id) {
        super(id, name, lastName, avatar, 0);
        this.token = token;
    }

    public MyProfile() {
        super();
    }

    public String getToken() {
        return token;
    }


}
