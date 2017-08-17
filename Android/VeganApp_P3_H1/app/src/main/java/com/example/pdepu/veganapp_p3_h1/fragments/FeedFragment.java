package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.FeedItem;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.FeedAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pdepu.veganapp_p3_h1.R.id.swipeToRefresh;

/**
 * Created by maartenvanmeersche on 9/08/17.
 */

public class FeedFragment extends Fragment {
    @BindView(R.id.feedRecyclerView)
    RecyclerView feedRecyclerView;
    @BindView(swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.progress)
    ProgressBar progress;

    protected LinearLayoutManager layoutManager;
    private User user;
    private ArrayList<Challenge> challenges = new ArrayList<>();
    private ArrayList<FeedItem> feedItems = new ArrayList<>();

    private FeedAdapter adapter;
    private Service service;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        callApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);

        progress.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                challenges.clear();
                feedItems.clear();
                adapter.notifyDataSetChanged();
                callApi();
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.bg_login);

        layoutManager = new LinearLayoutManager(getActivity());
        feedRecyclerView.setLayoutManager(layoutManager);

        adapter = new FeedAdapter(feedItems, FeedFragment.this);
        feedRecyclerView.setAdapter(adapter);

        return rootView;

    }

    private void callApi() {
        MainActivity activity = (MainActivity) getActivity();
        Call<User> usersCall = service.getUserById(activity.token.getUserid());
        usersCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null) {
                    user = response.body();
                    getUserChallenges();
                    Log.i("users", response.body().toString());
                }else {
                    progress.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
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
                    callChallenges();
                }else {
                    progress.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    private void callChallenges() {
        MainActivity activity = (MainActivity) getActivity();
        Call<List<Challenge>> challengeCall = service.getFeed(activity.token.getUserid());
        challengeCall.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Challenge> challengeResponse = new ArrayList<Challenge>(response.body());
                    challenges.addAll(challengeResponse);
                    Collections.sort(challenges, new Comparator<Challenge>() {
                        @Override
                        public int compare(Challenge obj1, Challenge obj2) {
                            return obj2.getDate().compareTo(obj2.getDate());
                        }
                    });

                    for (Challenge c : challenges) {
                        if (c.isCompleted()) {
                            FeedItem item = new FeedItem(c.getCreatedBy(), c);
                            feedItems.add(item);
                        }
                    }

                    if (feedItems.size() <= 0) {
                        feedRecyclerView.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    progress.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                Log.i("failure", t.toString());
            }
        });

    }

    public void onClickLike(final View view, FeedItem item) {
        Call<Challenge> usersCall = service.likeChallenge(user.get_id(), item.getChallenge().get_id());
        usersCall.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                if (response.isSuccessful()) {
                    TextView likes = (TextView) view.findViewById(R.id.likes);
                    likes.setText(String.valueOf(response.body().getAmountOfLikes()));
                    CharSequence text = "Liked post";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<Challenge> call, Throwable t) {
                Log.e("Error network", t.toString());
            }
        });

    }

}
