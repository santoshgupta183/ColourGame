package com.santosh.colourmemory.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.santosh.colourmemory.model.GameScore;

import java.util.List;

@Dao
public interface GameScoreDao {

    @Insert
    void insertScore(GameScore gameScore);

    @Query("select * from gameScoreTable order by score desc")
    LiveData<List<GameScore>> getScores();

}
