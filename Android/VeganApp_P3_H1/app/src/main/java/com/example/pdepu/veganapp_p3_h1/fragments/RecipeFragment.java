package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 9/08/2017.
 */

public class RecipeFragment extends Fragment {

    private Recipe recipe;
    private String recipeString;
    private Service service;
    @BindView(R.id.recipeIngredients)
    TextView recipeIngredientsTextView;

    @BindView(R.id.recipeInstructions)
    TextView recipeInstructionsTextView;

    @BindView(R.id.recipeDetails)
    TextView recipeDetails;

    @BindView(R.id.claimYourPointsButton)
    Button claimYourPointsButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeString = getArguments().getString("recipe");
        }
        service = new ServicesInitializer().initializeService();
        callApi();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @OnClick(R.id.claimYourPointsButton)
    public void onClick(){
        PublishRecipeFragment publishRecipeFragment = new PublishRecipeFragment();
        Bundle extras = new Bundle();
        extras.putString("recipeName",recipe.getName());
        extras.putString("recipePoints",String.valueOf(recipe.getVeganPoints()));
        publishRecipeFragment.setArguments(extras);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, publishRecipeFragment ).addToBackStack(null).commit();
    }

    private void callApi(){
        final Call<Recipe> recipeCall = service.getRecipe(recipeString);
        recipeCall.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()){
                    recipe = response.body();
                    updateView(recipe);
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private String getDetails(Recipe recipe){
        int time = (int)recipe.getTime() % 60;
        int exactTime = ((int)recipe.getTime() / 60);
        StringBuilder builder = new StringBuilder();
        builder.append(recipe.getName() + "\n");
        builder.append("Difficulty: " + recipe.getDifficulty() + "\n");
        builder.append(String.valueOf(recipe.getCalories()) + " calories" +  "\n");
        builder.append(String.valueOf(exactTime) + " hour " + String.valueOf(time) + " minutes" + "\n");
        builder.append(String.valueOf(recipe.getVeganPoints()) + " points" + "\n");
        builder.append("Type: " + String.valueOf(recipe.getType()) + "\n");
        return builder.toString();
    }

    private String getIngredients(Recipe recipe){
        StringBuilder builder = new StringBuilder();
        for (String ingredient : recipe.getFood()){
            builder.append(ingredient + "\n");
        }
        return builder.toString();
    }

    private String getInstructions(Recipe recipe){
        StringBuilder builder = new StringBuilder();
        for (String instruction : recipe.getInstructions()){
            builder.append(instruction + "\n");
        }
        return builder.toString();
    }

    private void updateView(Recipe recipe){
        recipeDetails.setText(getDetails(recipe));
        recipeIngredientsTextView.setText(getIngredients(recipe));
        recipeInstructionsTextView.setText(getInstructions(recipe));
    }
}
