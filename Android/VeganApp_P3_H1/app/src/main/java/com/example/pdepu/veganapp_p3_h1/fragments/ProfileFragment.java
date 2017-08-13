package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ProfileChallengeAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @BindView(R.id.imageViewUserProfile)
    ImageView imageViewUserProfile;

    @BindView(R.id.usernameProfile)
    TextView textViewUsernameProfile;

    @BindView(R.id.followerAmountProfile)
    TextView textViewFollowerAmountProfile;

    @BindView(R.id.veganPointsProfile)
    TextView textViewVeganPointsProfile;

    @BindView(R.id.linearLayoutProfile)
    LinearLayout linearLayoutProfile;

    @BindView(R.id.challengeRecyclerView)
    RecyclerView challengeRecyclerView;

    private static final int PICK_IMAGE = 0;


    private Map response;


    private File file;
    private String uri;
    private String imageUrl;
    private ArrayList<Challenge> challenges = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ProfileChallengeAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
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


        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProfileChallengeAdapter(challenges);

        challengeRecyclerView.setLayoutManager(layoutManager);
        challengeRecyclerView.setAdapter(adapter);
        challengeRecyclerView.setHasFixedSize(true);


        return rootView;

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu,inflater);
//        inflater.inflate(R.menu.main, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            startEditFragment();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void startEditFragment(){
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        fragment.setArguments(extras);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
                    getUserChallenges();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void postUser() {
        Call<User> userCall = service.updateUser(user.get_id(), user);
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

    private void getUserChallenges() {
        Call<List<Challenge>> challengeCall = service.getChallengesUser(user.get_id());
        challengeCall.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful()) {
                    challenges.addAll(response.body());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {

            }
        });
    }

    private void updateView(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty())
            Picasso.with(imageViewUserProfile.getContext()).load(user.getImage()).fit().into(imageViewUserProfile);
        textViewUsernameProfile.setText(user.getFullName());
        textViewFollowerAmountProfile.setText(String.valueOf(user.getFollowingUsers().length) + " followers");
        textViewVeganPointsProfile.setText(String.valueOf(user.getTotalVeganScore()) + " vegan score");
        MainActivity activity = (MainActivity) getActivity();
        activity.updateView(user);


    }


}
