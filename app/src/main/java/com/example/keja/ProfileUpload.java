package com.example.keja;

public class ProfileUpload {
    String name, image;

    public ProfileUpload(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public ProfileUpload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
