package com.example.finalproject.Models;

//This class models LandMark data which will be available to show through QR codes
public class LandMark {

    String place;
    String name;
    String image;
    String description;

    public String getPlace() {
        return place;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public LandMark(String place, String name, String image, String description){
        this.place = place;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public LandMark(){

    }

}
