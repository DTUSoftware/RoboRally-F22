package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game {
    private int id;
    private Date created;
    private Date lastPlayed;

    private String mapID;
    private int playerCount;

    private GameState gameState;
    private HashMap<UUID, Player> players;

    public Game() {}

    public Game(int id, String mapID, int playerCount) {
        super();
        this.id = id;
        this.mapID = mapID;
        this.playerCount = playerCount;

        this.created = new Date();
        this.lastPlayed = new Date();

        this.players = new HashMap<>();
        this.gameState = new GameState();

        for (int i = 0; i < playerCount; i++) {
            UUID uuid = UUID.randomUUID();
            Player player = new Player(uuid);
            players.put(uuid, player);
            gameState.addPlayer(player);
        }
    }

    public Game(int id) {
        this(id, "defaultboard", 2);
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public String getMapID() {
        return mapID;
    }

    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @JsonIgnore
    public GameState getGameState() {
        return gameState;
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return gameState.getPlayers();
    }

    @JsonIgnore
    public Player getPlayer(UUID uuid) {
        return players.get(uuid);
    }
}
