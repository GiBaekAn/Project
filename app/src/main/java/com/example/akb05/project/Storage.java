package com.example.akb05.project;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by dong on 2018-06-09.
 */public class Storage implements ClusterItem {
    private LatLng location;
    private String mName;

    public Storage(LatLng location, String name) {
        this.location = location;
        this.mName = name;
    }

    public LatLng getLocation(){
        return location;
    }
    public void setLocation(LatLng location){
        this.location = location;
    }
    public String getName(){
        return mName;
    }
    public void setName(String name){
        this.mName = name;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}