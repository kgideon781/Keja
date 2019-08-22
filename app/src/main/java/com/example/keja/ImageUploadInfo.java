package com.example.keja;

public class ImageUploadInfo {
    String location, price, place, image;

    public ImageUploadInfo(String location, String price, String place, String image) {
        this.location = location;
        this.price = price;
        this.place = place;
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
