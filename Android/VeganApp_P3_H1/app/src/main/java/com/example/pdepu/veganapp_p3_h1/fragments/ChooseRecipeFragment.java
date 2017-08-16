package com.example.pdepu.veganapp_p3_h1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ChooseRecipeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 9/08/2017.
 */

public class ChooseRecipeFragment extends Fragment {

    private Service service;
    private ArrayList<Recipe> recipes = new ArrayList<>();

    @BindView(R.id.chooseRecylerView)
    RecyclerView chooseRecipeRecyclerView;


    public static ListFragmentOnClickListener listFragmentOnClickListener;
    private LinearLayoutManager layoutManager;
    private ChooseRecipeAdapter adapter;
    private String recipeToPass;

    private int currentIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        listFragmentOnClickListener = new ListFragmentOnClickListener(this.getContext());
        callApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_choose, container, false);
        ButterKnife.bind(this,rootView);

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ChooseRecipeAdapter(recipes);

        chooseRecipeRecyclerView.setLayoutManager(layoutManager);
        chooseRecipeRecyclerView.setAdapter(adapter);

        return rootView;

    }

    private class ListFragmentOnClickListener implements View.OnClickListener {
        private final Context context;

        public ListFragmentOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            currentIndex = chooseRecipeRecyclerView.getChildAdapterPosition(view);
            RecipeFragment recipeFragment;

            recipeFragment = new RecipeFragment();
            Bundle extras = new Bundle();
            recipeToPass = recipes.get(currentIndex).get_id();
            extras.putString("recipe", recipeToPass);
            recipeFragment.setArguments(extras);


            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, recipeFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void callApi() {
        final Call<List<Recipe>> recipeCall = service.getRecipesList();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Recipe> recipeResponse = new ArrayList<Recipe>(response.body());
                    if (recipeResponse.size() > 4)
                        recipes.addAll(getRandomRecipes(recipeResponse));
                    else
                        recipes.addAll(recipeResponse);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private ArrayList<Recipe> getRandomRecipes(ArrayList<Recipe> recipes){
        Collections.shuffle(recipes);
        return new ArrayList<>(recipes.subList(0,5));
    }


}
