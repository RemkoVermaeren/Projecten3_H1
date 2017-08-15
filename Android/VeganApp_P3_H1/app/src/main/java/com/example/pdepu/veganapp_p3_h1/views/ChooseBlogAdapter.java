package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.ChooseBlogFragment;
import com.example.pdepu.veganapp_p3_h1.models.Blog;
import com.squareup.picasso.Picasso;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_challenge_choose_blog_cardview,parent,false);
        view.setOnClickListener(ChooseBlogFragment.listFragmentOnClickListener);
        return new ChooseBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseBlogAdapter.ChooseBlogViewHolder holder, int position) {
        Blog blog = blogs.get(position);
        TextView title = holder.title;
        TextView description = holder.description;
        TextView author = holder.author;
        ImageView image = holder.image;

        Context context = holder.title.getContext();
        if(blog.getPicture() != null){
            Picasso.with(context).load(blog.getPicture()).resize(120,150).into(image);
        }

        title.setText(blog.getName() + " - ");
        //description.setText(blog.getDescription()); Todo nog in webapp steken.
        author.setText(blog.getAuthor());
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public static class ChooseBlogViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chooseBlogTitle)
        TextView title;

        @BindView(R.id.chooseBlogDescription)
        TextView description;

        @BindView(R.id.chooseBlogAuthor)
        TextView author;

        @BindView(R.id.chooseBlogImage)
        ImageView image;

        public ChooseBlogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
