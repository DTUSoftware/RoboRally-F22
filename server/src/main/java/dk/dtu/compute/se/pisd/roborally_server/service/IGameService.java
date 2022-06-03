package dk.dtu.compute.se.pisd.roborally_server.service;

import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    List<Game> findAll();
    public Game getGameByID(int id);
    boolean addGame(Game game);
    public boolean updateGame(int id, Game game);
    public boolean deleteGameByID(int id);

    GameState getGameStateByID(int id);
    String updatePlayerState(int id, UUID playerID);

    boolean updatePlayerDeck(int id, UUID playerID, PlayerDeck playerDeck);

    Player getPlayer(int id, UUID playerID);
    PlayerDeck getPlayerDeck(int id, UUID playerID);
}
