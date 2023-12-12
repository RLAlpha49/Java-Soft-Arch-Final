package com.github.RLAlpha49;

import java.util.Random;

public class TreasureRoom implements RoomBehavior {
    private static final Item[] ITEMS = {
        new Item("Gold", 10),
        new Item("Diamond", 20),
        new Item("Emerald", 30),
        new Item("Ruby", 40),
        new Item("Sapphire", 50),
        new Item("Amethyst", 60),
        new Item("Pearl", 70),
        new Item("Topaz", 80),
        new Item("Opal", 90),
        new Item("Jade", 100)
    };
    private static CumulativeItem[] cumulativeItems;
    private Item treasure;
    private int score;

    static {
        double totalInverseScore = 0;
        cumulativeItems = new CumulativeItem[ITEMS.length];
        for (int i = 0; i < ITEMS.length; i++) {
            double inverseScore = 1.0 / ITEMS[i].getScore();
            totalInverseScore += inverseScore;
            cumulativeItems[i] = new CumulativeItem(ITEMS[i].getName(), ITEMS[i].getScore(), totalInverseScore);
        }
        // for (int i = 0; i < cumulativeItems.length; i++) {
        //     double scoreDifference = i == 0 ? cumulativeItems[i].getCumulativeScore() : cumulativeItems[i].getCumulativeScore() - cumulativeItems[i - 1].getCumulativeScore();
        //     double percentage = scoreDifference / totalInverseScore * 100;
        //     System.out.println("The probability of getting " + cumulativeItems[i].getName() + " is " + percentage + "%");
        // }
    }

    public TreasureRoom(int score) {
        Random random = new Random();
        int index = random.nextInt(cumulativeItems.length);
        this.treasure = new Item(cumulativeItems[index].getName(), (int) cumulativeItems[index].getScore());
        this.score = score;  // base score for entering a treasure room
    }

    @Override
    public void onEnter(Player player) {
        System.out.println("You've entered a treasure room. You found a " + treasure.getName() + "!");
        player.increaseScore(score + treasure.getScore());
    }

    @Override
    public int getScore() {
        return score;
    }

    public Item getTreasure() {
        return treasure;
    }
}