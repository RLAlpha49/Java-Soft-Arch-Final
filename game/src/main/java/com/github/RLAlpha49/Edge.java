package com.github.RLAlpha49;

public class Edge {
    Room room1;
    Room room;
    String direction;

    public Edge(Room room1, Room room, String direction) {
        this.room1 = room1;
        this.room = room;
        this.direction = direction;
    }
}