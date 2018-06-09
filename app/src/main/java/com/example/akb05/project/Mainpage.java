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
import com.google.android.gms.maps.model.LatLng;
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

public class Mainpage extends FragmentActivity implements OnMapReadyCallback{

    LinearLayout MapContainer;
    LinearLayout Hotdeal;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    DatabaseReference databaseReference;

    // Uri a = Uri.parse(storageReference.toString().trim());


    String[] strings = new String[5];
    ImageButton[] imageButtons = new ImageButton[5];
    String temp,now_date;
    ImageButton menuimg;
    TextView time1,time2,time3,time4;
    Button inputmenubtn;
    // 오버레이 관리자

    GoogleMap mMap;


    ///////////////////////////////////////////////////////////  HOT DEAL 시계 구현
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        for(int i=0;i<5;i++){
            strings[i] = "";
        }

        Hotdeal = findViewById(R.id.hotdeal);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        MapContainer = (LinearLayout) findViewById(R.id.mapcontainer);
        inputmenubtn = findViewById(R.id.inputmenubtn);


        inputmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputMenu.class);
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
                            int month = calendar.get(Calendar.MONTH); // 월
                            int day = calendar.get(Calendar.DAY_OF_MONTH); // 일
                            int hour = calendar.get(Calendar.HOUR_OF_DAY); // 시
                            int minute = calendar.get(Calendar.MINUTE); // 분
                            int second = calendar.get(Calendar.SECOND); // 초
                            String month_s = month+"";
                            String day_s = day+"";
                            String hour_s = hour+"";
                            String minute_s = minute+"";
                            String second_s = second+"";
                            if(month<10){month_s = "0"+month;}
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

        Log.v("알림", "date는 "+now_date);




        for(int i=0;i<5;i++){
            imageButtons[i] = new ImageButton(this);
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailmenu = new Intent(getApplicationContext(), MenuDetails.class);
                    startActivity(detailmenu);
                }
            });
        }


        //Query query = databaseReference.child("food").child("title").orderByValue().startAt(now_date).limitToFirst(5);

        databaseReference.child("food").addChildEventListener(new ChildEventListener() {

            int k =0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //strings[k++]=dataSnapshot.child("title").getValue().toString();
                Log.v("알림", "strings "+dataSnapshot.child("title").getValue());
                apply(dataSnapshot.child("title").getValue().toString(),k);
                k++;
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


        //Glide.with(this).using(new FirebaseImageLoader()).load(storage.getReferenceFromUrl(uri.toString())).into(test);
        //Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/project-245fb.appspot.com/o/photo%2F1201266192?alt=media&token=df029928-9b2a-4dc4-aa6c-99172d0a2280").into(aa);
        //Glide.with(this).using(new FirebaseImageLoader()).load(storageReference).into(test);
        //SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.aa);
        //draweeView.setImageURI(abc);

        for(int i=0;i<5;i++){
            Hotdeal.addView(imageButtons[i]);
        }



        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void apply(String a,int y){

        final int p=y;
        storageReference = FirebaseStorage.getInstance().getReference().child("photo/"+a);
        Log.v("알림", "string ::  " + a);
        Log.v("알림", "int ::  " + y);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                temp = uri.toString();
                Glide.with(getBaseContext()).load(temp).override(300,300).into(imageButtons[p]);
                //Glide.with(getBaseContext()).load(temp).override(300,300).into(imageButtons[i]);
                Log.v("알림", "This is temp ::  " + uri.toString());
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png");
        //storageReference = storage.getReference().child("photo/21365.png");
        //Uri abc = Uri.parse("https://firebasestorage.googleapis.com/v0/b/project-245fb.appspot.com/o/photo%2F21365?alt=media&token=fd4dc11b-10e1-4aea-ae51-425932584d31");


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
/////////////////////////////////////////////////////// 구글맵 사용하는 코드
