package com.example.keja.Model;

public class Comments {

  private String user, message;

    public Comments(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public Comments() {
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

