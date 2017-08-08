package com.example.pdepu.veganapp_p3_h1.views;

import android.util.Log;
import android.view.View;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;

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

    public Handlers(User user, User follower) {
        this.service = new ServicesInitializer().initializeService();
        this.user = user;
        this.follower = follower;
    }

    public void onClickFriend(final View view) {
        Call<User> usersCall = service.addFollower(user.get_id(), follower.get_id(),user.getToken().getToken());
        usersCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful())
                    view.setBackgroundResource(R.drawable.ic_checkmark_holo_light);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Error network", t.toString());
            }
        });

    }

//    public boolean checkIfFollow(View v){
//        for(String userid : user.getFollowingUsers()){
//            if(userid.equals(follower.get_id()))
//                v.setBackgroundResource(R.drawable.ic_checkmark_holo_light);
//            else{
//                v.setBackgroundResource(R.drawable.add_follower_drawable);
//            }
//        }
//    }
}
