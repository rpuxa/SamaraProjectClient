package ru.samara.mapapp.data;

import android.graphics.Bitmap;

public class Profile {
    int id;
    String name, lastName;
    Bitmap avatar;

    public Profile(String name, String lastName, Bitmap avatar) {
        this.name = name;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public Profile() {
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public int getId() {
        return id;
    }
}
