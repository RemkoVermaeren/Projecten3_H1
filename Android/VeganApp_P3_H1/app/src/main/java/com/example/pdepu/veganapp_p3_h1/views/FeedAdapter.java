package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.FeedFragment;
import com.example.pdepu.veganapp_p3_h1.models.FeedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maartenvanmeersche on 9/08/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    public ArrayList<FeedItem> feedItems;
    private FeedFragment fragment;
    public FeedAdapter(ArrayList<FeedItem> feedItems, FeedFragment fragment) {
        this.feedItems = feedItems;
        this.fragment = fragment;
    }


    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.feedImage)
        ImageView feedImage;

        @BindView(R.id.feedCardView)
        TextView feedCardView;

        @BindView(R.id.btnLike)
        Button btnLike;

        @BindView(R.id.likes)
        TextView likes;

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
        final FeedItem f = feedItems.get(position);
        ImageView image = holder.feedImage;
        TextView feed = holder.feedCardView;
        final TextView likes = holder.likes;
        Button like = holder.btnLike;
        final Context context = holder.feedCardView.getContext();
        feed.setText(f.toString());

        if(f.getChallenge().getAmountOfLikes() > 0 )
            likes.setText(String.valueOf(f.getChallenge().getAmountOfLikes()));
        if (f.getChallenge().getPicture() != null && !f.getChallenge().getPicture().isEmpty())
            Picasso.with(image.getContext()).load(UriHandler.resizeUrl(f.getChallenge().getPicture(), "150", "150")).fit().into(image);
        like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fragment.onClickLike(likes, f);
                //CharSequence text = "Liked post";
                //int duration = Toast.LENGTH_SHORT;
                //Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
            }
        });
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
