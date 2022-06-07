package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * The gamestate (returned with API).
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class GameState {
    private Phase phase;
    private int step;
    private int currentPlayer;

    private List<Player> players;

    private boolean stepMode;

    /**
     * Creates a new gamestate. Starts in waiting phase.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public GameState() {
        this.phase = Phase.WAITING;
        this.step = 0;
        this.currentPlayer = 0;
        this.players = new ArrayList<>();

        this.stepMode = true;
    }

    /**
     * Gets current phase.
     *
     * @return current phase.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Sets current phase.
     *
     * @param phase current phase
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * Gets current step.
     *
     * @return current step
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets current step
     *
     * @param step current step to set to
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Is it stepmode?
     *
     * @return true if stepmode, else false
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * Set stepmode.
     *
     * @param stepMode stepmode to set to
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setStepMode(boolean stepMode) {
        this.stepMode = stepMode;
    }

    /**
     * Gets the current player, as player number, not ID.
     *
     * @return the player number of current player (position in array).
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the number of the current player.
     *
     * @param currentPlayer the number of player to set
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets all players as list, ordered by player numbers.
     *
     * @return the list of players
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players.
     * Be careful, since changing this can cause UUID mismatch on clients!
     *
     * @param players the new list of players
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Clears the players.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void clearPlayers() {
        players.clear();
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes a player from the game.
     *
     * @param player the player to remove
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Gets the number of ready players.
     *
     * @return number of ready players.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getReadyPlayers() {
        int readyPlayers = 0;
        for (Player player : players) {
            if (player.getReady()) {
                readyPlayers++;
            }
        }
        return readyPlayers;
    }

    /**
     * Gets the player object of current player.
     *
     * @return the current player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Player getPlayerCurrent() {
        return getPlayer(currentPlayer);
    }

    /**
     * Gets the player using player number
     *
     * @param i player number
     * @return the player, if found, else null
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Player getPlayer(int i) {
        if (players.size() < i) {
            return null;
        }
        return players.get(i);
    }

    /**
     * Get number of player
     *
     * @param player the player to find number of
     * @return number of player if found, else throws error
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public int getPlayerNumber(Player player) {
        return players.indexOf(player);
    }

    /**
     * Ends the current player's turn.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void endCurrentPlayerTurn() {
        setCurrentPlayer((currentPlayer + 1) % players.size());
    }
}
