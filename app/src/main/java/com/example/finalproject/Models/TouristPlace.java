package com.example.finalproject.Models;

import com.google.android.gms.maps.model.LatLng;

//This class models TouristPlace data which will be charge as markers on the map
public class TouristPlace {

    String name;
    Double latitude;
    Double longitude;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TouristPlace(String name, Double latitude, Double longitude, String description){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public TouristPlace(){

    }



}
