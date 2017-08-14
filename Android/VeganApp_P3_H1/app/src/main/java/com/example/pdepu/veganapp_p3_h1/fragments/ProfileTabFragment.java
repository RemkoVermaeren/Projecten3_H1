package com.example.pdepu.veganapp_p3_h1.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pdepu on 13/08/2017.
 */

public class ProfileTabFragment extends Fragment {

    private Token token;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profiletab, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ProfileTabFragment.ViewPagerAdapter adapter = new ProfileTabFragment.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(createProfileFragment(), "Account");
        adapter.addFragment(createProfileChallengeFragment(), "Challenges");
        adapter.addFragment(createFollowerFragment(), "Followers");
        viewPager.setAdapter(adapter);
    }

    private ProfileFragment createProfileFragment(){
        ProfileFragment profileFragment = new ProfileFragment();
        setExtras(profileFragment);
        return profileFragment;
    }

    private FollowerFragment createFollowerFragment(){
        FollowerFragment followerFragment = new FollowerFragment();
        setExtras(followerFragment);
        return followerFragment;
    }

    private ProfileChallengesFragment createProfileChallengeFragment(){
        ProfileChallengesFragment profileChallengesFragment = new ProfileChallengesFragment();
        setExtras(profileChallengesFragment);
        return profileChallengesFragment;

    }

    private void setExtras(Fragment fragment){
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        fragment.setArguments(extras);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
