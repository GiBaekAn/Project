package com.example.akb05.project;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dong on 2018-05-21.
 */

public class InputMenu extends Activity implements View.OnClickListener {
    private LocationManager locationManager;
    private LocationListener locationListener;


    private String mCurrentPhotoPath;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Uri imgUri=null,photoURI=null;
    private FirebaseAuth mAuth;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = true;

    private GpsInfo gps;


    ImageButton inputimg;
    CalendarView cView;
    TimePicker tPicker;
    Spinner spinner;
    TextView t1,t2;
    EditText et_foodname, et_saledprice, et_price, et_storename;
    Button btn,btn2;
    int year;
    int month;
    int day;
    double lat;
    double lng;
    boolean photo = false;
    public static String select ="";

    private String absoultePath;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputmenu);

        t1 = findViewById(R.id.textView12);
        t2 = findViewById(R.id.textView14);
        inputimg = findViewById(R.id.inputimg);
        btn2 = findViewById(R.id.button2);
        cView = findViewById(R.id.cv);
        tPicker = findViewById(R.id.tp);
        spinner = findViewById(R.id.spinner);
        et_foodname = findViewById(R.id.name);
        et_saledprice = findViewById(R.id.saledprice);
        et_price = findViewById(R.id.price);
        et_storename = findViewById(R.id.storename);
        btn = findViewById(R.id.button);

        final String[] nameofstore = {"GS25", "미니스톱", "CU", "세븐일레븐", "With me", "기타"};
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameofstore);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //편의점 종류를 선택하는 spinner

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(cView.getDate());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        // 현재시간을 각 변수에 저장


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPermission) {
                    return;
                }
                gps = new GpsInfo(InputMenu.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    lat = latitude;
                    lng = longitude;
                    String a1 = Double.toString(lat);
                    String a2 = Double.toString(lng);
                    t1.setText(a1);
                    t2.setText(a2);

                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "가져오기 실패 \n ",
                            Toast.LENGTH_LONG).show();
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();

                }

            }
        });




    cView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
            year = i;
            month = i1+1;
            day = i2;
        }
    });
    // 캘린더 뷰의 변화가 있을 경우 각 변수에 선택된 값들을 저장


    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            UploadTask uploadTask;
            if(photoURI!=null){
                StorageReference riversRef = storageRef.child("photo/"+photoURI.getLastPathSegment());
                photo = true;
                uploadTask = riversRef.putFile(photoURI);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });
            }
            else{
                photo = false;
            }

            String foodname = et_foodname.getText().toString().trim();
            String saledprice = et_saledprice.getText().toString().trim();
            String price = et_price.getText().toString().trim();
            select = spinner.getSelectedItem().toString();
            String storename = et_storename.getText().toString().trim();
            int hour = tPicker.getHour();
            int minute = tPicker.getMinute();
            String month_s = month+"";
            String day_s = day+"";
            String hour_s = hour+"";
            String minute_s = minute+"";
            if(month<10){month_s = "0"+month;}
            if(day<10){day_s = "0"+day;}
            if(hour<10){hour_s = "0"+hour;}
            if(minute<10){minute_s = "0"+minute;}
            String date = year+month_s+day_s+hour_s+minute_s;

            if(foodname.equals("")||saledprice.equals("")||price.equals("")||year==0||month==0||day==0||hour==0||minute==0||lat==0.0||lng==0.0||storename.equals("")||!photo){
                Toast.makeText(InputMenu.this,
                        "빈 항목이 있는지 확인해주세요.",
                        Toast.LENGTH_LONG).show();
            }
            else{

                String title = photoURI.getLastPathSegment();
                foodDTO food = new foodDTO(title,foodname,saledprice,price,date,lat,lng,select,storename);
                databaseReference.child("food").push().setValue(food);
                Toast.makeText(InputMenu.this,
                        "등록되었습니다.",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InputMenu.this, Mainpage.class);
                startActivity(intent);
                finish();
            }
        }
    });
    // 등록 버튼을 눌렀을 경우 등록
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case FROM_ALBUM : {
                //앨범에서 가져오기
                if(data.getData()!=null){
                    try{
                        Log.v("알림", "FROM_ALBUM 처리");
                        photoURI = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        inputimg.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
            case FROM_CAMERA : {
                //카메라 촬영
                try{
                    Log.v("알림", "FROM_CAMERA 처리");
                    galleryAddPic();
                    inputimg.setImageURI(imgUri);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void onClick(View v){
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                takePhoto();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectAlbum();
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진 촬영",cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소",cancelListener)
                .show();
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + "jpg";
        photoURI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, FROM_CAMERA);
    }

    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
    }
    public void selectAlbum(){
        //앨범에서 이미지 가져옴
        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);
    }









}

