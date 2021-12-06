package com.venumadhav.mumo;

import static com.venumadhav.mumo.network.NetworkRequestUtil.imguri;
import static com.venumadhav.mumo.network.NetworkRequestUtil.post;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;
import com.squareup.picasso.Picasso;
import com.venumadhav.mumo.messages.MessagesAdapter;
import com.venumadhav.mumo.messages.MessagesList;
import com.venumadhav.mumo.network.Dummyclass;
import com.venumadhav.mumo.network.NetworkRequestUtil;
import com.venumadhav.mumo.network.PeekaLinkResponse;
import com.venumadhav.mumo.network.RequestPeekalink;
import com.venumadhav.mumo.recentlyplayed.RecentlyplayedAdapter;
import com.venumadhav.mumo.recentlyplayed.RecentlyplayedList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.ak1.BubbleTabBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mood extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLIENT_ID = "100d90984cc142febb47cdc0994d4e1a";
    private static final String REDIRECT_URI = "http://com.venumadhav.mumo/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String BUNDLE_RECYCLER_LAYOUT = "mood.recentlyplayedrecyler.layout";
    private boolean dataSet = false;
    private FirebaseAuth mAuth;
    private BottomSheetBehavior bottomSheetBehavior;
    private BubbleTabBar bubbleTabBar;
    static String test="haihello";
    static String myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private RecentlyplayedAdapter recentlyplayedAdapter;
    private RecyclerView recentlyplayedrecyler;
    private final List<RecentlyplayedList> recentlyplayedLists = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mumo-91024-default-rtdb.firebaseio.com/");
    private ScrollView scrollView22;
    //private SlideUp slideUp;
    Context thiscontext;
    // TODO: Rename and change types of parameters

    public mood() {
        // Required empty public constructor
    }


    public static mood newInstance(String param1, String param2) {
        mood fragment = new mood();

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

       // thiscontext = container.getContext();
        return inflater.inflate(R.layout.fragment_mood, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        String dwci = "529828707013-jb2bt1bc44g7jolio5n9im7nl4rbqf6f.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(dwci)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        RelativeLayout rellay = view.findViewById(R.id.rellay);
        TextView tv = view.findViewById(R.id.textView);
        GridLayout gl = view.findViewById(R.id.gridLayout);
        rellay.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.layouttransit));
        gl.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.downup));
        DynamicColors.applyIfAvailable(getActivity());
        tv.setText("Good Morning ");
        CardView vh = view.findViewById(R.id.cv1);
        ImageView vhimg = view.findViewById(R.id.vh);
        vh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            vhimg.setImageResource(R.drawable.smiley);
                            v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                            vhimg.setColorFilter(Color.parseColor("#FFFF00"),PorterDuff.Mode.MULTIPLY);

            }
        });

        ImageView dp = view.findViewById(R.id.imageView);
        ImageView dp2 = view.findViewById(R.id.imageView2);
        TextView namepop = view.findViewById(R.id.textView7);
        TextView emailpop = view.findViewById(R.id.textView6);
        //TextView tv = view.findViewById(R.id.textView);
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(thiscontext);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            myemail = personGivenName;
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Picasso.get().load(personPhoto).into(dp);
            Picasso.get().load(personPhoto).into(dp2);
            namepop.setText(personName);
            emailpop.setText(personEmail);
            tv.setText(getTimeofday()+" "+personGivenName+" !");

        }
        scrollView22 = (ScrollView)view.findViewById(R.id.scrollview22);

        recentlyplayedrecyler = (RecyclerView)view.findViewById(R.id.recyclerviewinmood);

        recentlyplayedrecyler.setHasFixedSize(true);
        recentlyplayedrecyler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        recentlyplayedAdapter = new RecentlyplayedAdapter(recentlyplayedLists, getContext());
        recentlyplayedrecyler.setAdapter(recentlyplayedAdapter);
        recentlyplayedrecyler.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.layouttransit));


        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    recentlyplayedLists.clear();
                    String myuri = "";
                    if(snapshot.hasChild("imguris")){
                        if(snapshot.child("imguris").hasChild(myemail)){
                            String getSonguri = "";
                            for (DataSnapshot dataSnapshot : snapshot.child("imguris").child(myemail).getChildren()) {
                                if(dataSnapshot.hasChild("tracks") && dataSnapshot.hasChild("uri")) {
                                    final String getemail = dataSnapshot.getKey();
                                    dataSet = false;
                                    //Toast.makeText(getContext(), getemail + "", Toast.LENGTH_SHORT).show();
                                    final String getName = dataSnapshot.child("tracks").getValue(String.class);
                                    getSonguri = dataSnapshot.child("uri").getValue(String.class);
                                    if (!getSonguri.isEmpty()) {
                                        RecentlyplayedList recentlyplayedList = new RecentlyplayedList(getSonguri);
                                        recentlyplayedLists.add(recentlyplayedList);
                                        recentlyplayedAdapter.updateData(recentlyplayedLists);
                                        recentlyplayedrecyler.smoothScrollToPosition(recentlyplayedLists.size()+1);
                                       /// Toast.makeText(getContext(), getSonguri + "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){
            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
        }













//

        LinearLayout linearLayout = view.findViewById(R.id.design_bottom_sheet);
        try {
            bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        }
        catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        bubbleTabBar = view.findViewById(R.id.bubbleTabBar);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    MainActivity2.bubbleTabBar.setVisibility(bubbleTabBar.INVISIBLE);
                    scrollView22.setVisibility(View.INVISIBLE);

                }
                catch (Exception e){
                    Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == 5){
                    //if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_HIDDEN){
                        MainActivity2.bubbleTabBar.setVisibility(bubbleTabBar.VISIBLE);
                        scrollView22.setVisibility(View.VISIBLE);
                        scrollView22.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
       Button logout = view.findViewById(R.id.logoutbtn);
       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(getContext(), "Loggedout", Toast.LENGTH_SHORT).show();
               logoutmethod();
           }
       });


    }



    private void logoutmethod(){
        //FirebaseAuth.getInstance().signOut();
        //mGoogleSignInClient.signOut();
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        }catch (Exception e){
            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }
    private void connected(){

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSpotifyAppRemote.isConnected()){
            SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public static void test(String str){
        test = str;
    }
    public static String getTimeofday(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            return "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            return "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            return "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            return "Good Night";
        }
        return "";
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recentlyplayedrecyler.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recentlyplayedrecyler.getLayoutManager().onSaveInstanceState());
    }


}