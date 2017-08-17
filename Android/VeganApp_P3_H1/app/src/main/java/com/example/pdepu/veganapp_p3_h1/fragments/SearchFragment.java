package com.example.pdepu.veganapp_p3_h1.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.databinding.FragmentSearchBinding;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.SearchAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 4/08/2017.
 */

public class SearchFragment extends Fragment {

    private Service service;


    private SearchAdapter adapter;
    private FragmentSearchBinding binding;
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
        }
        callApi();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        ButterKnife.bind(this, binding.getRoot());
        adapter = new SearchAdapter(this.getContext(), FullNameComparator, new User(), (MainActivity) getActivity());

        binding.progress.setVisibility(View.VISIBLE);
        binding.searchRecyclerView.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(this.getContext());
        binding.searchRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.searchRecyclerView.getContext(),
                layoutManager.getOrientation());
        binding.searchRecyclerView.addItemDecoration(dividerItemDecoration);

        binding.searchRecyclerView.setAdapter(adapter);
        binding.searchRecyclerView.setHasFixedSize(true);


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<User> filteredUserList = filter(users, newText);
                adapter.replaceAll(filteredUserList);
                binding.searchRecyclerView.scrollToPosition(0);
                return true;
            }
        });


        return binding.getRoot();

    }


    private static List<User> filter(List<User> users, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<User> filteredUserList = new ArrayList<>();
        for (User user : users) {
            final String text = user.getFullName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredUserList.add(user);
            }
        }
        return filteredUserList;
    }


    private void callApi() {
        Call<List<User>> usersCall = service.getAllUsers();
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> userResponse = new ArrayList<>(response.body());

                    users.clear();
                    users.addAll(userResponse);

                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).get_id().equals(token.getUserid())) {
                            user = users.get(i);
                            user.setToken(token);
                            users.remove(i);
                        }

                    }
                    adapter.setUserOriginal(user);
                    adapter.add(users);


                }
                binding.progress.setVisibility(View.GONE);
                binding.searchRecyclerView.setVisibility(View.VISIBLE);
                Log.i("users", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                binding.progress.setVisibility(View.GONE);
                binding.searchRecyclerView.setVisibility(View.VISIBLE);
                Log.i("failure", t.toString());
            }
        });
    }



}
