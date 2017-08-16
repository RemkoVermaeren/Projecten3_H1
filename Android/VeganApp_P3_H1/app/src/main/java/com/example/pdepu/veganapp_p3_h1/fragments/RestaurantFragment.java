package com.example.pdepu.veganapp_p3_h1.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Restaurant;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.UriHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pdepu.veganapp_p3_h1.R.id.restaurantImage;

/**
 * Created by pdepu on 12/08/2017.
 */

public class RestaurantFragment extends Fragment {

    private Restaurant restaurant;
    private String restaurantString;
    private Service service;
    @BindView(restaurantImage)
    ImageView restaurantImageView;

    @BindView(R.id.restaurantName)
    TextView restaurantName;

    @BindView(R.id.restaurantPoints)
    TextView restaurantPoints;

    @BindView(R.id.restaurantRating)
    TextView restaurantRating;

    @BindView(R.id.restaurantWheelChairAccess)
    TextView restaurantWheelChairAccess;

    @BindView(R.id.restaurantDetails)
    TextView restaurantDetails;

    @BindView(R.id.claimYourPointsRestaurantButton)
    Button claimYourPointsRestaurantButton;


    @BindView(R.id.openGoogleMaps)
    Button openGoogleMapsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            restaurantString = getArguments().getString("restaurant");
            callApi();
        }else
            checkSharedPreferences();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        checkSharedPreferences();
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

    @OnClick(R.id.openWeb)
    public void openWeb(){
        String url = restaurant.getWebsite();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

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

    private void setSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putString("restaurantPreferences", new Gson().toJson(restaurant)).apply();
    }

    private void checkSharedPreferences(){
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        if (prefs.getString("restaurantPreferences", null) != null) {
            restaurant = new Gson().fromJson(prefs.getString("restaurantPreferences", null), Restaurant.class);
            callApi();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setSharedPreferences();
    }

    private String getDetails(Restaurant restaurant) {



        StringBuilder builder = new StringBuilder();
        builder.append("Address: " + restaurant.getAddress() + "\n");
        builder.append("Telephone: " + restaurant.getTelephoneNumber() + "\n");
        builder.append("Website: " + restaurant.getWebsite() + "\n");
        builder.append("Information: " + restaurant.getExtraInformation() + "\n");
        return builder.toString();
    }


    private void updateView() {
        if (restaurant.getPicture() != null && !restaurant.getPicture().isEmpty())
            Picasso.with(restaurantImageView.getContext()).load(UriHandler.resizeUrl(restaurant.getPicture(),
                    String.valueOf(Resources.getSystem().getDisplayMetrics().widthPixels),
                    String.valueOf(((int) TypedValue.applyDimension(TypedValue.DENSITY_DEFAULT, 185, Resources.getSystem().getDisplayMetrics()))))).fit().into(restaurantImageView);
        if(restaurant.isWheelchairAccess())
            restaurantWheelChairAccess.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_box_checked, 0 , 0);
        restaurantName.setText(restaurant.getName());
        restaurantWheelChairAccess.setText(getString(R.string.wheelchair));
        restaurantPoints.setText(String.valueOf(restaurant.getVeganPoints()));
        restaurantRating.setText(String.valueOf(restaurant.getRating()));
        restaurantDetails.setText(getDetails(restaurant));

    }
}
