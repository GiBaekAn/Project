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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by dong on 2018-05-21.
 */

public class InputMenu extends Activity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    ImageButton inputimg;
    CalendarView cView;
    TimePicker tPicker;
    EditText et_foodname, et_saledprice, et_price, et_storename;
    Button btn;

    private String absoultePath;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.inputmenu);

    inputimg = findViewById(R.id.inputimg);
    cView = findViewById(R.id.cv);
    tPicker = findViewById(R.id.tp);

    et_foodname = findViewById(R.id.name);
    et_saledprice = findViewById(R.id.saledprice);
    et_price = findViewById(R.id.price);
    et_storename = findViewById(R.id.storename);
    btn = findViewById(R.id.button);


    final long a=1;

    final String[] nameofstore = {"GS25","미니스톱","CU","세븐일레븐","With me","기타"};
    Spinner spinner = findViewById(R.id.spinner);
    ArrayAdapter<String> adapter;
    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,nameofstore);
    spinner.setAdapter(adapter);



    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String foodname = et_foodname.getText().toString().trim();
            String saledprice = et_saledprice.getText().toString().trim();
            String price = et_price.getText().toString().trim();
            String storename = et_storename.getText().toString().trim();
            foodDTO food = new foodDTO(foodname,saledprice,price,a,a,a,storename);
            databaseReference.child("food").push().setValue(food);
            Toast.makeText(InputMenu.this,
                    "등록되었습니다.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    });
    }

    public void doTakePhotoAction(){ // 카메라 촬영 후 이미지 가져오기
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //임시로 사용할 파일의 경로 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis() + ".jpg");
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction(){ // 앨범에서 이미지 가져오기
        //앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode)
        {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA:{
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE);
                break;
            }

            case CROP_FROM_iMAGE:{
                if(resultCode != RESULT_OK){
                    return;
                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        "/SmartWheel/"+System.currentTimeMillis()+".jpg";

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                    inputimg.setImageBitmap(photo);

                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }
                //임시파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }

            }
        }
    }

    public void onClick(View v){
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doTakePhotoAction();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doTakeAlbumAction();
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

    //비트맵을 저장하는 부분
    private void storeCropImage(Bitmap bitmap, String filePath){
        //SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            //sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

