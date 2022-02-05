package com.santosh.colourmemory.model;

public class CurrentGameState {
    int score;
    int rank;
    boolean isGameOver;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public void reset() {
        rank = 0;
        score = 0;
        isGameOver = false;
    }
}
