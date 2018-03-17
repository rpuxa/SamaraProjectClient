package ru.samara.mapapp.chat;


import android.content.Context;
import android.widget.BaseAdapter;

import ru.samara.mapapp.data.Profile;
import ru.samara.mapapp.data.ProfileBase;

public class Comment {
    private int userId;
    private String userName, text;
    private long time;
    private Profile author;

    public Comment(int userId, String userName, String text, long time) {
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.time = time;
    }

    public Comment(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public void loadProfile(Context context, BaseAdapter adapter) {
        author = ProfileBase.base.get(userId, context, bitmap -> adapter.notifyDataSetChanged());
    }

    public int getUserId() {
        return userId;
    }

    public long getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public Profile getAuthor() {
        return author;
    }
}
