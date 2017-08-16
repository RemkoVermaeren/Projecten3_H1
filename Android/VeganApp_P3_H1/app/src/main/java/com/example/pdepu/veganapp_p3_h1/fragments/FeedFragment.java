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
import android.widget.Toast;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.FeedItem;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.FeedAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maartenvanmeersche on 9/08/17.
 */

public class FeedFragment extends Fragment {
    @BindView(R.id.feedRecyclerView)
    RecyclerView feedRecyclerView;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    protected LinearLayoutManager layoutManager;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Challenge> challenges = new ArrayList<>();
    private ArrayList<Challenge> hulpChallenges = new ArrayList<>();
    private ArrayList<FeedItem> feedItems = new ArrayList<>();

    private FeedAdapter adapter;
    private Service service;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        callApi();
        //callChallenges();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_feed,container,false);
        ButterKnife.bind(this,rootView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                callApi();
                callChallenges();
                ////test refresh
                //User test = new User("testUser", "testName", "Marieke", new Date(), true, "password");
                //feedItems.add(new FeedItem(test, new Challenge("Become a vegan", "description", "http://placehold.it/120x120&text=image1", new Date(), 1, 3, true)));

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        layoutManager = new LinearLayoutManager(getActivity());
        feedRecyclerView.setLayoutManager(layoutManager);

        adapter = new FeedAdapter(feedItems, FeedFragment.this);
        feedRecyclerView.setAdapter(adapter);

        return rootView;

    }

    private void callApi(){
        Call<List<User>> usersCall = service.getAllUsers();
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    ArrayList<User> userResponse = new ArrayList<User>(response.body());
                    Collections.sort(userResponse, new Comparator<User>() {
                        @Override
                        public int compare(User obj1, User obj2) {
                            return Integer.valueOf(obj1.getTotalVeganScore()).compareTo(obj2.getTotalVeganScore());
                        }
                    });

                    users.clear();
                    users.addAll(userResponse);
                    adapter.notifyDataSetChanged();

                    //Challenges in db
                   //adapter.clear();
                   //adapter.addAll(feedItems);
                   //swipeRefreshLayout.setRefreshing(false);

                }
                Log.i("users", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });
    }

    private void callChallenges() {
        for (User u : users) {
            Call<List<Challenge>> challengeCall = service.getChallengesUser(u.get_id());
            challengeCall.enqueue(new Callback<List<Challenge>>() {
                @Override
                public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<Challenge> challengeResponse = new ArrayList<Challenge>(response.body());
                        Collections.sort(challengeResponse, new Comparator<Challenge>() {
                            @Override
                            public int compare(Challenge obj1, Challenge obj2) {
                                return Integer.valueOf(obj1.getVeganScore()).compareTo(obj2.getVeganScore());
                            }
                        });

                        hulpChallenges.clear();
                        hulpChallenges.addAll(challengeResponse);

                        //challenges.clear();
                        challenges.addAll(challengeResponse);
                        //adapter.notifyDataSetChanged();

                        // Collections.sort(feedItems, new Comparator<FeedItem>() {
                        //   @Override
                        //  public int compare(FeedItem obj1, FeedItem obj2) {
                        //      return obj1.getChallenge().getDate().compareTo(obj2.getChallenge().getDate());
                        //     }
                        // });

                        //Challenges in db
                        //adapter.clear();
                        //adapter.addAll(feedItems);
                        //swipeRefreshLayout.setRefreshing(false);

                    }
                    Log.i("users", response.body().toString());
                }

                @Override
                public void onFailure(Call<List<Challenge>> call, Throwable t) {
                    Log.i("failure", t.toString());
                }
            });
            for (Challenge c : hulpChallenges){
                if (c.isCompleted()){
                    FeedItem item = new FeedItem(u, c);
                    feedItems.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onClickLike(final View view, FeedItem item) {
        Call<User> usersCall = service.likeChallenge(item.getUser().get_id(), item.getChallenge().get_id());
        usersCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    //lock like or different color ?
                    CharSequence text = "Liked post";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error network", t.toString());
            }
        });

    }
}
