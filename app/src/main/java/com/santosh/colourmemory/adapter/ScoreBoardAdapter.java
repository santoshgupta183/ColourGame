package com.santosh.colourmemory.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.santosh.colourmemory.databinding.ScoreBoardListItemBinding;
import com.santosh.colourmemory.model.GameScore;

public class ScoreBoardAdapter extends ListAdapter<GameScore, ScoreBoardAdapter.ScoreBoardVH> {

    public ScoreBoardAdapter(){
        super(ITEM_CALLBACK);
    }

    @NonNull
    @Override
    public ScoreBoardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScoreBoardListItemBinding binding = ScoreBoardListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ScoreBoardVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreBoardVH holder, int position) {
        holder.bindingView.playerRank.setText(String.valueOf(position+1));
        holder.bindingView.playerName.setText(getItem(position).getName());
        holder.bindingView.score.setText(String.valueOf(getItem(position).getScore()));
    }

    class ScoreBoardVH extends RecyclerView.ViewHolder{
        private ScoreBoardListItemBinding bindingView;

        public ScoreBoardVH(@NonNull ScoreBoardListItemBinding itemView) {
            super(itemView.getRoot());
            bindingView = itemView;
        }
    }

    private final static DiffUtil.ItemCallback<GameScore> ITEM_CALLBACK = new DiffUtil.ItemCallback<GameScore>(){
        @Override
        public boolean areItemsTheSame(@NonNull GameScore oldItem, @NonNull GameScore newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull GameScore oldItem, @NonNull GameScore newItem) {
            return newItem.equals(oldItem);
        }
    };

}
