package dk.dtu.compute.se.pisd.roborally_server.model;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.Phase;

import java.util.List;

public class GameState {
    private Phase phase;
    private int step;
    private int currentPlayer;

    private List<Player> players;
}
