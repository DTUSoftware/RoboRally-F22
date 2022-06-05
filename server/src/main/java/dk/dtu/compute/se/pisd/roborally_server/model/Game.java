package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Game {
    private UUID id;
    private Date created;
    private Date lastPlayed;

    private String mapID;
    private int playerCount;

    private GameState gameState;
    private HashMap<UUID, Player> players;

    private static final List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    public Game() {}

    public Game(UUID id, String mapID, int playerCount) {
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
            player.setColor(PLAYER_COLORS.get(i));
            player.setName("Player " + Integer.toString(i+1));
            players.put(uuid, player);
            gameState.addPlayer(player);
        }
    }

    public Game(String mapID, int playerCount) {
        this(UUID.randomUUID(), mapID, playerCount);
    }

    public Game(UUID id) {
        this(id, "defaultboard", 2);
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
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

    @JsonIgnore
    public boolean hasPlayer(Player player) {
        return players.containsValue(player);
    }
}
