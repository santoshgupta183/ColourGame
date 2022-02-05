package com.santosh.colourmemory.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.santosh.colourmemory.model.GameScore;

@Database(
        entities = {GameScore.class},
        version = 2
)
public abstract class GameScoreDatabase extends RoomDatabase {
    public abstract GameScoreDao getGameScoreDao();

    private static GameScoreDatabase instance =null;

    public static GameScoreDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,
                    GameScoreDatabase.class, "gameScoreDb2.db")
                    .build();
        }
        return instance;
    }
}
