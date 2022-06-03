package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.Heading;

import java.util.UUID;

public class Player {
    private UUID id;
    private String name;
    private String color;
    private int currentCheckpoint;
    private Position position;
    private PlayerDeck deck;

    class Position {
        private int x;
        private int y;
        private Heading heading;

        Position(int x, int y, Heading heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }

        Position() {
            this(0, 0, Heading.NORTH);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Heading getHeading() {
            return heading;
        }

        public void setHeading(Heading heading) {
            this.heading = heading;
        }
    }

    public Player() {
        this(UUID.randomUUID());
    }

    public Player(UUID id) {
        this.id = id;
        this.position = new Position();
        this.deck = new PlayerDeck();
    }

    public Player(String name) {
        this();
        this.name = name;
    }

    public Player(UUID id, String name) {
        this(id);
        this.name = name;
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y, Heading heading) {
        if (this.position != null) {
            this.position.setX(x);
            this.position.setY(y);
            this.position.setHeading(heading);
        }
        else {
            this.position = new Position(x, y, heading);
        }
    }

    @JsonIgnore
    public PlayerDeck getDeck() {
        return this.deck;
    }

    public void setDeck(PlayerDeck deck) {
        this.deck = deck;
    }
}
