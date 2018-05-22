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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    EditText etid,etpw;
    Button login,create;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, CustomAnimationDialog.class);
        startActivity(intent);

       // initView();

        etid = findViewById(R.id.etid);
        etpw = findViewById(R.id.etpw);
        login = findViewById(R.id.login);
        create = findViewById(R.id.create);

        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
        } else {
            Log.d(TAG, "Log out State");
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etid.getText().toString().trim();
                String passwd = etpw.getText().toString().trim();
                Log.d(TAG, "Email:" + email + " Password:" + passwd);
                //이메일과 비밀번호를 확인하는 부분
                if(isValidEmail(email) && isValidPasswd(passwd)){
                    signinAccount(email, passwd);
                    // 로그인 클릭 동시에 메인메뉴로 이동
                    Intent mainintent = new Intent(getApplicationContext(), Mainpage.class);
                    startActivity(mainintent);
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
                Intent intent = new Intent(getApplicationContext(),CreateAccount.class);
                startActivity(intent);
//                String email = etid.getText().toString().trim();
//                String passwd = etpw.getText().toString().trim();
//                Log.d(TAG, "Email:" + email + " Password:" + passwd);
//                if(isValidEmail(email) && isValidPasswd(passwd)){
//                    createAccount(email, passwd);
//                } else {
//                    Toast.makeText(MainActivity.this,
//                            "Check Email or Password",
//                            Toast.LENGTH_LONG).show();
//                }
            }
        });
    }



    private boolean isValidPasswd(String str){
            if(str == null || TextUtils.isEmpty(str)){
                return false;
            } else {
                if(str.length() > 4)
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
//    private void createAccount(String email, String passwd){
//
//        mAuth.createUserWithEmailAndPassword(email, passwd)
//                .addOnCompleteListener(this,
//                        new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                Log.d(TAG, "Create Account:" + task.isSuccessful());
//                                if(task.isSuccessful()){
//                                    Log.d(TAG, "Account Create Complete");
//                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
//
//                                }else {
//                                    Toast.makeText(MainActivity.this,
//                                            "Create Account Failed", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//
//    }
    private void signinAccount(String email, String passwd){

        //로그내용
        mAuth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Sing in Account:" + task.isSuccessful());
                                if(task.isSuccessful()){
                                    Log.d(TAG, "Account Log in  Complete");
                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
                                    // Go go Main
                                }else {
                                    Toast.makeText(MainActivity.this,
                                            "Log In Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

    }



}

