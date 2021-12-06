package com.venumadhav.mumo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.color.DynamicColors;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.app.SpotifyAuthHandler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.ak1.BubbleTabBar;
import io.ak1.OnBubbleClickListener;

public class MainActivity2 extends AppCompatActivity {
    static int pos=0;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabasereference;
    public static BubbleTabBar bubbleTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#F5F7DC"));
        //Menu menu = new
        DynamicColors.applyIfAvailable(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try  {
//                    //Your code goes here
//                    try {
//                        URL url = new URL("https://api.spotify.com/v1/me/");
//                        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                        Toast.makeText(MainActivity2.this, in+"", Toast.LENGTH_SHORT).show();
//                        //readStream(in);
//                    }catch(Exception e){
//                        Toast.makeText(MainActivity2.this, e+"", Toast.LENGTH_SHORT).show();
//                    } finally {
//                        // urlConnection.disconnect();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();

        //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();











        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        bubbleTabBar = findViewById(R.id.bubbleTabBar);
//        Fragment m = new mood();
//        Fragment mu = new musiclist();
//        Fragment u = new userprofile();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view,m).commit();
//        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.music, new Fragment(),"TAG_A");
//        fragmentTransaction.add(R.id.profile, new Fragment(), "TAG_B");
//        fragmentTransaction.commit();
//        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
//            @Override
//            public void onBubbleClick(int i) {
//                 //Toast.makeText(MainActivity2.this, i+"", Toast.LENGTH_SHORT).show();
//                 if(i==R.id.home){
//                     Toast.makeText(MainActivity2.this, "HOME-CLICKED", Toast.LENGTH_SHORT).show();
////                     Fragment fragA = (Fragment) getSupportFragmentManager().findFragmentByTag("TAG_A");
////                     FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                     fragmentTransaction.detach(getSupportFragmentManager().findFragmentByTag("TAG_B"));
////                     fragmentTransaction.attach(fragA);
////                     fragmentTransaction.addToBackStack(null);
////
////                     fragmentTransaction.commitAllowingStateLoss();
////                     getSupportFragmentManager().executePendingTransactions();
//
//                     fragmentManager
//                             .beginTransaction()
//
//                             .replace(R.id.fragment_container_view, m)
//
//                             .addToBackStack("mood") //if you want to keep transaction to backstack
//                             .commit();
//                 }
//                 if(i==R.id.music){
//                     fragmentManager
//                             .beginTransaction()
//
//                             .replace(R.id.fragment_container_view, mu)
////addToBackstack(String name) //if you want to keep transaction to backstack
//                             .addToBackStack("music")
//                             .commit();
//                 }
//                 if(i==R.id.profile){
//                     fragmentManager
//                             .beginTransaction()
//
//
//
//                             .addToBackStack("user")
////addToBackstack(String name) //if you want to keep transaction to backstack
//                             .commit();
//                 }
//
//
//
//
//            }
//        });
        ;
       // static int pos=0;
        pager2 = findViewById(R.id.view_pager2);
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm,getLifecycle());
        pager2.setAdapter(adapter);
        pager2.setCurrentItem(1);
        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                if(i==R.id.home){
                    pager2.setCurrentItem(1);
                    pos = i;
                }
                if(i==R.id.music){
                    pos = i;
                    pager2.setCurrentItem(0);
                }
                if(i==R.id.profile){
                    pos = i;
                    pager2.setCurrentItem(2);
                }

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position==0){

                    bubbleTabBar.setSelected(position,false);
                }
                if(position==1){
                    bubbleTabBar.setSelected(position,false);
                }
                if(position==2){
                    bubbleTabBar.setSelected(position,false);
                }
//                Toast.makeText(MainActivity2.this, position+"", Toast.LENGTH_SHORT).show();
            }
        });




    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        // mSpotifyAppRemote.getPlayerApi().resume();
    }



    @Override
    protected void onStop() {
        super.onStop();
        //SpotifyAppRemote.disconnect(mSpotifyAppRemote);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //mSpotifyAppRemote.getPlayerApi().resume();

    }

    public void hidebar(){
        bubbleTabBar.setVisibility(BubbleTabBar.INVISIBLE);
    }

    public static String getGetmobileno() {
        Intent i = new Intent();
        String mobileno = i.getStringExtra("mobileno");
        return mobileno;
    }




}

