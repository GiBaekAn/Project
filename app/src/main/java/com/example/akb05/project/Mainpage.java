package com.example.akb05.project;

import android.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dong on 2018-05-21.
 */

public class Mainpage extends FragmentActivity implements OnMapReadyCallback{

    LinearLayout MapContainer;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://project-245fb.appspot.com").child("photo").child("13062.jpg");

    Uri a = Uri.parse(storageReference.toString());

    ImageView test;
    ImageButton menuimg;
    Button inputmenubtn;
    // 오버레이 관리자

    GoogleMap mMap;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
//
//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                Toast.makeText(Mainpage.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(Mainpage.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//            }
//        };
//        new TedPermission(this)
//                .setPermissionListener(permissionlistener)
//                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission] ")
//                .setPermissions(android.Manifest.permission.INTERNET)
//                .check();


        MapContainer = (LinearLayout) findViewById(R.id.mapcontainer);
        menuimg = findViewById(R.id.menuimg);
        inputmenubtn = findViewById(R.id.inputmenubtn);
        test = findViewById(R.id.test);

        Picasso.get().load(a).error(R.drawable.err).into(test);
        //Glide.with(this).load(a).into(test);
        //Glide.with(getBaseContext()).load(a).into(menuimg);

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
