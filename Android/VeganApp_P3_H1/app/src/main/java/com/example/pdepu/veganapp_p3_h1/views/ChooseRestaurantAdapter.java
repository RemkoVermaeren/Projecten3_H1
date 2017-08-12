package com.example.pdepu.veganapp_p3_h1.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.ChooseRecipeFragment;
import com.example.pdepu.veganapp_p3_h1.models.Restaurant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pdepu on 12/08/2017.
 */

public class ChooseRestaurantAdapter  extends RecyclerView.Adapter<ChooseRestaurantAdapter.ChooseRestaurantViewHolder> {

    private ArrayList<Restaurant> restaurants;

    public ChooseRestaurantAdapter(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public static class ChooseRestaurantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chooseRestaurantImage)
        ImageView chooseRestaurantImage;

        @BindView(R.id.chooseRestaurantName)
        TextView chooseRestaurantName;

        @BindView(R.id.chooseRestaurantVeganPoints)
        TextView chooseRestaurantVeganPoints;

        public ChooseRestaurantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ChooseRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_challenge_choose_restaurant_cardview, parent, false);
        view.setOnClickListener(ChooseRecipeFragment.listFragmentOnClickListener);
        return new ChooseRestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseRestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        //CircleImageView image = holder.imageViewUserCardView;
        TextView recipeName = holder.chooseRestaurantName;
        TextView veganScore = holder.chooseRestaurantVeganPoints;
        // Context context = holder.imageViewUserCardView.getContext();
        recipeName.setText(restaurant.getName());
        veganScore.setText(String.valueOf(restaurant.getVeganPoints()) + " points");

    }


    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}

