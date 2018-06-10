package com.example.akb05.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by dong on 2018-05-21.
 */

public class MenuDetails extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView food, storelogoiv;
    TextView nametv, pricedtv, pricetv, datetv, storenametv, storekindtv;
    double lat, lng;
    Button btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menudetails);
        getSupportActionBar().hide();

        food = findViewById(R.id.food);
        nametv = findViewById(R.id.nametv);
        pricetv = findViewById(R.id.pricetv);
        pricedtv = findViewById(R.id.pricedtv);
        storenametv = findViewById(R.id.storetv);
        storelogoiv = findViewById(R.id.storelogo);
        datetv = findViewById(R.id.datetv);
        btn = findViewById(R.id.mapbtn);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String data = intent.getStringExtra("strings");
        String name = intent.getStringExtra("name");
        String storename = intent.getStringExtra("storename");
        String storekind = intent.getStringExtra("storekind");
        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);
        String price = intent.getStringExtra("price");
        String priced = intent.getStringExtra("priced");
        String date = intent.getStringExtra("date");
        apply(data);



        nametv.setText(name);
        storenametv.setText(storename);
        switch (storekind){
            case "GS25": storelogoiv.setImageResource(R.drawable.gs25); break;
            case "미니스톱":storelogoiv.setImageResource(R.drawable.mini); break;
            case "CU":storelogoiv.setImageResource(R.drawable.cu); break;
            case "세븐일레븐":storelogoiv.setImageResource(R.drawable.seven); break;
            case "With me":storelogoiv.setImageResource(R.drawable.withme); break;
            case "기타":storelogoiv.setImageResource(R.drawable.logo3); break;
        }
        pricetv.setText(price);
        pricedtv.setText(priced);
        datetv.setText(date.substring(0,4)+"년 "+date.substring(4,6)+"월 "+date.substring(6,8)+"일 "+date.substring(8,10)+"시 "+date.substring(10,12)+"분");

        final double a = lat;
        final double b = lng;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(getApplicationContext(), Minimap.class);
                map.putExtra("lat",a);
                map.putExtra("lng",b);
                Log.v("알림", "전달됩니다 ::  " + a);
                Log.v("알림", "인텐트가 ::  " + b);
                startActivity(map);
            }
        });

    }

    public void apply(String data) {
        storageReference = FirebaseStorage.getInstance().getReference().child("photo/" + data);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.v("알림", "this is ::  " + uri.toString());
                Glide.with(getBaseContext()).load(uri.toString()).centerCrop().placeholder(R.drawable.placeholder).into(food);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }



}
