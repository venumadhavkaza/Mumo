package com.venumadhav.mumo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.color.DynamicColors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.internal.SpotifyLocator;
import com.spotify.protocol.client.ErrorCallback;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.PlaybackPosition;
import com.spotify.protocol.types.PlayerState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;
import com.spotify.protocol.types.UserStatus;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link musiclist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class musiclist extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public  static SpotifyAppRemote mSpotifyAppRemote;
    Handler handler  = new Handler();
    Runnable runnable;
    private static final String CLIENT_ID = "100d90984cc142febb47cdc0994d4e1a";
    private static final String REDIRECT_URI = "http://com.venumadhav.mumo/callback";
    private static CircleImageView playerpic;
    private static TextView trackname;
    private static TextView artist;
    private static Button playpause;
    private static Button seekforward10;
    private static Button seekback10;
    private static Button seeknext;
    private static Button seekback;
    private static SeekBar seekBar;
    private Button viewinSpotify;
    private TextView runtime;
    private TextView runningtime;
    public static Track track;
    public static boolean ispause;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mumo-91024-default-rtdb.firebaseio.com/");

    public static HashMap<String,Integer> imurimap = new HashMap<>();



    //private SpotifyAppRemote mSpotifyAppRemote;
    Context thiscontext;
    // TODO: Rename and change types of parameters

    public musiclist() {
        // Required empty public constructor
    }
    public static musiclist newInstance(String param1, String param2) {
        musiclist fragment = new musiclist();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //thiscontext = container.getContext();
        return inflater.inflate(R.layout.fragment_music, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerpic = view.findViewById(R.id.playerpic);
        trackname = view.findViewById(R.id.trackname);
        artist = view.findViewById(R.id.artist);
        playpause = view.findViewById(R.id.btnplaypause);
        seekforward10 = view.findViewById(R.id.btnforward);
        seekback10 = view.findViewById(R.id.btnrewind);
        seekback = view.findViewById(R.id.btnprevious);
        seeknext = view.findViewById(R.id.btnnext);
        seekBar = view.findViewById(R.id.seekbar);
        runtime = view.findViewById(R.id.runtime);
        runningtime = view.findViewById(R.id.runningtime);
        DynamicColors.applyIfAvailable(getActivity());
        viewinSpotify = view.findViewById(R.id.spotify_btn);


        buttonEffect(playpause);
        buttonEffect(seekforward10);
        buttonEffect(seekback10);
        buttonEffect(seekforward10);
        buttonEffect(seekback);
        buttonEffect(seeknext);


        connectspotify();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
               // Toast.makeText(getContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSpotifyAppRemote.getPlayerApi().seekTo(track.duration*seekBar.getProgress()/100);
                // Toast.makeText(getContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
               // Toast.makeText(getContext(), seekBar.getProgress()+"", Toast.LENGTH_SHORT).show();
            }
        });
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ispause==true){
                    mSpotifyAppRemote.getPlayerApi().resume();
                }
                else{
                 mSpotifyAppRemote.getPlayerApi().pause();
                }
            }
        });

        viewinSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(track.album.uri+""));
                intent.putExtra(Intent.EXTRA_REFERRER,
                        Uri.parse("android-app://" + getContext().getPackageName()));
                startActivity(intent);
            }
        });

        seekforward10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().seekTo(track.duration*(seekBar.getProgress()+5)/100);
            }
        });
        seekback10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().seekTo(track.duration*(seekBar.getProgress()-5)/100);
            }
        });
        seeknext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        });
        seekback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        });
        //TextView tv = view.findViewById(R.id.textView);
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(thiscontext);

        //Uri imuri = Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl();
//        dp.setImageURI(null);
       // Toast.makeText(getContext(), mSpotifyAppRemote.getPlayerApi().getPlayerState()+"", Toast.LENGTH_SHORT).show();

       // mSpotifyAppRemote.getPlayerApi().play("spotify:track:5FXMRdJjKq1BIX4e8Eg9mK");
//


    }
    private void connected(){
       //mSpotifyAppRemote.getPlayerApi().play("spotify:track:1stiSonuKkZqhI1o9nZ9MT");
     //   https://open.spotify.com/track/?si=d59e886647a3401b

    //Picasso.get().load("https://cdn.peekalink.io/public/images/6bfc5d82-9f38-4aaf-8171-d6f1f1c64839/b9a45533-64ba-4150-9d94-553fdef8121f.jpe").into(playerpic);
       try {
           mSpotifyAppRemote.getPlayerApi()
                   .subscribeToPlayerState()
                   .setEventCallback(playerState -> {
                       String img = playerState.track.imageUri.toString();
                       track = playerState.track;

                       if (track != null) {
                           final ImageUri im = track.imageUri;
                          // Log.d("MainActivity2", track.name + " by " + track.artist.name);
                           //Toast.makeText(getContext(), "i.scdn.co/image/"+track.imageUri.toString().substring(22, track.imageUri.toString().length() - 2)+"",
                           // Toast.LENGTH_SHORT).show();

                           if(!imurimap.containsKey(im.toString())){
                               if(!track.name.equals("Advertisement")){
                                   settofirebase("https://i.scdn.co/image/"+track.imageUri.toString().substring(22, track.imageUri.toString().length() - 2),track.name);
                               }

                           Picasso.get().load("https://i.scdn.co/image/"+track.imageUri.toString().substring(22, track.imageUri.toString().length() - 2)).into(playerpic);
                           playerpic.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.layouttransit));
                           imurimap.put(im.toString(),1);

                           }

                           runtime.setText(gettime(track.duration));

                           if(playerState.isPaused==true){
                               //seekBar.setProgress((int)playerState.playbackPosition);
                               ispause=true;
                               playpause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                           }else{
                               //seekbarmethod();
                               //seekBar.setProgress((int)playerState.playbackPosition);
                               ispause=false;
                               playpause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                           }
                           if(!imurimap.containsKey(track.name)&&!imurimap.containsKey(track.artist.name)) {
                               trackname.setText(track.name);
                               String art = track.artist.name;
                               artist.setText(art + "");
                               imurimap.put(track.name,1);
                               imurimap.put(track.artist.name,1);
                           }
                           //Toast.makeText(getContext(), track.artist+ "", Toast.LENGTH_SHORT).show();
                       }
                       //Toa
                       // st.makeText(getContext(), im + "", Toast.LENGTH_SHORT).show();
                       //Toast.makeText(getContext(), (int) track.duration+"", Toast.LENGTH_SHORT).show();
                   });

                   }
       catch(Exception e){
           Toast.makeText(getContext(), e + "", Toast.LENGTH_SHORT).show();
       }
       // mSpotifyAppRemote.getConnectApi().connectIncreaseVolume();
        seekbarmethod();
    }

    private void seekbarmethod(){



        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 500);
                //Intent i = new Intent(getContext(),MainActivity.class);
                //startActivity(i);
                    mSpotifyAppRemote.getPlayerApi()
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                        if(track!=null) {

                                runningtime.setText(gettime(playerState.playbackPosition));
                                seekBar.setProgress((100)*(int)playerState.playbackPosition/(int)playerState.track.duration);
                        }
                    });
                // Toast.makeText(getContext(), playerState.playbackPosition+"", Toast.LENGTH_SHORT).show();
            }
        }, 500);

    }

    private void connectspotify(){


        try {
            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(CLIENT_ID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build();

            SpotifyAppRemote.connect(getContext(), connectionParams,
                    new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("MainActivity", "Connected! Yay!");

                            // Now you can start interacting with App Remote
                            connected();

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("MainActivity", throwable.getMessage(), throwable);

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });


        }
        catch (Exception e){
            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
        }



    }


    public static void playmusicfromchat(String uri) {

            mSpotifyAppRemote.getPlayerApi().play(uri);
            final ImageUri im = track.imageUri;
            //return im.toString();



    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void buttonEffect(View button){

        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.parseColor("#90D0D9"), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public static String gettime(long total){
        long milliseconds = total;

        long minutes
                = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

        // This method uses this formula seconds =
        // (milliseconds / 1000);
        long seconds
                = (TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                % 60);
        if(seconds<10){
            String out = minutes+":0"+seconds;
            return out;
        }
        else{
            String out = minutes + ":" + seconds;
            return out;
        }

    }

    public void settofirebase(String uri,String name) {
        try{
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);


                        databaseReference.child("imguris").child(personGivenName).child(currentTimestamp).child("tracks").setValue(name);
                        databaseReference.child("imguris").child(personGivenName).child(currentTimestamp).child("uri").setValue(uri);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }}catch (Exception e){
            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
        }

    }

}

