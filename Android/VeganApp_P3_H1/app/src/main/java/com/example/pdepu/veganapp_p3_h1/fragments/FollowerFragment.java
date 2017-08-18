package com.example.pdepu.veganapp_p3_h1.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.databinding.FragmentFollowersBinding;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.FollowersAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 8/08/2017.
 */

public class FollowerFragment extends Fragment {

    private Service service;


    private FollowersAdapter adapter;
    private FragmentFollowersBinding binding;
    private LinearLayoutManager layoutManager;

    private User user;
    private Token token;


    private static final Comparator<User> FullNameComparator = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            return a.getFullName().compareTo(b.getFullName());
        }
    };


    private ArrayList<User> users = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
            callApi();
        } else {
            SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
            if (prefs.getString("tokenStringPreferences", null) != null) {
                token = new Gson().fromJson(prefs.getString("tokenStringPreferences", null), Token.class);
                callApi();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, binding.getRoot());

        binding.progress.setVisibility(View.VISIBLE);
        binding.followersRecyclerView.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(this.getContext());
        adapter = new FollowersAdapter(this.getContext(), FullNameComparator, user, ((MainActivity) getActivity()));


        binding.followersRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.followersRecyclerView.getContext(),
                layoutManager.getOrientation());
        binding.followersRecyclerView.addItemDecoration(dividerItemDecoration);

        binding.followersRecyclerView.setAdapter(adapter);
        binding.followersRecyclerView.setHasFixedSize(true);

        return binding.getRoot();

    }

    private void loadingDone() {
        binding.progress.setVisibility(View.GONE);
        binding.followersRecyclerView.setVisibility(View.VISIBLE);

    }

    private void callApi() {
        Call<User> userCall = service.getUserById(token.getUserid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    user.setToken(token);
                    adapter.setUserOriginal(user);
                    getUserFollowers();
                } else {
                    loadingDone();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingDone();
                Log.i("failure", t.toString());
            }
        });

    }


    private void getUserFollowers() {
        Call<List<User>> usersCall = service.getAllFollowers(token.getUserid());
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    final ArrayList<User> userResponse = new ArrayList<>(response.body());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            users.clear();
                            users.addAll(userResponse);
                            adapter.add(users);
                            if (users.size() <= 0) {
                                binding.followersRecyclerView.setVisibility(View.GONE);
                                binding.empty.setVisibility(View.VISIBLE);
                            } else {
                                binding.followersRecyclerView.setVisibility(View.VISIBLE);
                            }


                        }
                    });
                    binding.progress.setVisibility(View.GONE);


                } else {
                    loadingDone();
                }

                Log.i("users", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                loadingDone();
                Log.i("failure", t.toString());
            }
        });
    }


}
