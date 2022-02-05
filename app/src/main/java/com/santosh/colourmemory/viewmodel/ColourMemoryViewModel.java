package com.santosh.colourmemory.viewmodel;

import static com.santosh.colourmemory.utils.Constants.CARD_CLOSE;
import static com.santosh.colourmemory.utils.Constants.MATRIX_SIZE;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santosh.colourmemory.model.CardData;
import com.santosh.colourmemory.model.CurrentGameState;
import com.santosh.colourmemory.model.GameScore;
import com.santosh.colourmemory.repository.ScoreRepo;
import com.santosh.colourmemory.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ColourMemoryViewModel extends AndroidViewModel {

    private ScoreRepo scoreRepo;

    private int[][] GAME_MATRIX;
    private int removedCardsCount;
    private boolean roundComplete;

    private CurrentGameState currentGameState = new CurrentGameState();
    private MutableLiveData<CurrentGameState> currentGameStateLiveData = new MutableLiveData<>(currentGameState);

    private List<CardData> selectedCardsList = new ArrayList<>();
    private MutableLiveData<List<CardData>> selectedCardsListLiveData = new MutableLiveData<>();

    private MutableLiveData<List<GameScore>> gameScoreListLiveData = new MutableLiveData<>();

    public ColourMemoryViewModel(@NonNull Application application) {
        super(application);
        scoreRepo = ScoreRepo.getInstance(application.getApplicationContext());
        scoreRepo.getAllScores().observeForever(gameScores -> {
            gameScoreListLiveData.postValue(gameScores);
        });
        GAME_MATRIX = randomArrayWithPairs(MATRIX_SIZE);
        System.out.println(Arrays.deepToString(GAME_MATRIX));
    }

    public LiveData<List<CardData>> getSelectedCardsList() {
        return selectedCardsListLiveData;
    }

    public LiveData<List<GameScore>> getGameScoreListLiveData() {
        return gameScoreListLiveData;
    }

    public LiveData<CurrentGameState> getCurrentGameStateLiveData() {
        return currentGameStateLiveData;
    }

    public void onCardClicked(int cardId) {
        if (!roundComplete && !currentGameState.isGameOver()) {
            CardData firstSelectedCard = null;
            if (selectedCardsList == null) {
                selectedCardsList = new ArrayList<>(2);
            } else if (!selectedCardsList.isEmpty()) {
                firstSelectedCard = selectedCardsList.get(0);
            }
            if (firstSelectedCard == null || firstSelectedCard.getCardValue() == Constants.CARD_REMOVE
                    || firstSelectedCard.getCardValue() == Constants.CARD_CLOSE){
                clearSelectedCardList();
                pushCardInList(new CardData(cardId, getCardValue(cardId)));
                updateLiveDataList();
            } else {
                pushCardInList(new CardData(cardId, getCardValue(cardId)));
                updateLiveDataList();
                Handler handler = new Handler();
                roundComplete = true;
                if(selectedCardsList.get(0).getCardValue() == selectedCardsList.get(1).getCardValue()){
                    handler.postDelayed(() -> {
                        selectedCardsList.get(0).setCardValue(Constants.CARD_REMOVE);
                        selectedCardsList.get(1).setCardValue(Constants.CARD_REMOVE);
                        updateLiveDataList();
                        currentGameState.setScore(currentGameState.getScore() + 2);
                        updateCurrentGameStateLiveData();
                        removeCards();
                    }, 1000);
                } else {
                    handler.postDelayed(() -> {
                        selectedCardsList.get(0).setCardValue(Constants.CARD_CLOSE);
                        selectedCardsList.get(1).setCardValue(Constants.CARD_CLOSE);
                        updateLiveDataList();
                        currentGameState.setScore(currentGameState.getScore()  -1);
                        updateCurrentGameStateLiveData();
                    }, 1000);
                }
            }
        }
    }

    private void updateCurrentGameStateLiveData() {
        currentGameStateLiveData.postValue(currentGameState);
    }

    private void updateLiveDataList() {
        selectedCardsListLiveData.postValue(selectedCardsList);
    }

    private void clearSelectedCardList() {
        selectedCardsList.clear();
    }

    private void pushCardInList(CardData card) {
        selectedCardsList.add(card);
    }

    private void removeCards(){
        removedCardsCount+=2;
        for (CardData cardData : selectedCardsList) {
            GAME_MATRIX[cardData.getIndex()/MATRIX_SIZE][cardData.getIndex()%MATRIX_SIZE] = Constants.CARD_REMOVE;
        }
        if (removedCardsCount>=(MATRIX_SIZE*MATRIX_SIZE)){
        }
        currentGameState.setGameOver(true);
        updateCurrentGameStateLiveData();
    }

    private int getCardValue(int id){
        return GAME_MATRIX[id/MATRIX_SIZE][id%MATRIX_SIZE];
    }

    private int[][] randomArrayWithPairs(int n){
        List<Integer> list = new ArrayList<>();
        for (int i=1; i<n*n; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);

        int[][] randomMatrix = new int [n][n];
        for (int i=0; i<list.size(); i++) {
            randomMatrix[i/n][i%n] = list.get(i)/(n/2);
        }

        return randomMatrix;
    }

    public void onScreenUpdated() {
        roundComplete = false;
    }

    public void onSubmitScore(String name, int score) {

        GameScore newScore = new GameScore(name, score);
        scoreRepo.insertScore(newScore);
        Handler handler = new Handler();
        handler.postDelayed(() -> fetchCurrentScore(newScore), 200);
    }

    public void fetchCurrentScore(GameScore newScore){
        int index = gameScoreListLiveData.getValue().indexOf(newScore)+1;
        currentGameState.setRank(index);
        updateCurrentGameStateLiveData();
    }

    public void resetGame(){
        GAME_MATRIX = randomArrayWithPairs(MATRIX_SIZE);
        roundComplete = false;
        removedCardsCount=0;
        for (int i = 0; i < MATRIX_SIZE; i ++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                pushCardInList(new CardData((i*4)+j, CARD_CLOSE));
            }
        }
        updateLiveDataList();
        currentGameState.reset();
        updateCurrentGameStateLiveData();
    }
}
