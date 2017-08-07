package com.example.pdepu.veganapp_p3_h1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.LeaderboardFragment;
import com.example.pdepu.veganapp_p3_h1.fragments.SearchFragment;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Service service;

    @BindView(R.id.nav_view)
    NavigationView navigationViewHeader;

    //@BindView(R.id.username)
    TextView textViewUsername;

    //@BindView(R.id.followerAmount)
    TextView textViewFollowerAmount;

    //@BindView(R.id.imageViewUser)
    ImageView imageViewUser;

    private Token token;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        service = new ServicesInitializer().initializeService();
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            token = new Gson().fromJson(extras.getString("tokenString"), Token.class);
        getUser(token);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            // Handle the camera action
        } else if (id == R.id.nav_challenge) {

        } else if (id == R.id.nav_leaderboard) {
            LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, leaderboardFragment).commit();
        } else if (id == R.id.nav_search) {
            SearchFragment searchFragment = new SearchFragment();
            Bundle extras = new Bundle();
            extras.putString("user", new Gson().toJson(user));
            searchFragment.setArguments(extras);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();

        } else if(id == R.id.nav_signout){
            user.setToken(null);
            this.user = null;
            Intent loginActivity = new Intent(this,LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUser(Token token) {
        Call<User> userCall = service.getUserById(token.getUserid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    Log.i("iet", response.body().toString());
                    updateView(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }


    private void updateView(User user) {
        View headerView = navigationViewHeader.getHeaderView(0);
        textViewUsername = (TextView)headerView.findViewById(R.id.username);
        textViewFollowerAmount = (TextView)headerView.findViewById(R.id.followerAmount);
        imageViewUser = (CircleImageView)headerView.findViewById(R.id.imageViewUser);
        textViewUsername.setText(user.getName() + " " + user.getSurName());
        textViewFollowerAmount.setText(String.valueOf(user.getFollowingUsers().length) + " followers");
    }
}
