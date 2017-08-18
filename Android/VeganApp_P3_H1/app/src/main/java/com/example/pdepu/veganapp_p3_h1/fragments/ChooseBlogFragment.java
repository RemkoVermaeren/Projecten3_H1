package com.example.pdepu.veganapp_p3_h1.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Blog;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ChooseBlogAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty)
    TextView empty;

    @BindView(R.id.progress)
    ProgressBar progress;


    public static ChooseBlogFragment.ListFragmentOnClickListener listFragmentOnClickListener;
    private LinearLayoutManager layoutManager;
    private ChooseBlogAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        listFragmentOnClickListener = new ListFragmentOnClickListener(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_choose_blog, container, false);
        ButterKnife.bind(this, rootView);

        progress.setVisibility(View.VISIBLE);
        chooseBlogRecyclerView.setVisibility(View.GONE);

        checkSharedPreferences();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                blogs.clear();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(true);
                checkSharedPreferences();
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.bg_login);

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ChooseBlogAdapter(blogs);

        chooseBlogRecyclerView.setLayoutManager(layoutManager);
        chooseBlogRecyclerView.setAdapter(adapter);

        return rootView;
    }

    private void loadingDone() {
        swipeRefreshLayout.setRefreshing(false);
        progress.setVisibility(View.GONE);
        chooseBlogRecyclerView.setVisibility(View.VISIBLE);

    }

    private void setSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putString("chooseBlogsPreferences", new Gson().toJson(blogs)).apply();
    }

    private void checkSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        if (prefs.getString("chooseBlogsPreferences", null) != null) {
            if (adapter != null) {
                blogs.clear();
                blogs.addAll(new ArrayList<>(Arrays.asList(new Gson().fromJson(prefs.getString("chooseBlogsPreferences", null), Blog[].class))));
            } else
                blogs = new ArrayList<>(Arrays.asList(new Gson().fromJson(prefs.getString("chooseBlogsPreferences", null), Blog[].class)));
            loadingDone();
        } else
            callBlogApi();
    }

    private void clearSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        prefs.edit().remove("chooseBlogsPreferences").apply();

    }

    private void callBlogApi() {
        Call<List<Blog>> blogCall = service.getAllBlogs();
        blogCall.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if (response.isSuccessful()) {
                    blogs.clear();
                    ArrayList<Blog> blogsResponse = new ArrayList<Blog>(response.body());
                    Collections.sort(blogsResponse, new Comparator<Blog>() {
                        @Override
                        public int compare(Blog obj1, Blog obj2) {
                            return obj2.getDate().compareTo(obj1.getDate());
                        }
                    });
                    blogs.addAll(blogsResponse);
                    if (blogs.size() <= 0) {
                        chooseBlogRecyclerView.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    } else
                        empty.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    loadingDone();
                    setSharedPreferences();
                } else {
                    loadingDone();
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                loadingDone();
                Log.i("failure", t.toString());
            }
        });
    }

    private void startFeedFragment() {
        progress.setVisibility(View.GONE);
        FeedFragment fragment = new FeedFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    public class ListFragmentOnClickListener implements View.OnClickListener {
        private final Context context;

        public ListFragmentOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Blog blogSelected = blogs.get(chooseBlogRecyclerView.getChildAdapterPosition(view));

            MainActivity activity = (MainActivity) getActivity();
            Challenge challenge = new Challenge("Blog", blogSelected.getName(), blogSelected.getPicture(), Calendar.getInstance().getTime(), 0, 10, true, activity.user.getFullName());
            callApi(challenge);

            String url = blogSelected.getWebsite();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        private void callApi(Challenge challenge) {
            MainActivity activity = (MainActivity) getActivity();
            Call<User> challengeCall = service.postChallenge(activity.token.getUserid(), challenge);
            challengeCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        clearSharedPreferences();
                        startFeedFragment();
                        Log.i("SUCCESS", "Blog challenge posted to user");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("FAILURE", t.toString());
                }
            });
        }
    }
}
