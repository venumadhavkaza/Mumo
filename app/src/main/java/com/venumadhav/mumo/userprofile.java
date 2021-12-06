package com.venumadhav.mumo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.auth.app.SpotifyAuthHandler;
import com.squareup.picasso.Picasso;
import com.venumadhav.mumo.messages.MessagesAdapter;
import com.venumadhav.mumo.messages.MessagesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userprofile extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    private final List<MessagesList> messagesLists = new ArrayList<>();

    String name;
    String email;
    Uri personPhoto;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private SpotifyAuthHandler spotifyAuthHandler;
    Context thiscontext;
    DatabaseReference mdatabasereference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mumo-91024-default-rtdb.firebaseio.com/");
    // TODO: Rename and change types of parameters
    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private int unseenMessages = 0;
    private String lastMessage = "";
    private String chatKey = "";
    public static HashMap<String,Integer> map = new HashMap<>();

    private boolean dataSet = false;



    //private RecyclerView messagesRecyclerView;
    public userprofile() {
        // Required empty public constructor
    }


    public static userprofile newInstance(String param1, String param2) {
        userprofile fragment = new userprofile();

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
        return inflater.inflate(R.layout.fragment_userprofile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /////////////////////////////////////////////////////

        final CircleImageView userProfilePic = view.findViewById(R.id.userProfilePic);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
             name = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String email = acct.getEmail();
            String personId = acct.getId();
             personPhoto = acct.getPhotoUrl();
            //Picasso.get().load(personPhoto).into(dp);
            //Picasso.get().load(personPhoto).into(dp2);

        }




        /////////////////////////////////////////////////////
        messagesRecyclerView = (RecyclerView)view.findViewById(R.id.messagesRecyclerView);

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesAdapter = new MessagesAdapter(messagesLists, getContext());

        messagesRecyclerView.setAdapter(messagesAdapter);
        //messagesAdapter.notifyDataStateChanged();






        //profilepic from firebase

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();



        //Toast.makeText(getContext(), mobileno, Toast.LENGTH_SHORT).show();
        String mobile = MainActivity.mobileno;
        mdatabasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//
                final String profilepicUrl = snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);
                Picasso.get().load(profilepicUrl).into(userProfilePic);
                    progressDialog.dismiss();
//
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });


       mdatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";

                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){

                    final String getMobile = dataSnapshot.getKey();

                    dataSet = false;

                    if(!getMobile.equals(mobile)){

                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);

                        mdatabasereference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if(getChatCounts > 0){

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile))){

                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){

                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(getContext(), getKey));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if(getMessageKey > getLastSeenMessage){
                                                        unseenMessages++;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }

                                if(!dataSet){
                                    dataSet = true;
                                    MessagesList messagesList = new MessagesList(getName, getMobile, lastMessage, getProfilePic, unseenMessages, chatKey);
                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}