package dk.dtu.compute.se.pisd.roborally_server.model;

import java.util.Date;

public class Game {
    private int id;
    private Date created;
    private Date lastPlayed;

    private String mapID;

    public Game() {}

    public Game(int id) {
        super();
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
}
