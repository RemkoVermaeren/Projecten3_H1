package com.example.pdepu.veganapp_p3_h1.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Restaurant;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 12/08/2017.
 */

public class RestaurantFragment extends Fragment {

    private Restaurant restaurant;
    private String restaurantString;
    private Service service;
    @BindView(R.id.restaurantImage)
    ImageView restaurantImageView;

    @BindView(R.id.restaurantDetails)
    TextView restaurantDetails;

    @BindView(R.id.claimYourPointsRestaurantButton)
    Button claimYourPointsRestaurantButton;


    @BindView(R.id.wheelChairAccessCheckbox)
    CheckBox wheelChairAccessCheckbox;

    @BindView(R.id.openGoogleMaps)
    Button openGoogleMapsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantString = getArguments().getString("restaurant");
        }
        service = new ServicesInitializer().initializeService();
        callApi();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.claimYourPointsRestaurantButton)
    public void onClick() {
        PublishRestaurantFragment publishRestaurantFragment = new PublishRestaurantFragment();
        Bundle extras = new Bundle();
        extras.putString("restaurantName", restaurant.getName());
        extras.putString("restaurantPoints", String.valueOf(restaurant.getVeganPoints()));
        publishRestaurantFragment.setArguments(extras);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, publishRestaurantFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.openGoogleMaps)
    public void openMaps(){
        String map = "http://maps.google.co.in/maps?q=" + restaurant.getAddress();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(map));
        startActivity(intent);
    }

    private void callApi() {
        Call<Restaurant> restaurantsCall = service.getRestaurant(restaurantString);
        restaurantsCall.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    restaurant = response.body();
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private String getDetails(Restaurant restaurant) {



        StringBuilder builder = new StringBuilder();
        builder.append(restaurant.getName() + "\n");
        builder.append("Address: " + restaurant.getAddress() + "\n");
        builder.append("Rating: " + String.valueOf(restaurant.getRating()) + "\n");
        builder.append("Telephone: " + restaurant.getTelephoneNumber() + "\n");
        builder.append("Website: " + restaurant.getWebsite() + "\n");
        builder.append("ExtraInformation: " + restaurant.getExtraInformation() + "\n");
        builder.append(String.valueOf(restaurant.getVeganPoints()) + " points" + "\n");
        builder.append("Wheelchair acces: ");
        return builder.toString();
    }


    private void updateView() {
        restaurantDetails.setText(getDetails(restaurant));
        wheelChairAccessCheckbox.setChecked(restaurant.isWheelchairAccess());
    }
}
