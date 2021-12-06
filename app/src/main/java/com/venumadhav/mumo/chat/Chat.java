
package com.venumadhav.mumo.chat;

import static com.venumadhav.mumo.userprofile.map;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Clock;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.venumadhav.mumo.MemoryData;
import com.venumadhav.mumo.R;
import com.squareup.picasso.Picasso;

import com.venumadhav.mumo.musiclist;
import com.venumadhav.mumo.network.Dummyclass;
import com.venumadhav.mumo.network.NetworkRequestUtil;
import com.venumadhav.mumo.network.RequestPeekalink;
import com.venumadhav.mumo.userprofile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private final List<ChatList> chatLists = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mumo-91024-default-rtdb.firebaseio.com/");
    String getUserMobile = "";
    private String chatKey;
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;
    public static EditText messageEditText;
    public static boolean canPlay=false;
    public static boolean requested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        MemoryData.getcanPlay(Chat.this);
        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.name);
        messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        // get data from messages adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        // get user mobile from memory
        getUserMobile = MemoryData.getData(Chat.this);

        nameTV.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        chattingRecyclerView.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    // generate chat key. by default chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                if(snapshot.hasChild("chat")){

                    if(snapshot.child("chat").child(chatKey).hasChild("messages")){

                        chatLists.clear();
                        String getMsg="";
                        String invoker="";
                        String userMobile = MemoryData.getData(Chat.this);
                        for(DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()){

                            if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")){

                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                getMsg = messagesSnapshot.child("msg").getValue(String.class);
                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                                ChatList chatList = new ChatList(getMobile, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);

                                chatAdapter.updateChatList(chatLists);
                                chattingRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount());

                                if(loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(Chat.this, chatKey))){

                                    loadingFirstTime = false;

                                    MemoryData.saveLastMsgTS(messageTimestamps, chatKey, Chat.this);
                                    chatAdapter.updateChatList(chatLists);

                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);

                                }
                            }

                        }
                        //Toast.makeText(Chat.this, getMsg+"", Toast.LENGTH_SHORT).show();
                        if(!getMobile.equals(userMobile)&&getMsg.equals("@connect")&&requested==false){
                            if(!canPlay) {
                                showDialog(Chat.this, "Incoming Connection Request");
                            }
                        }

                        if(canPlay && requested) {
                            //Toast.makeText(Chat.this, canPlay+"", Toast.LENGTH_SHORT).show();
                            if(!map.containsKey(getMsg)) {
                                if (getMsg.contains("open.spotify.com")) {
                                    invoker = getMsg;
                                    //Toast.makeText(Chat.this, invoker+"", Toast.LENGTH_SHORT).show();
                                    try {
                                        musiclist.playmusicfromchat(geturifromurl(invoker));
                                        map.clear();
                                        map.put(getMsg, 1);
                                    } catch (Exception e) {
                                        Toast.makeText(Chat.this, e + "", Toast.LENGTH_SHORT).show();
                                    }
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


        chattingRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if ( bottom < oldBottom) {
                    chattingRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chattingRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                        }
                    }, 100);
                }
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getTxtMessage = messageEditText.getText().toString();
                if(!getTxtMessage.equals("")) {
                    // get current timestamps
//                    if(getTxtMessage.contains("open.spotify.com")){
//                        RequestPeekalink ob1 = new RequestPeekalink();
//                        ob1.setLink(getTxtMessage);
//                        Dummyclass ob = new Dummyclass();
//                        ob.setUrl("https://api.peekalink.io/");
//                        ob.setRequestPeekalink(ob1);
//                        new NetworkRequestUtil().execute(ob);
//
//                    }
                    final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                    databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                    databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                    databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                    databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(getUserMobile);

                    // clear edit text
                    messageEditText.setText("");
                    try {
                        databaseReference.getDatabase();
                    }catch (Exception e){
                        Toast.makeText(Chat.this, e+"", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.testsample);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.incmsg);
        text.setText(msg);

        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.deny);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();


                canPlay = false;
                MemoryData.savecanPlay(Boolean.toString(canPlay),Chat.this);
                requested = false;
                messageEditText.setText("@no");
                dialog.dismiss();
            }
        });

        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.accept);
        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
                if(canPlay){
                    Toast.makeText(Chat.this, "You are already connected", Toast.LENGTH_SHORT).show();
                }else{
                messageEditText.setText("@yes");
                canPlay = true;
                MemoryData.savecanPlay(Boolean.toString(canPlay),Chat.this);
                requested = true;
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public String geturifromurl(String uri){
        String out = "spotify:";
        String uri2="";
        if(uri.contains("album")){
            out = out+"album:";
            uri2 = uri.replaceAll("https://open.spotify.com/album/","");
        }
        if(uri.contains("track")){
            out = out+"track:";
            uri2 = uri.replaceAll("https://open.spotify.com/track/","");
        }
        if(uri.contains("playlist")){
            out = out+"playlist:";
            uri2 = uri.replaceAll("https://open.spotify.com/playlist/","");
        }
        String arr[]=uri2.split("\\?");
        out+=arr[0];
        //Toast.makeText(Chat.this, "in here"+out+"", Toast.LENGTH_SHORT).show();
        return out;
    }

    public void conlistener(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Chat.this.notify();
            }
        });
    }
}