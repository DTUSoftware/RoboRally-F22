package dk.dtu.compute.se.pisd.roborally_server.model;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.Heading;

public class Player {
    int id;
    String name;
    String color;
    int power;
    int energy;
    int currentCheckpoint;
    Position position;

    class Position {
        int x;
        int y;
        Heading heading;

        Position(int x, int y, Heading heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }
    }

    public Player(int id) {
        this.id = id;
        this.name = "bob";
    }
}
