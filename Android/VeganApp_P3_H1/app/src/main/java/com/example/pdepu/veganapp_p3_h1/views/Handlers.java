package com.example.pdepu.veganapp_p3_h1.views;

import android.util.Log;
import android.view.View;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pdepu on 7/08/2017.
 */

public class Handlers {

    private Service service;
    private User user;
    private User follower;
    private boolean toAdd;

    public Handlers(User user, User follower) {
        this.service = new ServicesInitializer().initializeService();
        this.user = user;
        this.follower = follower;
        this.toAdd = false;
    }

    public void onClickFriend(final View view) {
        Call<User> usersCall = service.addFollower(user.get_id(), follower.get_id(),user.getToken().getToken());
        usersCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() && toAdd){
                    setFollowers(follower.get_id(), false);
                    view.setBackgroundResource(R.drawable.ic_menu_close_clear_cancel);

                }

                else{
                    view.setBackgroundResource(R.drawable.ic_checkmark_holo_light);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error network", t.toString());
            }
        });

    }

    public void toAddOrDelete(final View view){
        boolean toDelete = user.IsFollower(follower.get_id());
        if(toDelete)
            onDeleteFriend(view);
        else{
            toAdd = true;
            onClickFriend(view);
        }

    }

    private void setFollowers(String followerid, boolean toDelete){
        if(toDelete){
            ArrayList<String> followers = new ArrayList<String>(Arrays.asList(user.getFollowingUsers()));
            followers.remove(follower.get_id());
            user.setFollowingUsers(followers.toArray(new String[followers.size()]));
        }else{
            ArrayList<String> followers = new ArrayList<String>(Arrays.asList(user.getFollowingUsers()));
            followers.add(follower.get_id());
            user.setFollowingUsers(followers.toArray(new String[followers.size()]));
            toAdd = false;
        }

    }

    public void onDeleteFriend(final View view) {
        Call<User> usersCall = service.deleteFollower(user.get_id(), follower.get_id(),user.getToken().getToken());
        usersCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    setFollowers(follower.get_id(), true);
                    view.setBackgroundResource(R.drawable.add_follower_drawable);

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error network", t.toString());
            }
        });

    }


}
