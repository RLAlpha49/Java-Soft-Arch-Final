package com.github.RLAlpha49;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private RoomBehavior behavior;
    private String name;
    private String description;
    private Item item;
    private int x, y;
    private boolean visited = false;
    Map<String, Room> exits = new HashMap<>();

    public Room(int x, int y, String name, String description, Item item, RoomBehavior behavior) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.description = description;
        this.item = item;
        this.behavior = behavior;
    }

    public List<String> getAvailableDirections() {
        List<String> directions = new ArrayList<>();
        for (Map.Entry<String, Room> entry : exits.entrySet()) {
            if (entry.getValue() != null) {
                directions.add(entry.getKey());
            }
        }
        return directions;
    }

    public void enter(Player player) {
        if (!visited) {
            behavior.onEnter(player);
            visited = true;
        } else {
            System.out.println("You've already visited this room.");
        }
    }

    public String getDescription() {
        return "Room at (" + x + ", " + y + "): " + description;
    }

    public Item getItem() {
        return item;
    }

    public void removeItem() {
        this.item = null;
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public void setExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public String getCellTop() {
        return exits.containsKey("north") ? "+  +" : "++++";
    }

    public String getCellMiddle() {
        return exits.containsKey("west") ? "   " : "|  ";
    }

    public String getCellBottom() {
        return exits.containsKey("south") ? "+  +" : "++++";
    }

    public Map<String, Room> getExits() {
        return exits;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Room at (" + x + ", " + y + ")";
    }
}