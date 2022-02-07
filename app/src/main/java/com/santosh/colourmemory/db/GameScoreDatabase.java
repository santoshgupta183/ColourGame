package com.santosh.colourmemory.db;

import static com.santosh.colourmemory.utils.Constants.DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.santosh.colourmemory.model.GameScore;

@Database(
        entities = {GameScore.class},
        version = 1
)
public abstract class GameScoreDatabase extends RoomDatabase {
    public abstract GameScoreDao getGameScoreDao();

    private static GameScoreDatabase instance =null;

    public static GameScoreDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,
                    GameScoreDatabase.class, DATABASE_NAME)
                    .build();
        }
        return instance;
    }
}
