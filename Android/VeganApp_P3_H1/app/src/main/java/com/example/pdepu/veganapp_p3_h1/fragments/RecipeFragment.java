package com.example.pdepu.veganapp_p3_h1.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.UriHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

    @BindView(R.id.recipeName)
    TextView recipeName;

    @BindView(R.id.recipeDifficulty)
    TextView recipeDifficulty;

    @BindView(R.id.recipeTime)
    TextView recipeTime;

    @BindView(R.id.recipePoints)
    TextView recipePoints;

    @BindView(R.id.recipeDetails)
    TextView recipeDetails;

    @BindView(R.id.recipeAllergies)
    TextView recipeAllergies;

    @BindView(R.id.claimYourPointsButton)
    Button claimYourPointsButton;

    @BindView(R.id.recipeImage)
    ImageView recipeImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            recipeString = getArguments().getString("recipe");
            callApi();
        } else
            checkSharedPreferences();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        checkSharedPreferences();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.claimYourPointsButton)
    public void onClick() {
        PublishRecipeFragment publishRecipeFragment = new PublishRecipeFragment();
        Bundle extras = new Bundle();
        extras.putString("recipeName", recipe.getName());
        extras.putString("recipePoints", String.valueOf(recipe.getVeganPoints()));
        publishRecipeFragment.setArguments(extras);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, publishRecipeFragment).addToBackStack(null).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        setSharedPreferences();
    }


    private void callApi() {
        Call<Recipe> recipeCall = service.getRecipe(recipeString);
        recipeCall.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful()) {
                    recipe = response.body();
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private String getDetails(Recipe recipe) {
        StringBuilder builder = new StringBuilder();
        builder.append("Calories: " + String.valueOf(recipe.getCalories()) + "\n");
        builder.append("Type: " + String.valueOf(recipe.getType()) + "\n");
        return builder.toString();
    }

    private String getAllergies(Recipe recipe){
        if(recipe.getAllergies().length <= 0)
            return "No allergies";
        else{
            StringBuilder builder = new StringBuilder();
            for(String allergie : recipe.getAllergies())
                builder.append(allergie + "\n");
            return builder.toString();
        }
    }

    private String getTime(Recipe recipe){
        String timeString = "";

        if (recipe.getTime() >= 60) {
            int time = (int) recipe.getTime() % 60;
            int exactTime = ((int) recipe.getTime() / 60);
            timeString = String.valueOf(exactTime) + "h" + String.valueOf(time) + "m";

        } else
            timeString = String.valueOf((int) recipe.getTime()) + "m";

        return timeString;
    }

    private String getIngredients(Recipe recipe) {
        StringBuilder builder = new StringBuilder();
        for (String ingredient : recipe.getFood()) {
            builder.append(ingredient + "\n");
        }
        return builder.toString();
    }

    private String getInstructions(Recipe recipe) {
        StringBuilder builder = new StringBuilder();
        for (String instruction : recipe.getInstructions()) {
            builder.append(instruction + "\n");
        }
        return builder.toString();
    }

    private void updateView() {
        if (recipe.getPicture() != null && !recipe.getPicture().isEmpty())
            Picasso.with(recipeImage.getContext()).load(UriHandler.resizeUrl(recipe.getPicture(),
                    String.valueOf(Resources.getSystem().getDisplayMetrics().widthPixels),
                    String.valueOf(((int) TypedValue.applyDimension(TypedValue.DENSITY_DEFAULT, 185, Resources.getSystem().getDisplayMetrics()))))).fit().into(recipeImage);
        recipeName.setText(recipe.getName());
        recipeDifficulty.setText(recipe.getDifficulty());
        recipeTime.setText(getTime(recipe));
        recipePoints.setText(String.valueOf(recipe.getVeganPoints()));
        recipeDetails.setText(getDetails(recipe));
        recipeAllergies.setText(getAllergies(recipe));
        recipeIngredientsTextView.setText(getIngredients(recipe));
        recipeInstructionsTextView.setText(getInstructions(recipe));
    }

    private void setSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        preferences.edit().putString("recipePreferences", new Gson().toJson(recipe)).apply();
    }

    private void checkSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        if (prefs.getString("recipePreferences", null) != null) {
            recipe = new Gson().fromJson(prefs.getString("recipePreferences", null), Recipe.class);
            callApi();
        }
    }


}
