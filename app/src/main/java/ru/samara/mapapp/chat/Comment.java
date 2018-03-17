package ru.samara.mapapp.chat;


import android.app.Activity;
import android.content.Context;
import android.widget.BaseAdapter;

import ru.samara.mapapp.data.Profile;
import ru.samara.mapapp.data.ProfileBase;
import ru.samara.mapapp.utils.DateUtils;

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

    public void loadProfile(Activity activity, BaseAdapter adapter) {
        author = ProfileBase.base.get(userId, activity, bitmap -> {
            getAuthor().setAvatar(bitmap);
            activity.runOnUiThread(adapter::notifyDataSetChanged);
        });
    }

    public int getUserId() {
        return userId;
    }

    public long getTime() {
        return time;
    }

    public String getStringTime() {
        return DateUtils.dateToString(time);
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
