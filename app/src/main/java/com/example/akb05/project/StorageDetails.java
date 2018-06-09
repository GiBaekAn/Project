package com.example.akb05.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by dong on 2018-06-09.
 */

public class StorageDetails extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storagedetails);

        Intent intent = getIntent();

        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);

        Log.d("알림","되는거냐lat??"+ String.valueOf(lat));
        Log.d("알림","마는거냐lng??"+ String.valueOf(lng));
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng mylocation = new LatLng(lat,lng);
        markerOptions.position(mylocation);


        mMap.addMarker(markerOptions.title("받아와서찍은거"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));
        Log.v("알림","lat값은??"+ String.valueOf(lat));
        Log.v("알림","lat값은??"+ String.valueOf(lng));

    }
}
