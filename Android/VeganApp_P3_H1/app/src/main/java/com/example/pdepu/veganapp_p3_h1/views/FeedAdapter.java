package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.FeedItem;
import com.example.pdepu.veganapp_p3_h1.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maartenvanmeersche on 9/08/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    public ArrayList<FeedItem> feedItems;

    public FeedAdapter(ArrayList<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }


    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.feedCardView)
        TextView feedCardView;

        @BindView(R.id.btnLike)
        Button btnLike;

        public FeedViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed_cardview,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position){
        FeedItem f = feedItems.get(position);
        TextView feed = holder.feedCardView;
        Button like = holder.btnLike;
        Context context = holder.feedCardView.getContext();
        feed.setText(f.toString());
    }

    public void clear() {
        feedItems.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<FeedItem> items){
        feedItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return feedItems.size();
    }
}
