package com.example.keja;

import android.content.Context;

public class KejaHelper {
    private String price,place,location,image,fav,uid, houseid;
    public KejaHelper(String price, String place, String location, String image, String fav, String uid, String houseid) {
        this.price = price;
        this.place = place;
        this.location = location;
        this.uid = uid;
        this.image = image;
        this.fav = fav;
        this.houseid = fav;

    }

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public KejaHelper(Context applicationContext){

    }
    public KejaHelper(){

    }
}
