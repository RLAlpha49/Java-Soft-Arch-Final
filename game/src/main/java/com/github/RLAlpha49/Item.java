package com.github.RLAlpha49;

public class Item {
    private String name;
    private int score;

    public Item(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}