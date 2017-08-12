package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 8/08/2017.
 */

public class ProfileFragment extends Fragment {
    private Service service;
    private Token token;
    private User user;

    @BindView(R.id.usernameProfile)
    TextView textViewUsernameProfile;

    @BindView(R.id.followerAmountProfile)
    TextView textViewFollowerAmountProfile;

    @BindView(R.id.veganPointsProfile)
    TextView textViewVeganPointsProfile;

    @BindView(R.id.linearLayoutProfile)
    LinearLayout linearLayoutProfile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
            callApi();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @OnClick(R.id.linearLayoutProfile)
    public void onClick() {
        FollowerFragment fragment = new FollowerFragment();
        Bundle extras = new Bundle();
        extras.putString("user", new Gson().toJson(user));
        fragment.setArguments(extras);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void callApi() {
        Call<User> userCall = service.getUserById(token.getUserid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    user.setToken(token);
                    updateView(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }


    private void updateView(User user) {
        textViewUsernameProfile.setText(user.getFullName());
        textViewFollowerAmountProfile.setText(String.valueOf(user.getFollowingUsers().length) + " followers");
        textViewVeganPointsProfile.setText(String.valueOf(user.getTotalVeganScore()) + " vegan score");


    }
}
