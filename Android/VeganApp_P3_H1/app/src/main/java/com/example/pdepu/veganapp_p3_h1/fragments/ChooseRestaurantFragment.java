package com.example.pdepu.veganapp_p3_h1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Restaurant;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ChooseRestaurantAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 12/08/2017.
 */

public class ChooseRestaurantFragment extends Fragment {
    private Service service;
    private ArrayList<Restaurant> restaurants = new ArrayList<>();

    @BindView(R.id.chooseRestaurantRecylerView)
    RecyclerView chooseRestaurantRecylerView;


    public static ListFragmentOnClickListener listFragmentOnClickListener;
    private LinearLayoutManager layoutManager;
    private ChooseRestaurantAdapter adapter;
    private String restaurantToPass;

    private int currentIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        listFragmentOnClickListener = new ListFragmentOnClickListener(this.getContext());
        callApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_choose_restaurant, container, false);
        ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ChooseRestaurantAdapter(restaurants);

        chooseRestaurantRecylerView.setLayoutManager(layoutManager);
        chooseRestaurantRecylerView.setAdapter(adapter);

        return rootView;

    }

    public class ListFragmentOnClickListener implements View.OnClickListener {
        private final Context context;

        public ListFragmentOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            currentIndex = chooseRestaurantRecylerView.getChildAdapterPosition(view);
            RestaurantFragment restaurantFragment;

            restaurantFragment = new RestaurantFragment();
            Bundle extras = new Bundle();
            restaurantToPass = restaurants.get(currentIndex).get_id();
            extras.putString("restaurant", restaurantToPass);
            restaurantFragment.setArguments(extras);


            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, restaurantFragment);
            transaction.addToBackStack(null);
            transaction.commit();


        }


    }

    private void callApi() {
        final Call<List<Restaurant>> restaurantCall = service.getRestaurantsList();
        restaurantCall.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Restaurant> restaurantsResponse = new ArrayList<>(response.body());
                    if (restaurantsResponse.size() > 4)
                        restaurants.addAll(getRandomRestaurants(restaurantsResponse));
                    else
                        restaurants.addAll(restaurantsResponse);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private ArrayList<Restaurant> getRandomRestaurants(ArrayList<Restaurant> restaurants) {
        Collections.shuffle(restaurants);
        return new ArrayList<>(restaurants.subList(0, 5));
    }
}
