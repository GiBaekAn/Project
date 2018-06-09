package com.example.akb05.project;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dong on 2018-06-08.
 */

public class StoreDetails extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;


    protected void onCreate(Bundle savedInstanceState) {
        //GIlide 통해서 이미지 받아오기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storedetails);


    }

    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        Mainpage mlocation = new Mainpage();

        double lat = mlocation.getLat();
        double lng = mlocation.getLng();


        LatLng mylocation = new LatLng(lat,lng);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mylocation).title("지금 추가된 편의점");

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));

        Log.d("디테일lat값", String.valueOf(lat));


    }

}