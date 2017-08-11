package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.FeedItem;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.FeedAdapter;

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

    protected LinearLayoutManager layoutManager;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<FeedItem> feedItems = new ArrayList<>();

    private FeedAdapter adapter;
    private Service service;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        callApi();

        ArrayList <FeedItem> vullen;
        for (User u : users){
            for (Challenge c : u.getChallenges()){
                if (c.isCompleted()){
                    FeedItem item = new FeedItem(u, c);
                    feedItems.add(item);        }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_feed,container,false);
        ButterKnife.bind(this,rootView);

        layoutManager = new LinearLayoutManager(getActivity());
        feedRecyclerView.setLayoutManager(layoutManager);


        //User test = new User("testUser", "testName", "testSurname", new Date(), true, "password");
        //feedItems.add(new FeedItem(test, new Challenge("testChallengeTrue", "description", "http://placehold.it/120x120&text=image1", new Date(), 1, 3, true)));

        //feedItems.add(new FeedItem(test, new Challenge("testChallengeFalse", "description", "http://placehold.it/120x120&text=image1", new Date(), 1, 3, false)));


        adapter = new FeedAdapter(feedItems);
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

                }
                Log.i("users", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });
    }
}
