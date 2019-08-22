package com.example.keja.Model;

public class FavModel {
    private String place, price, location, image, house_id;
    public FavModel() {
    }

    public FavModel(String place, String price, String location, String image, String house_id) {
        this.place = place;
        this.price = price;
        this.location = location;
        this.image = image;
        this.house_id = house_id;
    }

    public String getHouse_id() {
        return house_id;
    }

    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
