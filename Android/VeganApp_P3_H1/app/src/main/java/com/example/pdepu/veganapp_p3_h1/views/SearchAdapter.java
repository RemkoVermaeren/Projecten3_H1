package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.databinding.FragmentSearchCardviewBinding;
import com.example.pdepu.veganapp_p3_h1.models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by pdepu on 4/08/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<User> users;

    private final SortedList<User> userSortedList = new SortedList<>(User.class, new SortedList.Callback<User>() {
        @Override
        public int compare(User a, User b) {
            return comparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(User oldItem, User newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(User item1, User item2) {
            return item1.getId() == item2.getId();
        }
    });

    private final LayoutInflater inflater;
    private final Comparator<User> comparator;

    public SearchAdapter(Context context, Comparator<User> comparator) {
        inflater = LayoutInflater.from(context);
        this.comparator = comparator;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{

//        @BindView(R.id.imageViewUserCardViewSearch)
//        CircleImageView imageViewUserCardView;
//
//        @BindView(R.id.usernameCardViewSearch)
//        TextView usernameCardView;
//
//        @BindView(R.id.veganPointsCardViewSearch)
//        TextView veganPointsCardView;

        private final FragmentSearchCardviewBinding binding;


        public SearchViewHolder(FragmentSearchCardviewBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user){
            this.binding.setModel(user);
        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_cardview,parent,false);
        //return new SearchViewHolder(view);
        final FragmentSearchCardviewBinding binding = FragmentSearchCardviewBinding.inflate(inflater,parent,false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position){
//        User user = users.get(position);
//        CircleImageView image = holder.imageViewUserCardView;
//        TextView username = holder.usernameCardView;
//        TextView veganScore = holder.veganPointsCardView;
//        Context context = holder.imageViewUserCardView.getContext();
//        //username.setText(String.valueOf(position) + ". " + user.getName() + " " + user.getSurName());
//        veganScore.setText(String.valueOf(user.getTotalVeganScore()) + " points");
        final User user = userSortedList.get(position);
        holder.bind(user);

    }



    @Override
    public int getItemCount(){
        return userSortedList.size();
    }

    public void add(User model) {
        userSortedList.add(model);
    }

    public void remove(User model) {
        userSortedList.remove(model);
    }

    public void add(List<User> models) {
        userSortedList.addAll(models);
    }

    public void remove(List<User> models) {
        userSortedList.beginBatchedUpdates();
        for (User model : models) {
            userSortedList.remove(model);
        }
        userSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<User> models) {
        userSortedList.beginBatchedUpdates();
        for (int i = userSortedList.size() - 1; i >= 0; i--) {
            final User model = userSortedList.get(i);
            if (!models.contains(model)) {
                userSortedList.remove(model);
            }
        }
        userSortedList.addAll(models);
        userSortedList.endBatchedUpdates();
    }




}

