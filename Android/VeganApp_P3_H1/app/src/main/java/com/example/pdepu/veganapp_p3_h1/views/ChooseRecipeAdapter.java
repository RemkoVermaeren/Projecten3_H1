package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.ChooseRecipeFragment;
import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by pdepu on 9/08/2017.
 */

public class ChooseRecipeAdapter extends RecyclerView.Adapter<ChooseRecipeAdapter.ChooseRecipeViewHolder> {

    private ArrayList<Recipe> recipes;

    public ChooseRecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public static class ChooseRecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chooseRecipeImage)
        ImageView chooseRecipeImage;

        @BindView(R.id.chooseRecipeName)
        TextView chooseRecipeName;

//        @BindView(R.id.chooseRecipeVeganPoints)
//        TextView chooseRecipeVeganPoints;

        public ChooseRecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ChooseRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_challenge_choose_recipe_cardview, parent, false);
        view.setOnClickListener(ChooseRecipeFragment.listFragmentOnClickListener);
        return new ChooseRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseRecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        ImageView image = holder.chooseRecipeImage;
        TextView recipeName = holder.chooseRecipeName;
        Context context = holder.chooseRecipeImage.getContext();
        if (recipe.getPicture() != null && !recipe.getPicture().isEmpty())
            Picasso.with(context).load(recipe.getPicture()).resize(200, 200).into(image);
        recipeName.setText(recipe.getName());
        //veganScore.setText(String.valueOf(recipe.getVeganPoints()) + " points");

    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }
}

