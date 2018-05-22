package com.example.akb05.project;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by dong on 2018-05-21.
 */

public class Mainpage extends FragmentActivity{

    private GoogleMap mGoogleMap;

    ImageButton menuimg;
    Button inputmenubtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        menuimg = findViewById(R.id.menuimg);
        inputmenubtn = findViewById(R.id.inputmenubtn);

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

        MapsInitializer.initialize(getApplicationContext());
//        init();
    }
//
//    public void onMapClick(LatLng point) {
//
//
//
//        // 현재 위도와 경도에서 화면 포인트를 알려준다
//
//        Point screenPt = mGoogleMap.getProjection().toScreenLocation(point);
//
//
//
//        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
//
//        LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);
//
//
//
//        Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도("
//
//                + String.valueOf(point.longitude) + ")");
//
//        Log.d("화면좌표", "화면좌표: X(" + String.valueOf(screenPt.x) + "), Y("
//
//                + String.valueOf(screenPt.y) + ")");
//
//    }
//
//
//
//    /**
//
//     * 초기화
//
//     * @author
//
//     */
//
//    private void init() {
//
//
//
//        GooglePlayServicesUtil.isGooglePlayServicesAvailable(CurrentMapActivity.this);
//
//        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
//
//                .findFragmentById(R.id.map)).getMap();
//
//
//
//        // 맵의 이동
//
//        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
//
//
//
//        GpsInfo gps = new GpsInfo(CurrentMapActivity.this);
//
//        // GPS 사용유무 가져오기
//
//        if (gps.isGetLocation()) {
//
//            double latitude = gps.getLatitude();
//
//            double longitude = gps.getLongitude();
//
//
//
//            // Creating a LatLng object for the current location
//
//            LatLng latLng = new LatLng(latitude, longitude);
//
//
//
//            // Showing the current location in Google Map
//
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//
//
//            // Map 을 zoom 합니다.
//
//            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
//
//
//
//            // 마커 설정.
//
//            MarkerOptions optFirst = new MarkerOptions();
//
//            optFirst.position(latLng);// 위도 • 경도
//
//            optFirst.title("Current Position");// 제목 미리보기
//
//            optFirst.snippet("Snippet");
//
//            optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
//
//            mGoogleMap.addMarker(optFirst).showInfoWindow();
//
//        }
//
//    }
//
//}
//
//
//출처: http://mainia.tistory.com/1168 [녹두장군 - 상상을 현실로]
//

}
// 구글맵 사용하는 코드

