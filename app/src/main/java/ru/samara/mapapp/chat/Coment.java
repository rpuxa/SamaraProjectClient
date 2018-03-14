package ru.samara.mapapp.chat;


public class Coment {
    int userId;
    String userName, text;

    public Coment(int userId, String userName, String text) {
        this.userId = userId;
        this.userName = userName;
        this.text = text;
    }

    public Coment(int userId, String text) {
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
