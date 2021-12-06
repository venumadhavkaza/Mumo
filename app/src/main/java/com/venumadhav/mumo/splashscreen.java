package com.venumadhav.mumo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

public class splashscreen extends AppCompatActivity {

    Handler handler  = new Handler();
    Runnable runnable;
    int delay = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#F5F7DC"));
        //Menu menu = new
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.postDelayed(runnable =new Runnable()
        {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                Intent i = new Intent(splashscreen.this,MainActivity.class);
                startActivity(i);
                finish();
                handler.removeCallbacks(runnable);
            }
        },delay);

    }
}
