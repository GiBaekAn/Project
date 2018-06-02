package com.example.akb05.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by dong on 2018-05-21.
 */

public class Mainpage extends FragmentActivity implements OnMapReadyCallback{

    LinearLayout MapContainer;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://project-245fb.appspot.com").child("photo").child("13062.jpg");

    ImageButton menuimg;
    Button inputmenubtn;
    // 오버레이 관리자

    GoogleMap mMap;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        MapContainer = (LinearLayout) findViewById(R.id.mapcontainer);
        menuimg = findViewById(R.id.menuimg);
        inputmenubtn = findViewById(R.id.inputmenubtn);

        Glide.with(this).load(R.drawable.foodimage1).into(menuimg);

        menuimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailmenu = new Intent(getApplicationContext(), MenuDetails.class);
                startActivity(detailmenu);
            }
        });

        inputmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputMenu.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ajou = new LatLng(37.279940, 127.043867);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ajou).title("여기는 아주대학교");

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ajou));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ajou,16));

    }
}
// 구글맵 사용하는 코드
