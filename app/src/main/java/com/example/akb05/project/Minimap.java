package com.example.akb05.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
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

public class Minimap extends FragmentActivity implements OnMapReadyCallback  {

    GoogleMap mMap;
    double lat;
    double lng;
    LinearLayout MapContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimap);
        //getSupportActionBar().hide();

        MapContainer = (LinearLayout)findViewById(R.id.mapcontainer);

        Intent intent = getIntent();

        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);

        Log.v("알림","lat??"+ String.valueOf(lat));
        Log.v("알림","lng??"+ String.valueOf(lng));

       SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
       mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mylocation = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(mylocation).title("받아와서 찍은거"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));

        Log.d("알림","지도에찍을lat??"+ String.valueOf(lat));
        Log.d("알림","지도에찍을lng??"+ String.valueOf(lng));
        //ClusterManager<Storage> mClusterManager = new ClusterManager<Storage>(this,mMap);
        //mMap.setOnCameraChangeListener(mClusterManager);
        //mMap.setOnInfoWindowClickListener(this);///////////////////////////////////////////////////////////////////////////////////////////////안되는부분


    }

}
