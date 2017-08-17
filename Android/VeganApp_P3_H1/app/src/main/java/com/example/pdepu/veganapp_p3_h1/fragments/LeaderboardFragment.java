package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.LeaderboardAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 4/08/2017.
 */

public class LeaderboardFragment extends Fragment {

    @BindView(R.id.leaderboardRecyclerView)
    RecyclerView leaderboardRecyclerView;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.leaderboardLayout)
    LinearLayout leaderboardLayout;

    protected LinearLayoutManager layoutManager;
    private ArrayList<User> users = new ArrayList<>();

    private LeaderboardAdapter adapter;
    private Service service;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        callApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        ButterKnife.bind(this, rootView);

        progress.setVisibility(View.VISIBLE);
        leaderboardLayout.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(getActivity());
        leaderboardRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(leaderboardRecyclerView.getContext(),
                layoutManager.getOrientation());
        leaderboardRecyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new LeaderboardAdapter(users);
        leaderboardRecyclerView.setAdapter(adapter);
        leaderboardRecyclerView.setHasFixedSize(true);

        return rootView;

    }

    private void callApi() {
        Call<List<User>> usersCall = service.getAllUsers();
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> userResponse = new ArrayList<User>(response.body());
                    Collections.sort(userResponse, new Comparator<User>() {
                        @Override
                        public int compare(User obj1, User obj2) {
                            return Integer.valueOf(obj2.getTotalVeganScore()).compareTo(obj1.getTotalVeganScore());
                        }
                    });

                    users.clear();
                    users.addAll(userResponse);
                    adapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    leaderboardLayout.setVisibility(View.VISIBLE);


                } else {
                    progress.setVisibility(View.GONE);
                    leaderboardLayout.setVisibility(View.VISIBLE);
                }

                Log.i("users", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                leaderboardLayout.setVisibility(View.VISIBLE);
                Log.i("failure", t.toString());
            }
        });
    }
}
