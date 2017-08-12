package com.example.pdepu.veganapp_p3_h1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.fragments.ChallengeFragment;
import com.example.pdepu.veganapp_p3_h1.fragments.FeedFragment;
import com.example.pdepu.veganapp_p3_h1.fragments.LeaderboardFragment;
import com.example.pdepu.veganapp_p3_h1.fragments.ProfileFragment;
import com.example.pdepu.veganapp_p3_h1.fragments.SearchFragment;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    private Handler handler;

    public Token token;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new Handler();
        service = new ServicesInitializer().initializeService();
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            token = new Gson().fromJson(extras.getString("tokenString"), Token.class);
        getUser(token);


        View headerView = navigationViewHeader.getHeaderView(0);
        RelativeLayout relativeLayout = (RelativeLayout) headerView.findViewById(R.id.nav_header_relativelayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfileFragment();
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }, 200);
            }
        });


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
            createFeedFragment();
            // Handle the camera action
        } else if (id == R.id.nav_challenge) {
            createChallengeFragment();
        } else if (id == R.id.nav_leaderboard) {
            createLeaderboardFragment();
        } else if (id == R.id.nav_search) {
            createSearchFragment();
        } else if (id == R.id.nav_signout) {
            createLoginActivity();
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer(GravityCompat.START);
            }
        }, 200);

        return true;
    }

    private void getUser(final Token token) {
        Call<User> userCall = service.getUserById(token.getUserid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    user.setToken(token);
                    updateView(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void createProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        profileFragment.setArguments(extras);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
    }

    private void createFeedFragment() {
        FeedFragment feedFragment = new FeedFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, feedFragment).commit();
    }

    private void createLoginActivity() {
        user.setToken(null);
        this.user = null;
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    private void createLeaderboardFragment() {
        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, leaderboardFragment).commit();
    }

    private void createChallengeFragment() {
        ChallengeFragment challengeFragment = new ChallengeFragment();
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        challengeFragment.setArguments(extras);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, challengeFragment).commit();
    }

    private void createSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        searchFragment.setArguments(extras);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();
    }


    public void updateView(User user) {
        View headerView = navigationViewHeader.getHeaderView(0);
        textViewUsername = (TextView) headerView.findViewById(R.id.username);
        textViewFollowerAmount = (TextView) headerView.findViewById(R.id.followerAmount);
        imageViewUser = (CircleImageView) headerView.findViewById(R.id.imageViewUser);
        if (user.getImage() != null && !user.getImage().isEmpty())
            Picasso.with(imageViewUser.getContext()).load(user.getImage()).fit().into(imageViewUser);
        textViewUsername.setText(user.getName() + " " + user.getSurName());
        textViewFollowerAmount.setText(String.valueOf(user.getFollowingUsers().length) + " followers");
    }


}
