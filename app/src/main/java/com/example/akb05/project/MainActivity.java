package com.example.akb05.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    static boolean admin;
    static int count;
    private final String TAG = getClass().getSimpleName();
    EditText etid,etpw;
    ImageView login,create;
    TextView tv;
    double mlat,mlng;
    String now_date;
    double[] lat = new double[100];
    double[] lng = new double[100];
    String[] storename = new String[100];
    String[] storekind = new String[100];

    private GpsInfo gps;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admin = false;
        count = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().hide();

        Intent intent = new Intent(MainActivity.this, CustomAnimationDialog.class);
        startActivity(intent);

        // initView();
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission] ")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        Calendar calendar = Calendar.getInstance(); // 칼렌다 변수
        int year = calendar.get(Calendar.YEAR); // 년
        int month = calendar.get(Calendar.MONTH) + 1; // 월
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 일
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 시
        int minute = calendar.get(Calendar.MINUTE); // 분
        int second = calendar.get(Calendar.SECOND); // 초
        String month_s = month + "";
        String day_s = day + "";
        String hour_s = hour + "";
        String minute_s = minute + "";
        if (month < 10) {
            month_s = "0" + month;
        }
        if (day < 10) {
            day_s = "0" + day;
        }
        if (hour < 10) {
            hour_s = "0" + hour;
        }
        if (minute < 10) {
            minute_s = "0" + minute;
        }
        now_date = year + month_s + day_s + hour_s + minute_s;

        databaseReference.child("food").orderByChild("date").addChildEventListener(new ChildEventListener() {

            int k = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                long a = Long.parseLong(dataSnapshot.child("date").getValue().toString());
                long b = Long.parseLong(now_date);
                if ((a > b) && k < 100) {
                    lat[k] = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                    lng[k] = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                    storekind[k] = dataSnapshot.child("storekind").getValue().toString();
                    storename[k] = dataSnapshot.child("storeName").getValue().toString();
                    k++;
                }
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

        etid = findViewById(R.id.etid);
        etpw = findViewById(R.id.etpw);
        login = findViewById(R.id.login);
        create = findViewById(R.id.create);
        tv = findViewById(R.id.adminlogin);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
        } else {
            Log.d(TAG, "Log out State");
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "로그인 중입니다...",
                        Toast.LENGTH_SHORT).show();

                final String email = etid.getText().toString().trim();
                final String passwd = etpw.getText().toString().trim();

                gps = new GpsInfo(MainActivity.this);
                if (gps.isGetLocation()) {
                    final double latitude = gps.getLatitude();
                    final double longitude = gps.getLongitude();
                    mlat = latitude;
                    mlng = longitude;
                    Log.v("알림", "aa:" + String.valueOf(mlat));
                    Log.v("알림", "bb:" + String.valueOf(mlng));
                }

                Log.d(TAG, "Email:" + email + " Password:" + passwd);
                //이메일과 비밀번호를 확인하는 부분
                if (isValidEmail(email) && isValidPasswd(passwd)) {
                    mAuth.signInWithEmailAndPassword(email, passwd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), Mainpage.class);
                                        intent.putExtra("mlat", mlat);
                                        intent.putExtra("mlng", mlng);
                                        for (int i = 0; i < 100; i++) {
                                            intent.putExtra("lat[" + i + "]", lat[i]);
                                            intent.putExtra("lng[" + i + "]", lng[i]);
                                            intent.putExtra("storename[" + i + "]", storename[i]);
                                            intent.putExtra("storekind[" + i + "]", storekind[i]);
                                        }

                                        startActivity(intent);
                                        Toast.makeText(MainActivity.this,
                                                "로그인 성공",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "로그인 실패",
                                                Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this,
                            "Check Email or Password",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String checkadmin = etid.getText().toString().trim();
                Log.v("알림","관리자 모드 클릭 :: "+count+" :: "+checkadmin);
                count++;
                if (count == 5&&checkadmin.equals("1636")) {
                    count = 0;
                    admin = true;
                    gps = new GpsInfo(MainActivity.this);
                    if (gps.isGetLocation()) {
                        final double latitude = gps.getLatitude();
                        final double longitude = gps.getLongitude();
                        mlat = latitude;
                        mlng = longitude;
                    }
                    Intent intent = new Intent(getApplicationContext(), Mainpage.class);
                    intent.putExtra("mlat", mlat);
                    intent.putExtra("mlng", mlng);
                    for (int i = 0; i < 100; i++) {
                        intent.putExtra("lat[" + i + "]", lat[i]);
                        intent.putExtra("lng[" + i + "]", lng[i]);
                        intent.putExtra("storename[" + i + "]", storename[i]);
                        intent.putExtra("storekind[" + i + "]", storekind[i]);
                    }
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isValidPasswd(String str){
            if(str == null || TextUtils.isEmpty(str)){
                return false;
            } else {
                if(str.length() >= 6)
                    return true;
                else
                    return false;
            }
    }
    //Email확인
    private boolean isValidEmail(String str){
        if(str == null || TextUtils.isEmpty(str)){
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(str).matches();
        }
    }




}

