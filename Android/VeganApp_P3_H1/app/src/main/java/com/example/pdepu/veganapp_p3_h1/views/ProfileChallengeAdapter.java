package com.example.pdepu.veganapp_p3_h1.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pdepu on 13/08/2017.
 */

public class ProfileChallengeAdapter extends RecyclerView.Adapter<ProfileChallengeAdapter.ProfileChallengeViewHolder> {

    private ArrayList<Challenge> challenges;

    public ProfileChallengeAdapter(ArrayList<Challenge> challenges) {
        this.challenges = challenges;
    }

    public static class ProfileChallengeViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.challengeName)
        TextView challengeName;

        @BindView(R.id.challengeVeganPoints)
        TextView challengeVeganPoints;

        public ProfileChallengeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ProfileChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_challenge_view, parent, false);
        return new ProfileChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileChallengeViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        TextView challengeName = holder.challengeName;
        TextView challengeVeganPoints = holder.challengeVeganPoints;
        challengeName.setText(challenge.getName() + " " +  challenge.getDescription());
        challengeVeganPoints.setText("+" + String.valueOf(challenge.getVeganScore()) + " points");

    }


    @Override
    public int getItemCount() {
        return challenges.size();
    }
}

