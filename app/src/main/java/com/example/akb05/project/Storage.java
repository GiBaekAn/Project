package com.example.akb05.project;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by dong on 2018-06-09.
 */public class Storage implements ClusterItem {
    private final LatLng mPosition;

    public Storage(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}