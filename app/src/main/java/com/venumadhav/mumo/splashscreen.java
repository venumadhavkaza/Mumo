package com.venumadhav.mumo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class splashscreen extends AppCompatActivity {

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;
    int delay = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        // Theme.Mumo is a NoActionBar theme and already sets the status bar color,
        // so there's nothing to hide/override here.
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
