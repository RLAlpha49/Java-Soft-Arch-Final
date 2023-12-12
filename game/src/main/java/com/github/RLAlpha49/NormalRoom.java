package com.github.RLAlpha49;

public class NormalRoom implements RoomBehavior {
    private int score;

    public NormalRoom(int score) {
        this.score = score;
    }

    @Override
    public void onEnter(Player player) {
        System.out.println("You've entered a normal room. Nothing special here.");
        player.increaseScore(score);
    }

    @Override
    public int getScore() {
        return score;
    }
}