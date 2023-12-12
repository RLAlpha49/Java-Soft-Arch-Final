package com.github.RLAlpha49;

public class CumulativeItem {
    private String name;
    private double score;
    private double cumulativeScore;

    public CumulativeItem(String name, double score, double cumulativeScore) {
        this.name = name;
        this.score = score;
        this.cumulativeScore = cumulativeScore;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public double getCumulativeScore() {
        return cumulativeScore;
    }
}