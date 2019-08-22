package com.example.keja;

public class CommentModel {
    private String user, message;

    public CommentModel(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public CommentModel() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
