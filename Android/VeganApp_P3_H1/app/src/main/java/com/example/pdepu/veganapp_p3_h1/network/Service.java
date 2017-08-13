package com.example.pdepu.veganapp_p3_h1.network;

import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.Recipe;
import com.example.pdepu.veganapp_p3_h1.models.Restaurant;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by pdepu on 1/08/2017.
 */

public interface Service {

    //region HANDLE RECIPES
    @GET("recipes")
    Call<List<Recipe>> getRecipesList();

    @GET("recipes/{recipe}")
    Call<Recipe> getRecipe(@Path("recipe") String recipeId);
    //endregion

    //region HANDLE RESTAURANTS
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurantsList();

    @GET("restaurants/{restaurant}")
    Call<Restaurant> getRestaurant(@Path("restaurant") String restaurantId);
    //endregion


    //region HANDLE CHALLENGES
    @POST("users/{user}/challenges/")
    Call<User> postChallenge(@Path("user") String userId, @Body Challenge challenge);

    @GET("users/{user}/challenges")
    Call<List<Challenge>> getChallengesUser(@Path("user") String userId);
    //endregion

    //region HANDLE USERS
    @FormUrlEncoded
    @POST("users/login")
    Call<Token> loginUser(@Field("username") String username, @Field("password") String password);

    @POST("users/register")
    Call<Token> registerUser(@Body User user);

    @PUT("users/{user}")
    Call<User> updateUser(@Path("user") String userid, @Body User user);

    @GET("users/{userid}")
    Call<User> getUserById(@Path("userid") String userId);

    @GET("users/nonadmins")
    Call<List<User>> getAllUsers();

    @GET("users/{user}/followers")
    Call<List<User>> getAllFollowers(@Path("user") String userId);

    @FormUrlEncoded
    @POST("users/{user}/followers/add/{userfollow}")
    Call<User> addFollower(@Path("user") String userId, @Path("userfollow") String followerId, @Field("token") String token);

    @FormUrlEncoded
    @POST("users/{user}/followers/remove/{userfollow}")
    Call<User> deleteFollower(@Path("user") String userId, @Path("userfollow") String followerId, @Field("token") String token);
    //endregion


}
