package com.example.akb05.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
    private String mCurrentPhotoPath;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Uri imgUri,photoURI;
    private FirebaseAuth mAuth;

    ImageButton inputimg;
    CalendarView cView;
    TimePicker tPicker;
    Spinner spinner;
    EditText et_foodname, et_saledprice, et_price, et_storename;
    Button btn;
    int year;
    int month;
    int day;
    int flag=0;
    public static String select ="";

    private String absoultePath;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();



    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.inputmenu);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(InputMenu.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(InputMenu.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission] ")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
                .check();


        inputimg = findViewById(R.id.inputimg);
    cView = findViewById(R.id.cv);
    tPicker = findViewById(R.id.tp);
    spinner = findViewById(R.id.spinner);
    et_foodname = findViewById(R.id.name);
    et_saledprice = findViewById(R.id.saledprice);
    et_price = findViewById(R.id.price);
    et_storename = findViewById(R.id.storename);
    btn = findViewById(R.id.button);

    final String[] nameofstore = {"GS25","미니스톱","CU","세븐일레븐","With me","기타"};
    final ArrayAdapter<String> adapter;
    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nameofstore);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    //편의점 종류를 선택하는 spinner

    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(cView.getDate());
    year = c.get(Calendar.YEAR);
    month = c.get(Calendar.MONTH) +1;
    day = c.get(Calendar.DAY_OF_MONTH);
    // 현재시간을 각 변수에 저장

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

            //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            UploadTask uploadTask;
            StorageReference riversRef = storageRef.child("photo/"+photoURI.getLastPathSegment());
            uploadTask = riversRef.putFile(photoURI);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });


            String foodname = et_foodname.getText().toString().trim();
            String saledprice = et_saledprice.getText().toString().trim();
            String price = et_price.getText().toString().trim();
            select = spinner.getSelectedItem().toString();
            String storename = et_storename.getText().toString().trim();


            int hour = tPicker.getHour();
            int minute = tPicker.getMinute();


            foodDTO food = new foodDTO(foodname,saledprice,price,year,month,day,hour,minute,37.279940,127.043867,select,storename);
            databaseReference.child("food").push().setValue(food);
            Toast.makeText(InputMenu.this,
                    "등록되었습니다.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    });
    // 등록 버튼을 눌렀을 경우 등록

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
    public void takePhoto(){

        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(photoFile!=null){
                    Uri providerURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    imgUri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }
        }else{
            Log.v("알림", "저장공간에 접근 불가능");
            return;
        }
    }

    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile= null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");
        if(!storageDir.exists()){
            //없으면 만들기
            Log.v("알림","storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림","storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir,imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
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








}

