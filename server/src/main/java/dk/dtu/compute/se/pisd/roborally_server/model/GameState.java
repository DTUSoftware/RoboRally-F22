package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Phase phase;
    private int step;
    private int currentPlayer;

    private List<Player> players;

    private boolean stepMode;

    public GameState() {
        this.phase = Phase.WAITING;
        this.step = 0;
        this.currentPlayer = 0;
        this.players = new ArrayList<>();

        this.stepMode = true;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @JsonIgnore
    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        this.stepMode = stepMode;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public int getReadyPlayers() {
        int readyPlayers = 0;
        for (Player player : players) {
            if (player.getReady()) {
                readyPlayers++;
            }
        }
        return readyPlayers;
    }

    @JsonIgnore
    public Player getPlayerCurrent() {
        return getPlayer(currentPlayer);
    }

    @JsonIgnore
    public Player getPlayer(int i) {
        return players.get(i);
    }

    @JsonIgnore
    public int getPlayerNumber(Player player) {
        return players.indexOf(player);
    }

    public void endCurrentPlayerTurn() {
        setCurrentPlayer((currentPlayer + 1) % players.size());
    }
}
