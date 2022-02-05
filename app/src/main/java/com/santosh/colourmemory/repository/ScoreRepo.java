package com.santosh.colourmemory.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.santosh.colourmemory.db.GameScoreDao;
import com.santosh.colourmemory.db.GameScoreDatabase;
import com.santosh.colourmemory.model.GameScore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScoreRepo {

    private static ScoreRepo instance;
    private GameScoreDao dao;
    private ExecutorService executors = Executors.newSingleThreadExecutor();

    private ScoreRepo(Context context){
        dao = GameScoreDatabase.getInstance(context).getGameScoreDao();
    }

    public static ScoreRepo getInstance(Context context){
        if (instance == null){
            instance = new ScoreRepo(context);
        }
        return instance;
    }

    public void insertScore(GameScore score){
        executors.execute(() -> dao.insertScore(score));
    }

    public LiveData<List<GameScore>> getAllScores(){
        return dao.getScores();
    }
}
