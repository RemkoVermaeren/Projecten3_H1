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
        Call usersCall = service.addFollower(user.getId(), follower.getId());
        usersCall.enqueue(new Callback() {
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
}
