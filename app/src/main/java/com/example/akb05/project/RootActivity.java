package com.example.akb05.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by akb05 on 2018-05-31.
 */
public class RootActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_root);
//
//        btnStorage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RootActivity.this, StorageActivity.class);
//                startActivityForResult(intent , 101);
//            }
//        });


        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
            // Go to Main Page
            //tvUser.setText("USER:" + mAuth.getCurrentUser().getEmail());
        } else {
            Log.d(TAG, "Log out State");
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Logout(){
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public void onBackPressed() {
        Logout();
        finish();
    }
}


