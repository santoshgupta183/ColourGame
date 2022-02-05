package com.santosh.colourmemory.model;

public class CardData {
    private int index;
    private int cardValue;

    public CardData(int index, int cardValue) {
        this.index = index;
        this.cardValue = cardValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}
