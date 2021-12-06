package com.venumadhav.mumo.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.venumadhav.mumo.MemoryData;
import com.venumadhav.mumo.R;
import com.venumadhav.mumo.messages.MessagesList;
import com.venumadhav.mumo.mood;
import com.venumadhav.mumo.network.Dummyclass;
import com.venumadhav.mumo.network.NetworkRequestUtil;
import com.venumadhav.mumo.network.RequestPeekalink;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatList> chatLists;
    private final Context context;
    private String userMobile;

    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userMobile = MemoryData.getData(context);
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }



    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        ChatList list2 = chatLists.get(position);

        if(list2.getMobile().equals(userMobile)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
//            if(list2.getMessage().contains("open.spotify.com")){
//                RequestPeekalink ob1 = new RequestPeekalink();
//                ob1.setLink(list2.getMessage());
//                Dummyclass ob = new Dummyclass();
//                ob.setUrl("https://api.peekalink.io/");
//                ob.setRequestPeekalink(ob1);
//                holder.myimg.setVisibility(View.VISIBLE);
//                new NetworkRequestUtil().execute(ob);
//
//                holder.myTime.setText(list2.getDate() + " " + list2.getTime());
//
//            }else {


                holder.myMessage.setText(list2.getMessage());

                holder.myTime.setText(list2.getDate() + " " + list2.getTime());
            //}
        }
        else{

            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+" "+list2.getTime());

        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists){
        this.chatLists = chatLists;
        notifyDataSetChanged();

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;
        public static ImageView oppoimg, myimg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
            oppoimg = itemView.findViewById(R.id.oppoimg);
            myimg = itemView.findViewById(R.id.myimg);
        }
    }
}

