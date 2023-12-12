package com.github.RLAlpha49;

public class TrapRoom implements RoomBehavior {
    private int level;
    private int score;

    public TrapRoom(int level) {
        this.level = level;
        switch (level) {
            case 1:
                this.score = -10;  // small penalty
                break;
            case 2:
                this.score = -20;  // medium penalty
                break;
            case 3:
                this.score = -30;  // large penalty
                break;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    public class GameOverException extends RuntimeException {
        public GameOverException(String message) {
            super(message);
        }
    }

    @Override
    public void onEnter(Player player) {
        System.out.println("You've entered a level " + level + " trap room. Watch out!");
        player.increaseScore(score);
        if (level == 4) {
            //System.out.println("You've been killed by the trap!");
            throw new GameOverException("You've been killed by the trap!");
        }
    }

    @Override
    public int getScore() {
        return score;
    }
}