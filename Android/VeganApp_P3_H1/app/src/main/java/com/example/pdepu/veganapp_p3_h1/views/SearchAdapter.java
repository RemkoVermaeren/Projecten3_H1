package com.example.pdepu.veganapp_p3_h1.views;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.databinding.FragmentSearchCardviewBinding;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pdepu on 4/08/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {


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
            return item1.get_id() == item2.get_id();
        }
    });

    private final LayoutInflater inflater;
    private final Comparator<User> comparator;
    private User userOriginal;
    private MainActivity activity;

    public SearchAdapter(Context context, Comparator<User> comparator, User user, MainActivity activity) {
        inflater = LayoutInflater.from(context);
        this.comparator = comparator;
        this.userOriginal = user;
        this.activity = activity;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{

        private final FragmentSearchCardviewBinding binding;

        @BindView(R.id.imageViewUserCardViewSearch)
        CircleImageView imageViewUserCardViewSearch;


        public SearchViewHolder(FragmentSearchCardviewBinding binding){
            super(binding.getRoot());
            ButterKnife.bind(this,binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, Handlers handlers, User original){
            this.binding.setHandler(handlers);
            this.binding.setModel(user);
            this.binding.setOriginal(original);


        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        final FragmentSearchCardviewBinding binding = FragmentSearchCardviewBinding.inflate(inflater,parent,false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position){
        final User user = userSortedList.get(position);
        CircleImageView image = holder.imageViewUserCardViewSearch;
        if (user.getImage() != null && !user.getImage().isEmpty())
            Picasso.with(image.getContext()).load(user.getImage()).resize(60,60).into(image);
        holder.bind(user, new Handlers(userOriginal,user, activity),userOriginal);

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

    public void setUserOriginal(User user){
        this.userOriginal = user;
    }




}

