package dk.dtu.compute.se.pisd.roborally_server.model;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.Phase;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Phase phase;
    private int step;
    private int currentPlayer;

    private List<Player> players;

    public GameState() {
        this.phase = Phase.INITIALISATION;
        this.step = 0;
        this.currentPlayer = 0;
        this.players = new ArrayList<>();
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
}
