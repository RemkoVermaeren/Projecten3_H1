package com.example.pdepu.veganapp_p3_h1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Blog;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ChooseBlogAdapter;

import java.util.ArrayList;
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

    public static ChooseBlogFragment.ListFragmentOnClickListener listFragmentOnClickListener;
    private LinearLayoutManager layoutManager;
    private ChooseBlogAdapter adapter;

   @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
       listFragmentOnClickListener = new ListFragmentOnClickListener(getContext());
        callBlogApi();
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

    private void callBlogApi() {
        Call<List<Blog>> blogCall = service.getAllBlogs();
        blogCall.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                if(response.isSuccessful()){
                    blogs.clear();

                    ArrayList<Blog> blogsResponse = new ArrayList<Blog>(response.body());
                    Collections.sort(blogsResponse, new Comparator<Blog>() {
                        @Override
                        public int compare(Blog obj1, Blog obj2) {
                            return obj2.getDate().compareTo(obj1.getDate());
                        }
                    });

                    blogs.addAll(blogsResponse);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });
    }

    public class ListFragmentOnClickListener implements View.OnClickListener{
        private final Context context;

        public ListFragmentOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Blog blogSelected = blogs.get(chooseBlogRecyclerView.getChildAdapterPosition(view));

            Challenge challenge = new Challenge("Blog", blogSelected.getName() , blogSelected.getPicture(), Calendar.getInstance().getTime(), 0, 10, true);
            callApi(challenge);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(blogSelected.getWebsite()));
            startActivity(intent);
        }

        private void callApi(Challenge challenge) {
            MainActivity activity = (MainActivity)getActivity();
            Call<User> challengeCall = service.postChallenge(activity.token.getUserid(), challenge);
            challengeCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
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
