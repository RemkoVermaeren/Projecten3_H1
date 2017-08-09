package com.example.pdepu.veganapp_p3_h1.network;

import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.models.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by pdepu on 1/08/2017.
 */

public interface Service {

    //region HANDLE RECIPES
    @GET("recipes")
    Call<List<Recipe>> getRecipesList();
    //endregion

    //region HANDLE USERS
    @FormUrlEncoded
    @POST("users/login")
    Call<Token> loginUser(@Field("username") String username, @Field("password") String password);

    @POST("users/register")
    Call<Token> registerUser(@Body User user);

    @GET("users/{userid}")
    Call<User> getUserById(@Path("userid") String userid);

    @GET("users")
    Call<List<User>> getAllUsers();

    @GET("users/{user}/followers")
    Call<List<User>> getAllFollowers(@Path("user") String userid);

    @FormUrlEncoded
    @POST("users/{user}/followers/add/{userfollow}")
    Call<User> addFollower(@Path("user") String userid, @Path("userfollow") String followerid, @Field("token") String token);

    @FormUrlEncoded
    @POST("users/{user}/followers/remove/{userfollow}")
    Call<User> deleteFollower(@Path("user") String userid, @Path("userfollow") String followerid, @Field("token") String token);
    //endregion


}
