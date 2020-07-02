package com.example.finalproject;

import com.google.android.gms.maps.model.LatLng;

//This class models TouristPlace data which will be charge as markers on the map
public class TouristPlace {

    String name;
    LatLng location;
    String description;

    public TouristPlace(String name, LatLng location, String description){
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }


    public String getDescription() {
        return description;
    }

}
