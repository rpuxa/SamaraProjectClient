package ru.samara.mapapp.chat;


public class Comment {
    private int userId;
    private String userName, text;
    private long time;

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
}
