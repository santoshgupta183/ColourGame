package com.santosh.colourmemory.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santosh.colourmemory.R;
import com.santosh.colourmemory.adapter.ScoreBoardAdapter;
import com.santosh.colourmemory.databinding.FragmentScoreBoardBinding;
import com.santosh.colourmemory.ui.ColourMemoryActivity;
import com.santosh.colourmemory.viewmodel.ColourMemoryViewModel;

public class ScoreBoardFragment extends Fragment {

    private FragmentScoreBoardBinding binding;
    private ColourMemoryViewModel colourMemoryViewModel;
    String TAG = "COLOUR GAME";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScoreBoardBinding.inflate(inflater, container, false);
//        setHasOptionsMenu(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.VISIBLE);
        ((ColourMemoryActivity) getActivity()).binding.tvScore.setVisibility(View.GONE);

        colourMemoryViewModel = ViewModelProviders.of(this).get(ColourMemoryViewModel.class);

        ScoreBoardAdapter scoreBoardAdapter = new ScoreBoardAdapter();
        binding.scoreList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.scoreList.setAdapter(scoreBoardAdapter);
        binding.scoreList.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        colourMemoryViewModel.getGameScoreListLiveData().observe(this, scoreList ->{
            binding.progressBar.setVisibility(View.INVISIBLE);
            if (scoreList != null && !scoreList.isEmpty()) {
                scoreBoardAdapter.submitList(scoreList);
            } else {
                Log.e(TAG, "onViewCreated : scoreList empty");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.game_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.scoreBoardFragment).setVisible(false);
    }
}
