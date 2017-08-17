package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pdepu on 4/08/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private ArrayList<User> users;

    public LeaderboardAdapter(ArrayList<User> users) {
        this.users = users;
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.positionCardView)
        TextView positionCardView;

        @BindView(R.id.imageViewUserCardView)
        CircleImageView imageViewUserCardView;

        @BindView(R.id.usernameCardView)
        TextView usernameCardView;

        @BindView(R.id.veganPointsCardView)
        TextView veganPointsCardView;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_leaderboard_cardview, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderboardViewHolder holder, int position) {
        User user = users.get(position);
        TextView number = holder.positionCardView;
        CircleImageView image = holder.imageViewUserCardView;
        TextView username = holder.usernameCardView;
        TextView veganScore = holder.veganPointsCardView;
        Context context = holder.imageViewUserCardView.getContext();
        number.setText(String.valueOf(position + 1));
        if (user.getImage() != null && !user.getImage().isEmpty())
        {
            Picasso.with(context).load(UriHandler.resizeUrl(user.getImage(), "60", "60")).into(image);
        }else
            image.setImageResource(R.drawable.avatar_placeholder_small);

        username.setText(user.getName() + " " + user.getSurName());
        veganScore.setText(String.valueOf(user.getTotalVeganScore()) + " points");

    }




    @Override
    public int getItemCount() {
        return users.size();
    }
}
