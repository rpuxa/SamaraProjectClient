package ru.samara.mapapp.chat;


public class Comment {
    private int userId;
    private String userName, text;

    public Comment(int userId, String userName, String text) {
        this.userId = userId;
        this.userName = userName;
        this.text = text;
    }

    public Comment(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }
}
