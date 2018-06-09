package com.example.akb05.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by akb05 on 2018-06-09.
 */

public class Minimap extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener  {

    GoogleMap mMap;
    Double lat;
    Double lng;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimap);
        getSupportActionBar().hide();

        Intent intent = getIntent();

        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);
        Log.v("알림","lat??"+ String.valueOf(lat));
        Log.v("알림","lng??"+ String.valueOf(lng));



       // SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //MarkerOptions markerOptions = new MarkerOptions();
        //LatLng mylocation = new LatLng(lat,lng);
        //mMap.addMarker(markerOptions);
        //mMap.addMarker(new MarkerOptions().position(mylocation).title("등록한편의점"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
        //ClusterManager<Storage> mClusterManager = new ClusterManager<Storage>(this,mMap);
        //mMap.setOnCameraChangeListener(mClusterManager);
        //mMap.setOnInfoWindowClickListener(this);///////////////////////////////////////////////////////////////////////////////////////////////안되는부분


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getApplicationContext(), MenuDetails.class);/////////////////////////////////////////////////////////////////바꿔야함!
        startActivity(intent);
    }

}
