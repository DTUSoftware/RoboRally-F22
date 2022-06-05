package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.SpawnGear;

import java.util.*;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadBoard.loadBoard;

public class Game {
    private UUID id;
    private Date created;
    private Date lastPlayed;

    private String mapID;
    private int playerCount;

    private GameState gameState;
    private HashMap<UUID, Player> players;

    private Board board;
    private GameLogicController gameLogicController;

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

        this.gameLogicController = new GameLogicController(this);
        loadBoard(this);

        for (int i = 0; i < playerCount; i++) {
            UUID uuid = UUID.randomUUID();
            addPlayer(uuid, "Player " + Integer.toString(i+1), PLAYER_COLORS.get(i));

            SpawnGear[] spawnGears = board.getSpawnGears();
            if (spawnGears.length >= playerCount) {
                SpawnGear spawnGear = board.getSpawnGears()[i];
                Player player = getPlayer(uuid);
                player.setSpace(spawnGear.getSpace());
                player.setHeading(spawnGear.getDirection());
                player.setStartGear(spawnGear);
            }
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

    public void addPlayer(UUID uuid, String name, String color) {
        Player player = new Player(uuid, id);
        player.setColor(color);
        player.setName(name);
        players.put(uuid, player);
        gameState.addPlayer(player);
    }

    @JsonIgnore
    public Player getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    @JsonIgnore
    public boolean hasPlayer(Player player) {
        return players.containsValue(player);
    }

    @JsonIgnore
    public GameLogicController getGameLogicController() {
        return gameLogicController;
    }

    @JsonIgnore
    public Board getBoard() {
        return board;
    }


    /**
     * Set the board for the gamecontroller to control.
     * Used for initialization that needs the gamecontroller.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @param board the board.
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}
