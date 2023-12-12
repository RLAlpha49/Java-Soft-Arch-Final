package com.github.RLAlpha49;

public class RoomFactory {
    public Room createRoom(int x, int y, String name, String description, Item item, RoomBehavior behavior) {
        if (behavior instanceof TreasureRoom) {
            return new Room(x, y, name, description, item, behavior);
        } else {
            return new Room(x, y, name, description, null, behavior);
        }
    }
}