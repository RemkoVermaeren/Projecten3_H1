package com.example.pdepu.veganapp_p3_h1.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Blog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kas on 13/08/2017.
 */

public class ChooseBlogAdapter extends RecyclerView.Adapter<ChooseBlogAdapter.ChooseBlogViewHolder> {

    private ArrayList<Blog> blogs;

    public ChooseBlogAdapter(ArrayList<Blog> blogs) {
        this.blogs = blogs;
    }


    @Override
    public ChooseBlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_challenge_choose_restaurant_cardview,parent,false);
        return new ChooseBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseBlogAdapter.ChooseBlogViewHolder holder, int position) {
            // Todo
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ChooseBlogViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chooseBlogTitle)
        TextView title;

        @BindView(R.id.chooseBlogDescription)
        TextView description;

        @BindView(R.id.chooseBlogAuthor)
        TextView author;

        public ChooseBlogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
