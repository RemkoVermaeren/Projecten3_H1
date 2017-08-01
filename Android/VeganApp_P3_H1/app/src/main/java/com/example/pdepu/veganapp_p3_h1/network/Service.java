package com.example.pdepu.veganapp_p3_h1.network;

import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.example.pdepu.veganapp_p3_h1.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by pdepu on 1/08/2017.
 */

public interface Service {

    @GET("recipes")
    Call<List<Recipe>> getRecipesList();


    @FormUrlEncoded
    @POST("users/login")
    Call<User> getUser(@Field("username") String username, @Field("password") String password);


}
