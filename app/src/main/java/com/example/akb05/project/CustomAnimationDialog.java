package com.example.akb05.project;

import android.app.Dialog;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by akb05 on 2018-05-17.
 */

public class CustomAnimationDialog extends Activity {
    private ImageView imageView;

    private CustomAnimationDialog customAnimationDialog;
    private ImageView imgAndroid;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        imageView = (ImageView) findViewById(R.id.img_android);

        initView();
        Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.loading);
        a.setDuration(3000);
        /*a.setInterpolator(new Interpolator() {
            private final int frameCount = 8;

            @Override
            public float getInterpolation(float input) {
                return (float)Math.floor(input*frameCount)/frameCount;
            }
        });*/
        imageView.startAnimation(a);
        startLoading();
    }

    private void initView() {

        imgAndroid = (ImageView)findViewById(R.id.img_android);
        anim = AnimationUtils.loadAnimation(this,R.anim.loading);
        imgAndroid.setAnimation(anim);
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}

