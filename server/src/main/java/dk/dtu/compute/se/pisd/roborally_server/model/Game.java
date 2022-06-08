package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.SpawnGear;

import java.util.*;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadBoard.loadBoard;

/**
 * A game (returned with API).
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
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

    /**
     * Creates a new game.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Game() {
        this.created = new Date();
        this.lastPlayed = new Date();

        this.players = new HashMap<>();
        this.gameState = new GameState();

        this.gameLogicController = new GameLogicController(this);
    }

    /**
     * Creates a new game with specified UUID, map and players.
     *
     * @param id          game ID
     * @param mapID       map ID
     * @param playerCount player count
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Game(UUID id, String mapID, int playerCount) {
        this();
        this.id = id;
        this.mapID = mapID;
        this.playerCount = playerCount;

        loadBoard(this);

        initializePlayers();
    }

    /**
     * Creates a new game with mapID and players
     *
     * @param mapID       map ID
     * @param playerCount player count
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Game(String mapID, int playerCount) {
        this(UUID.randomUUID(), mapID, playerCount);
    }

    /**
     * Creates a new game with UUID. Uses defaultboard and 2 players.
     *
     * @param id game ID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Game(UUID id) {
        this(id, "defaultboard", 2);
    }

    /**
     * Gets the game ID
     *
     * @return game ID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public UUID getID() {
        return id;
    }

    /**
     * Sets the game ID
     *
     * @param id game ID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setID(UUID id) {
        this.id = id;
    }

    /**
     * Clears the players from gamestate and game, and recreates them with playerCount and random UUID's.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void initializePlayers() {
        players.clear();
        gameState.clearPlayers();
        for (int i = 0; i < playerCount; i++) {
            UUID uuid = UUID.randomUUID();
            addPlayer(uuid, "Player " + Integer.toString(i + 1), PLAYER_COLORS.get(i));

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

    /**
     * Get creation date.
     *
     * @return creation date.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Set creation date
     *
     * @param created creation date
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Get last played.
     *
     * @return last played
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Date getLastPlayed() {
        return lastPlayed;
    }

    /**
     * Sets last played.
     *
     * @param lastPlayed last played
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    /**
     * Get MAPID.
     *
     * @return mapID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public String getMapID() {
        return mapID;
    }

    /**
     * Changes the map.
     * You have to reload the board afterwards!
     *
     * @param mapID mapID of new map
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    /**
     * Get playercount.
     *
     * @return playercount
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * Changes playercount.
     * Have to use initializePlayers afterwards.
     *
     * @param playerCount playercount
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * Get gamestate.
     *
     * @return gamestate
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Get players.
     *
     * @return players
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public List<Player> getPlayers() {
        return gameState.getPlayers();
    }

    /**
     * Adds a player to the game.
     * (DOESNT'T CHANGE PLAYERCOUNT).
     *
     * @param uuid  UUID of player
     * @param name  name of player
     * @param color color of player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void addPlayer(UUID uuid, String name, String color) {
        Player player = new Player(uuid, id);
        player.setColor(color);
        player.setName(name);
        players.put(uuid, player);
        gameState.addPlayer(player);
    }

    /**
     * Gets a player with UUID
     *
     * @param playerID player ID
     * @return the player, if found, else null
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Player getPlayer(UUID playerID) {
        if (players.containsKey(playerID)) {
            return players.get(playerID);
        }
        return null;
    }

    /**
     * Changes the UUID of a player
     *
     * @param playerID the old player ID
     * @param newID    the new player ID
     * @return whether it changed or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean changePlayerID(UUID playerID, UUID newID) {
        Player player = getPlayer(playerID);
        if (player != null) {
            players.remove(playerID);
            player.setID(newID);
            players.put(player.getID(), player);
        }
        return false;
    }

    /**
     * Returns whether the game has the player.
     *
     * @param player the player to check
     * @return true if in game, else false
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public boolean hasPlayer(Player player) {
        return players.containsValue(player);
    }

    /**
     * Gets the gamelogiccontroller.
     *
     * @return controller
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public GameLogicController getGameLogicController() {
        return gameLogicController;
    }

    /**
     * Gets the board.
     *
     * @return the board
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Board getBoard() {
        return board;
    }


    /**
     * Set the board for the gamecontroller to control.
     * Used for initialization that needs the gamecontroller.
     *
     * @param board the board.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setBoard(Board board) {
        this.board = board;
        this.board.setGame(this);
    }
}
