package com.example.pdepu.veganapp_p3_h1.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
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
            user = new Gson().fromJson(getArguments().getString("user"), User.class);
            token = user.getToken();
        }
        callApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers,container,false);
        ButterKnife.bind(this, binding.getRoot());
        adapter = new FollowersAdapter(this.getContext(), FullNameComparator, user);
        binding.followersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.followersRecyclerView.setAdapter(adapter);



        return binding.getRoot();

    }




    private void callApi() {
        Call<List<User>> usersCall = service.getAllUsers();
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> userResponse = new ArrayList<>(response.body());
                    ArrayList<User> followers = new ArrayList<User>();
                    for (String id : user.getFollowingUsers()){
                        for(User f : userResponse){
                            if(id.equals(f.get_id()))
                                followers.add(f);
                        }
                    }
                    users.clear();
                    users.addAll(followers);


                    for(int i = 0; i < users.size(); i++){
                        if(users.get(i).get_id().equals(token.getUserid())){
                            users.remove(i);
                        }

                    }
                    adapter.add(users);

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
