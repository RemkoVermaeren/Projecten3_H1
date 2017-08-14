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
import com.example.pdepu.veganapp_p3_h1.models.Blog;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ChooseBlogAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kas on 13/08/2017.
 */

public class ChooseBlogFragment extends Fragment {
    private Service service;

    private ArrayList<Blog> blogs = new ArrayList<>();

    @BindView(R.id.chooseBlogRecylerView)
    RecyclerView chooseBlogRecyclerView;

    private LinearLayoutManager layoutManager;
    private ChooseBlogAdapter adapter;

   @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        callApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_choose_blog, container, false);
        ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ChooseBlogAdapter(blogs);
        
        chooseBlogRecyclerView.setLayoutManager(layoutManager);
        chooseBlogRecyclerView.setAdapter(adapter);

        return rootView;
    }

    private void callApi() {
        Call<List<Blog>> blogCall = service.getAllBlogs();
        blogCall.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if(response.isSuccessful()){
                    blogs.addAll(response.body());
                    adapter.notify();
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });
    }
}
