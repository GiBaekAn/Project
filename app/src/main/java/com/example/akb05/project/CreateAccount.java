package com.example.akb05.project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by dong on 2018-05-22.
 */

public class CreateAccount extends AppCompatActivity {

    EditText createid;
    EditText createpw;
    EditText checkpw;
    ImageView createbtn;
    ImageView cancelbtn;

    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth mAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        createid = findViewById(R.id.createid);
        createpw = findViewById(R.id.createpw);
        createbtn = findViewById(R.id.createbtn);
        cancelbtn = findViewById(R.id.cancelbtn);
        checkpw = findViewById(R.id.checkpw);

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = createid.getText().toString().trim();
                String passwd = createpw.getText().toString().trim();
                String checkpasswd = checkpw.getText().toString().trim();
                Log.d(TAG, "Email:" + email + " Password:" + passwd);

                if(isValidEmail(email) && isValidPasswd(passwd)){
                    createAccount(email, passwd);
                } else {
                    Toast.makeText(CreateAccount.this,
                            "Check Email or Password",
                            Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        checkpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = createpw.getText().toString();
                String confirm = checkpw.getText().toString();

                if( password.equals(confirm) ) {
                    createpw.setTextColor(Color.BLACK);
                    checkpw.setTextColor(Color.BLACK);
                } else {
                    createpw.setTextColor(Color.RED);
                    checkpw.setTextColor(Color.RED);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    boolean isValidPasswd(String str){
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

    private void createAccount(String email, String passwd){

        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Create Account:" + task.isSuccessful());
                                if(task.isSuccessful()){
                                    Toast.makeText(CreateAccount.this,
                                            "ID 생성이 완료되었습니다.",
                                            Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "Account Create Complete");
                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());

                                }else {
                                    Toast.makeText(CreateAccount.this,
                                            "Create Account Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

    }



}