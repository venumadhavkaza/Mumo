package com.venumadhav.mumo.recentlyplayed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.venumadhav.mumo.R;
import com.venumadhav.mumo.messages.MessagesAdapter;
import com.venumadhav.mumo.messages.MessagesList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentlyplayedAdapter extends RecyclerView.Adapter<RecentlyplayedAdapter.MyViewHolder>{

    private List<RecentlyplayedList> recentlyplayedlists;
    private final Context context;
    public static int lastPosition =0;
    public RecentlyplayedAdapter(List<RecentlyplayedList> recentlyplayedlists, Context context) {
        this.recentlyplayedlists = recentlyplayedlists;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentlyplayedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new RecentlyplayedAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lastplayed_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyplayedAdapter.MyViewHolder holder, int position) {
        RecentlyplayedList list2 = recentlyplayedlists.get(position);
        if(!list2.getUri().isEmpty()){
            Picasso.get().load(list2.getUri()).into(holder.imginsiderec);
        }
        setAnimation(holder.itemView, position);
    }

    public void updateData(List<RecentlyplayedList> recentlyplayedlists){
        this.recentlyplayedlists = recentlyplayedlists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recentlyplayedlists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imginsiderec;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imginsiderec = itemView.findViewById(R.id.imginsiderec);

        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
        if (position < lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
