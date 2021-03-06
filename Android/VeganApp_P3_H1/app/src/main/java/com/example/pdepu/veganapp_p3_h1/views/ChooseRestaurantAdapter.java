package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.ChooseRestaurantFragment;
import com.example.pdepu.veganapp_p3_h1.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pdepu on 12/08/2017.
 */

public class ChooseRestaurantAdapter extends RecyclerView.Adapter<ChooseRestaurantAdapter.ChooseRestaurantViewHolder> {

    private ArrayList<Restaurant> restaurants;

    public ChooseRestaurantAdapter(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public static class ChooseRestaurantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chooseImage)
        ImageView chooseRestaurantImage;

        @BindView(R.id.chooseName)
        TextView chooseRestaurantName;


        public ChooseRestaurantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ChooseRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_challenge_choose_cardview, parent, false);
        view.setOnClickListener(ChooseRestaurantFragment.listFragmentOnClickListener);
        return new ChooseRestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseRestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        ImageView image = holder.chooseRestaurantImage;
        TextView recipeName = holder.chooseRestaurantName;
        Context context = holder.chooseRestaurantImage.getContext();
        if (restaurant.getPicture() != null && !restaurant.getPicture().isEmpty())
            Picasso.with(image.getContext()).load(UriHandler.resizeUrl(restaurant.getPicture(),
                    String.valueOf(Resources.getSystem().getDisplayMetrics().widthPixels),
                    String.valueOf(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 185, Resources.getSystem().getDisplayMetrics()))))).fit().into(image);
        recipeName.setText(restaurant.getName());

    }


    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}

