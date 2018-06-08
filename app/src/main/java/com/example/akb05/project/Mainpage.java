package com.example.akb05.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dong on 2018-05-21.
 */

public class Mainpage extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    LinearLayout MapContainer;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://project-245fb.appspot.com").child("photo").child("13062.jpg");

    Uri a = Uri.parse(storageReference.toString());

    ImageButton menuimg;
    Button inputmenubtn;
    // 오버레이 관리자
    GoogleMap addMap;
    GoogleMap mMap;

    MainActivity mlocation = new MainActivity();

    double lat = mlocation.getLat();
    double lng = mlocation.getLng();

    public static Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        mContext = this;

        MapContainer = (LinearLayout) findViewById(R.id.mapcontainer);
        menuimg = findViewById(R.id.menuimg);
        inputmenubtn = findViewById(R.id.inputmenubtn);

        //Glide.with(this).load(a).into(menuimg);
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


        //LatLng ajou = new LatLng(37.279940, 127.043867);

        LatLng mylocation = new LatLng(lat,lng);

        //savelocation.add(mylocation);


//        MarkerOptions mylocationmarker = new MarkerOptions();
//        mylocationmarker.position(mylocation).title("현재 나의 위치");
//
//
//        mMap.addMarker(mylocationmarker);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));

        mMap.addMarker(new MarkerOptions().position(mylocation).title("등록한편의점"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
        ClusterManager<Storage> mClusterManager = new ClusterManager<Storage>(this,mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        for(int i=0; i<100; i++){
            double offset = i/1000d;
            lat = lat + offset;
            lng = lng + offset;
            Storage offsetItem = new Storage(lat, lng);
            mClusterManager.addItem(offsetItem);
        }


        //마커클릭 리스너
       // mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        Log.d("lat값은??", String.valueOf(lat));
        Log.d("lat값은??", String.valueOf(lng));
    }
//
//    public void addMapmarker(GoogleMap googleMap){
//        InputMenu addmarker = new InputMenu();
//
//        double addlat = addmarker.getLat();
//        double addlng = addmarker.getLng();
//
//        addMap = googleMap;
//
//        LatLng addloc = new LatLng(addlat, addlng);
//
//        MarkerOptions addmarkerOptions = new MarkerOptions();
//        addmarkerOptions.position(addloc).title("지금 추가된 편의점");
//        //추가되는 마커
//        addMap.addMarker(addmarkerOptions);
//        addMap.moveCamera(CameraUpdateFactory.newLatLng(addloc));
//        addMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addloc,16));
//    }

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        Intent intent = new Intent(getApplicationContext(), StoreDetails.class);
//        startActivity(intent);
//        return false;
//    }

    public double getLat(){
        Log.d("로그인시 lat값:", String.valueOf(lat));
        return lat;
    }
    public double getLng(){
        Log.d("로그인시 lng값:", String.valueOf(lng));
        return lng;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getApplicationContext(), StoreDetails.class);
        startActivity(intent);
    }
}
// 구글맵 사용하는 코드
