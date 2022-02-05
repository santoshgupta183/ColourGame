package com.santosh.colourmemory.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(
        tableName = "gameScoreTable"
)
public class GameScore {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int score;

    public GameScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameScore gameScore = (GameScore) o;
        return score == gameScore.score && name.equals(gameScore.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }
}
