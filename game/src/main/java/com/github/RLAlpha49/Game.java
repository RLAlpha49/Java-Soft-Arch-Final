package com.github.RLAlpha49;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Collections;

public class Game {
    private int size;
    private Room[][] rooms;
    private Room currentRoom;
    private Player player;
    private Random random = new Random();

    // Singleton pattern
    private static Game instance;

    private Game() {
        setupGame();
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private void setupGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the size of the maze:");
        size = scanner.nextInt();
        rooms = new Room[size][size];

        // Create rooms and initialize visited set
        Set<Room> visited = new HashSet<>();
        Random random = new Random();
        RoomFactory roomFactory = new RoomFactory();
        List<Integer> trapLevels = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            trapLevels.add(1);
        }
        for (int i = 0; i < 20; i++) {
            trapLevels.add(2);
        }
        for (int i = 0; i < 15; i++) {
            trapLevels.add(3);
        }
        for (int i = 0; i < 5; i++) {
            trapLevels.add(4);
        }
        Collections.shuffle(trapLevels);

        List<String> roomTypes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            roomTypes.add("trap");
        }
        for (int i = 0; i < 20; i++) {
            roomTypes.add("treasure");
        }
        for (int i = 0; i < 60; i++) {
            roomTypes.add("normal");
        }
        Collections.shuffle(roomTypes);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                RoomBehavior behavior;
                String roomType = roomTypes.remove(roomTypes.size() - 1);
                if (roomType.equals("trap")) {
                    int level = trapLevels.remove(trapLevels.size() - 1);
                    behavior = new TrapRoom(level);
                } else if (roomType.equals("treasure")) {
                    behavior = new TreasureRoom(20);  // Treasure rooms increase the score by 20
                } else {
                    behavior = new NormalRoom(1);  // Normal rooms increase the score by 1
                }
                // Create room
                rooms[j][i] = roomFactory.createRoom(j, i, "Room " + (i * size + j + 1), "You are in room " + (i * size + j + 1), behavior instanceof TreasureRoom ? ((TreasureRoom) behavior).getTreasure() : null, behavior);
                visited.add(rooms[j][i]);
            }
        }

        // Start from a random room
        Room currentRoom = rooms[random.nextInt(size)][random.nextInt(size)];
        visited.add(currentRoom);

        // Stack for backtracking
        Stack<Room> stack = new Stack<>();
        stack.push(currentRoom);

        // While there are still rooms that haven't been visited
        while (visited.size() < size * size) {
            // Get the current room's neighbors that haven't been visited yet
            List<Room> unvisitedNeighbors = getUnvisitedNeighbors(currentRoom, visited);

            if (!unvisitedNeighbors.isEmpty()) {
                // If there are unvisited neighbors, choose one randomly
                Room chosenRoom = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));

                // Connect the chosen room with the current room
                connectRooms(currentRoom, chosenRoom);

                // Continue the process from the chosen room
                currentRoom = chosenRoom;
                visited.add(currentRoom);
                stack.push(currentRoom);
            } else {

                // If there are no unvisited neighbors, backtrack to the previous room
                stack.pop();
                currentRoom = stack.peek();
            }
        }

        // After the maze is generated, go through all the rooms and for each room that has less than two exits, add a random exit
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Room room = rooms[i][j];
                List<Edge> possibleEdges = getEdges(i, j);
                List<Edge> edgesToRemove = new ArrayList<>();
                for (Edge edge : possibleEdges) {
                    if (room.getExits().containsKey(edge.direction)) {
                        edgesToRemove.add(edge);
                    }
                }
                possibleEdges.removeAll(edgesToRemove);
                if (!possibleEdges.isEmpty()) {
                    Edge edge = possibleEdges.get(random.nextInt(possibleEdges.size()));
                    room.setExit(edge.direction, edge.room);
                    edge.room.setExit(opposite(edge.direction), room);
                }
            }
        }

        // Create player
        player = new Player(rooms[0][0]);

        // Set the current room to a random room
        this.currentRoom = rooms[0][0];

        // THIS IS VERY INCONSISTENT
        // Print the maze layout
        //printMaze(currentRoom);
        //System.out.println("You start in the top-left room (marked with 'S').");
    }

    public String opposite(String direction) {
        switch (direction) {
            case "north": return "south";
            case "south": return "north";
            case "east": return "west";
            case "west": return "east";
            default: throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    public List<Edge> getEdges(int x, int y) {
        List<Edge> edges = new ArrayList<>();

        if (x > 0) edges.add(new Edge(rooms[x][y], rooms[x - 1][y], "west"));
        if (x < size - 1) edges.add(new Edge(rooms[x][y], rooms[x + 1][y], "east"));
        if (y > 0) edges.add(new Edge(rooms[x][y], rooms[x][y - 1], "south"));
        if (y < size - 1) edges.add(new Edge(rooms[x][y], rooms[x][y + 1], "north"));

        return edges;
    }

    public void printMaze(Room currentRoom) {
        // Print debug information
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Debug information
                //System.out.println("Room at (" + i + ", " + j + "): " + rooms[i][j]);
                System.out.println("Exits: " + rooms[i][j].exits);
            }
        }

        System.out.println("Legend:");
        System.out.println("X - Room");
        System.out.println("- - Exit to the east or west");
        System.out.println("| - Exit to the north or south");
        System.out.println("P - Player's current position");

        for (int i = size - 1; i >= 0; i--) {
            // Print rooms and east exits
            for (int j = 0; j < size; j++) {
                System.out.print((rooms[j][i] == currentRoom) ? "P" : "X");
                System.out.print((j < size - 1 && rooms[j][i].exits.containsKey("east")) ? "-" : " ");
            }
            System.out.println();
    
            // Print north exits
            if (i > 0) {
                for (int j = 0; j < size; j++) {
                    if (rooms[j][i - 1].exits.containsKey("south")) {
                        System.out.print("|");
                    }
                    if (j < size - 1) {
                        if (i > 1) {
                            System.out.print(" ");
                        }
                    }
                }
                if (i > 1) {
                    System.out.println();
                }
            }

            // Print south exits for y value 1
            if (i == 1) {
                for (int j = 0; j < size; j++) {
                    if (rooms[j][i].exits.containsKey("south")) {
                        System.out.print("|");
                    }
                    if (j < size - 1 && rooms[j + 1][i].exits.containsKey("south")) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }
    
    private List<Room> getUnvisitedNeighbors(Room room, Set<Room> visited) {
        List<Room> unvisitedNeighbors = new ArrayList<>();
        int x = room.getX();
        int y = room.getY();

        if (x > 0 && !visited.contains(rooms[x - 1][y])) {
            unvisitedNeighbors.add(rooms[x - 1][y]);
        }
        if (x < size - 1 && !visited.contains(rooms[x + 1][y])) {
            unvisitedNeighbors.add(rooms[x + 1][y]);
        }
        if (y > 0 && !visited.contains(rooms[x][y - 1])) {
            unvisitedNeighbors.add(rooms[x][y - 1]);
        }
        if (y < size - 1 && !visited.contains(rooms[x][y + 1])) {
            unvisitedNeighbors.add(rooms[x][y + 1]);
        }

        return unvisitedNeighbors;
    }

    private void connectRooms(Room room1, Room room2) {
        int x1 = room1.getX();
        int y1 = room1.getY();
        int x2 = room2.getX();
        int y2 = room2.getY();

        if (x1 == x2) {
            if (y1 < y2) {
                room1.setExit("north", room2);
                room2.setExit("south", room1);
            } else {
                room1.setExit("south", room2);
                room2.setExit("north", room1);
            }
        } else {
            if (x1 < x2) {
                room1.setExit("east", room2);
                room2.setExit("west", room1);
            } else {
                room1.setExit("west", room2);
                room2.setExit("east", room1);
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        // Print game instructions
        System.out.println("Welcome to the game!");
        System.out.println("You can use the following commands:");
        System.out.println("\"go [direction]\": Move to the next room in the specified direction (north, south, east, west).");
        System.out.println("\"quit\": Quit the game.");

        while (true) {
            System.out.println(currentRoom.getDescription());
            System.out.println("You can go: " + currentRoom.getAvailableDirections());
            String[] command = scanner.nextLine().split(" ");

            if (command[0].equals("go")) {
                if (command.length < 2) {
                    System.out.println("You need to specify a direction to go (north, south, east, west).");
                } else {
                    Room nextRoom = currentRoom.getExit(command[1]);
                    if (nextRoom == null) {
                        System.out.println("There's no exit in that direction.");
                    } else {
                        currentRoom = nextRoom;
                        try {
                            nextRoom.enter(player);
                            System.out.println("Your current score is: " + player.getScore());
                        } catch (TrapRoom.GameOverException e) {
                            System.out.println(e.getMessage());
                            System.out.println("Your final score is: " + player.getScore());
                            break;
                        }                        
                        //printMaze(currentRoom);  // Print the maze after moving
                    }
                }
            } else if (command[0].equals("quit")) {
                break;
            } else {
                System.out.println("I don't understand.");
            }
        }

        scanner.close();
    }
}
