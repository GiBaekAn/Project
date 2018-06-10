package com.example.akb05.project;

import android.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.clustering.ClusterManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dong on 2018-05-21.
 */

public class Mainpage extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{


    LinearLayout MapContainer;
    LinearLayout Hotdeal;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String[] strings = new String[100];
    ImageButton[] imageButtons = new ImageButton[10];
    String[] name = new String[100];
    String[] price = new String[100];
    String[] priced = new String[100];
    double[] lat = new double[100];
    double[] lng = new double[100];
    String[] storename = new String[100];
    String[] storekind = new String[100];
    String[] date = new String[100];

    Double mlat,mlng;
    String temp,now_date;
    TextView time1,time2,time3,time4;
    Button inputmenubtn;
    Button refresh;
    // 오버레이 관리자

    GoogleMap mMap;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        Intent intent = getIntent();

        mlat = intent.getDoubleExtra("mlat",0.0);
        mlng = intent.getDoubleExtra("mlng",0.0);

        for(int i=0;i<100;i++){
            lat[i] = intent.getDoubleExtra("lat["+i+"]",0.0);
            lng[i] = intent.getDoubleExtra("lng["+i+"]",0.0);
            storename[i] = intent.getStringExtra("storename["+i+"]");
            storekind[i] = intent.getStringExtra("storekind["+i+"]");
        }
        Log.v("알림", "현재 lat "+mlat);
        Log.v("알림", "현재 lng "+mlng);

//
//        for(int i=0;i<100;i++){
//            strings[i] = "";
//            name[i]="";
//            price[i] = "";
//            priced[i]="";
//            date[i]="";
//        }

        Hotdeal = findViewById(R.id.hotdeal);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        MapContainer = (LinearLayout) findViewById(R.id.mapcontainer);
        inputmenubtn = findViewById(R.id.inputmenubtn);
        refresh = findViewById(R.id.refresh);


        inputmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputMenu.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(Mainpage.this,
                        "새로고침을 실행합니다",
                        Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(getApplicationContext(),Mainpage.class);
                intent.putExtra("mlat",mlat);
                intent.putExtra("mlng",mlng);
                for(int i=0;i<100;i++){
                    intent.putExtra("lat["+i+"]",lat[i]);
                    intent.putExtra("lng["+i+"]",lng[i]);
                    intent.putExtra("storename["+i+"]",storename[i]);
                    intent.putExtra("storekind["+i+"]",storekind[i]);
                }
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance(); // 칼렌다 변수
        int year = calendar.get(Calendar.YEAR); // 년
        int month = calendar.get(Calendar.MONTH) + 1; // 월
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 일
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 시
        int minute = calendar.get(Calendar.MINUTE); // 분
        int second = calendar.get(Calendar.SECOND); // 초
        String month_s = month+"";
        String day_s = day+"";
        String hour_s = hour+"";
        String minute_s = minute+"";
        if(month<10){month_s = "0"+month;}
        if(day<10){day_s = "0"+day;}
        if(hour<10){hour_s = "0"+hour;}
        if(minute<10){minute_s = "0"+minute;}
        now_date = year+month_s+day_s+hour_s+minute_s;

        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar calendar = Calendar.getInstance(); // 칼렌다 변수
                            int year = calendar.get(Calendar.YEAR); // 년
                            int month = calendar.get(Calendar.MONTH) +1; // 월
                            int day = calendar.get(Calendar.DAY_OF_MONTH); // 일
                            int hour = calendar.get(Calendar.HOUR_OF_DAY); // 시
                            int minute = calendar.get(Calendar.MINUTE); // 분
                            int second = calendar.get(Calendar.SECOND); // 초
                            String month_s = month+"";
                            String day_s = day+"";
                            String hour_s = hour+"";
                            String minute_s = minute+"";
                            String second_s = second+"";
                            if(month<9){month_s = "0"+month;}
                            if(day<10){day_s = "0"+day;}
                            if(hour<10){hour_s = "0"+hour;}
                            if(minute<10){minute_s = "0"+minute;}
                            if(second<10){second_s = "0"+second;}
                            now_date = year+month_s+day_s+hour_s+minute_s;
                            time1.setText(month+"월 "+day+"일");
                            time2.setText(hour_s);
                            time3.setText(minute_s);
                            time4.setText(second_s);
                        }
                    });
                    try {
                        Thread.sleep(1000); // 1000 ms = 1초
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        ///////////////////////////////////////////////////////////  HOT DEAL 시계 구현




        for(int i=0;i<10;i++){
            imageButtons[i] = new ImageButton(this);
        }


        databaseReference.child("food").orderByChild("date").addChildEventListener(new ChildEventListener() {

            int k =0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                long a = Long.parseLong(dataSnapshot.child("date").getValue().toString());
                long b = Long.parseLong(now_date);
                if((a>b)&&k<100){
                    strings[k] = dataSnapshot.child("title").getValue().toString();
                    name[k] = dataSnapshot.child("foodName").getValue().toString();
                    date[k] = dataSnapshot.child("date").getValue().toString();
                    lat[k] = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                    lng[k] = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                    storekind[k] = dataSnapshot.child("storekind").getValue().toString();
                    storename[k] = dataSnapshot.child("storeName").getValue().toString();
                    priced[k] = dataSnapshot.child("price").getValue().toString();
                    price[k] = dataSnapshot.child("saledprice").getValue().toString();
                    //Log.v("알림", k+"date "+dataSnapshot.child("date").getValue());
                    apply(dataSnapshot.child("title").getValue().toString(),k);
                    k++;
                }
                else if(a<b){
                    dataSnapshot.getRef().removeValue();
                    delete(dataSnapshot.child("title").getValue().toString());
                    Log.v("알림", "Value 삭제"); //휘발성 database 구현
                }
                else{}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        for(int i=0;i<10;i++){  //HOT DEAL 상품은 오직 10개만 만든다.
            Hotdeal.addView(imageButtons[i]);
            final int temp = i;
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailmenu = new Intent(getApplicationContext(), MenuDetails.class);
                    detailmenu.putExtra("strings",strings[temp]);
                    detailmenu.putExtra("date",date[temp]);
                    detailmenu.putExtra("name",name[temp]);
                    detailmenu.putExtra("price",price[temp]);
                    detailmenu.putExtra("priced",priced[temp]);
                    detailmenu.putExtra("storename",storename[temp]);
                    detailmenu.putExtra("storekind",storekind[temp]);
                    detailmenu.putExtra("lat",lat[temp]);
                    detailmenu.putExtra("lng",lng[temp]);
                    startActivity(detailmenu);
                }
            });
        }

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void apply(String a,int y){ // Glide를 통하여 제품의 사진을 불러온다
        final int p=y;
        storageReference = FirebaseStorage.getInstance().getReference().child("photo/"+a);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                temp = uri.toString();
                Glide.with(getBaseContext()).load(temp).centerCrop().override(300,300).placeholder(R.drawable.placeholder).into(imageButtons[p]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void delete(String b){
        FirebaseStorage.getInstance().getReference().child("photo/"+b).delete(); // 휘발성 Storage 구현
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng mylocation = new LatLng(mlat,mlng);
        markerOptions.position(mylocation);
        mMap.addMarker(markerOptions.title("현재위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,16));
        mMap.setOnInfoWindowClickListener(this);


        for(int i = 0; i< 100; i++){
            LatLng addlocation = new LatLng(lat[i],lng[i]);
            mMap.addMarker(new MarkerOptions().position(addlocation).title(storekind[i]+" / "+storename[i]));
        }


    }
    public double getLat(){
        return mlat;
    }
    public double getLng(){
        return mlng;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int temp = Integer.parseInt(marker.getId().substring(1));
        String store = marker.getTitle();
        if(temp>0) {
            temp--;
            Intent intent = new Intent(getApplicationContext(), MenuDetails.class);
            Intent detailmenu = new Intent(getApplicationContext(), MenuDetails.class);
            detailmenu.putExtra("strings",strings[temp]);
            detailmenu.putExtra("store",store);
            detailmenu.putExtra("date",date[temp]);
            detailmenu.putExtra("name",name[temp]);
            detailmenu.putExtra("price",price[temp]);
            detailmenu.putExtra("priced",priced[temp]);
            detailmenu.putExtra("storename",storename[temp]);
            detailmenu.putExtra("storekind",storekind[temp]);
            detailmenu.putExtra("lat",lat[temp]);
            detailmenu.putExtra("lng",lng[temp]);
            startActivity(detailmenu);
        }
    }

}
/////////////////////////////////////////////////////// 구글맵 사용하는 코드
