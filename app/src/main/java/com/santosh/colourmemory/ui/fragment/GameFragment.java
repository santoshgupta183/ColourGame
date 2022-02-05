package com.santosh.colourmemory.ui.fragment;

import static com.santosh.colourmemory.utils.Constants.MATRIX_SIZE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.santosh.colourmemory.R;
import com.santosh.colourmemory.databinding.DialogShowCurrentRankBinding;
import com.santosh.colourmemory.databinding.DialogSubmitScoreBinding;
import com.santosh.colourmemory.databinding.FragmentGameBinding;
import com.santosh.colourmemory.model.CardData;
import com.santosh.colourmemory.ui.ColourMemoryActivity;
import com.santosh.colourmemory.utils.Constants;
import com.santosh.colourmemory.viewmodel.ColourMemoryViewModel;

public class GameFragment extends Fragment {

    private FragmentGameBinding fragmentGameBinding;
    private ColourMemoryViewModel colourMemoryViewModel;
    private int currentScore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentGameBinding = FragmentGameBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });
        return fragmentGameBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        ((ColourMemoryActivity) getActivity()).binding.tvScore.setVisibility(View.VISIBLE);

        fragmentGameBinding.rootLayout.setRowCount(4);
        fragmentGameBinding.rootLayout.setColumnCount(4);

        for (int i = 0; i < MATRIX_SIZE; i ++){
            for (int j = 0; j< MATRIX_SIZE; j++){
                View view = new View(this.getContext());
                view.setLayoutParams(new ViewGroup.LayoutParams(0,0));
                view.setBackground(getResources().getDrawable(R.drawable.card_bg));
                view.setId((i*4)+j);
                view.setOnClickListener(view1 -> colourMemoryViewModel.onCardClicked(view1.getId()));

                GridLayout.Spec rowSpec = GridLayout.spec(i, 1f);
                GridLayout.Spec colSpec = GridLayout.spec(j, 1f);

                GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams();
                myGLP.rowSpec = rowSpec;
                myGLP.columnSpec = colSpec;
                myGLP.width = 0;
                myGLP.height = 0;

                fragmentGameBinding.rootLayout.addView(view, myGLP);

            }
        }

        colourMemoryViewModel = ViewModelProviders.of(this).get(ColourMemoryViewModel.class);

        colourMemoryViewModel.getSelectedCardsList().observe(this, cardDataList -> {
            if (cardDataList != null && !cardDataList.isEmpty()) {
                for (CardData card : cardDataList) {
                    View selectedCardView = fragmentGameBinding.rootLayout.getChildAt(card.getIndex());
                    if (card != null) {
                        if (card.getCardValue() == Constants.CARD_REMOVE) {
                            selectedCardView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.white));
                        } else if (card.getCardValue() == Constants.CARD_CLOSE) {
                            selectedCardView.setBackground(getResources().getDrawable(getColourImage(-1)));
                        } else {
                            selectedCardView.setBackground(getResources().getDrawable(getColourImage(card.getCardValue())));

                        }
                    } else {
                        selectedCardView.setBackground(getResources().getDrawable(getColourImage(-1)));
                    }
                }
            }

            onScreenUpdate();
        });

        colourMemoryViewModel.getCurrentGameStateLiveData().observe(this, currentGameState -> {
            if (currentGameState.getRank() > 0){
                showCurrentRank(currentGameState.getRank(), currentGameState.getScore());
            } else {
                currentScore = currentGameState.getScore();
                ((ColourMemoryActivity) getActivity()).binding.tvScore.setText(String.format(getResources().getString(R.string.current_score_text), currentScore));

                if (currentGameState.isGameOver()){
                    showNameInputDialog();
                }
            }
        });

    }

    private void onScreenUpdate() {
        colourMemoryViewModel.onScreenUpdated();
    }

    private void showNameInputDialog() {
        Dialog dialog = new Dialog(this.getContext());
        DialogSubmitScoreBinding dialogBinding = DialogSubmitScoreBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialogBinding.submitBtn.setOnClickListener(view -> {
            String name = dialogBinding.etName.getText().toString();
            if (name != null && !name.isEmpty()) {
                colourMemoryViewModel.onSubmitScore(name, currentScore);
                dialog.dismiss();
            } else {
                Snackbar.make(fragmentGameBinding.getRoot(), "Please enter Name!!!", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showCurrentRank(int rank, int score){
        Dialog dialog = new Dialog(this.getContext());
        DialogShowCurrentRankBinding dialogBinding = DialogShowCurrentRankBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogBinding.currentScore.setText(String.format(getResources().getString(R.string.current_score_dialog_text), score));
        dialogBinding.rankValue.setText(String.valueOf(rank));
        dialogBinding.okBtn.setOnClickListener(view -> {
            colourMemoryViewModel.resetGame();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showExitDialog(){
        new AlertDialog.Builder(this.getContext())
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialogInterface, i) -> GameFragment.this.getActivity().finish())
                .setNegativeButton("No", null)
                .show();
    }

    private int getColourImage(int index){
        switch (index) {
            case 0:
                return R.drawable.colour1;
            case 1:
                return R.drawable.colour2;
            case 2:
                return R.drawable.colour3;
            case 3:
                return R.drawable.colour4;
            case 4:
                return R.drawable.colour5;
            case 5:
                return R.drawable.colour6;
            case 6:
                return R.drawable.colour7;
            case 7:
                return R.drawable.colour8;
            default:
                return R.drawable.card_bg;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.game_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, ((ColourMemoryActivity) getActivity()).navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.scoreBoardFragment).setVisible(true);
    }
}
