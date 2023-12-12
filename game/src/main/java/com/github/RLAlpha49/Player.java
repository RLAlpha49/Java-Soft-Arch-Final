package com.github.RLAlpha49;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Room currentRoom;
    private int score;
    private List<Item> inventory;

    public Player(Room currentRoom) {
        this.currentRoom = currentRoom;
        this.inventory = new ArrayList<>();
        this.score = 0;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean hasItem(Item item) {
        return inventory.contains(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }
}