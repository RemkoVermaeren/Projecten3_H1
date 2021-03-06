package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pdepu.veganapp_p3_h1.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pdepu on 9/08/2017.
 */

public class ChallengeFragment extends Fragment {

    @BindView(R.id.makeFood)
    Button makeFoodButton;

    @BindView(R.id.goToDinner)
    Button goToDinnerButton;

    @BindView(R.id.goToBlogs)
    Button goToBooksAndBlogs;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_challenge, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @OnClick(R.id.makeFood)
    public void onClick(){
        ChooseRecipeFragment fragment = new ChooseRecipeFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.goToDinner)
    public void onClickDinner(){
        ChooseRestaurantFragment fragment = new ChooseRestaurantFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.goToBlogs)
    public void onClickBooksAndBlogs(){
        ChooseBlogFragment fragment = new ChooseBlogFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}
