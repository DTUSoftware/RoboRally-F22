package dk.dtu.compute.se.pisd.roborally_server.server.service;

import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;

import java.util.List;
import java.util.UUID;

public interface IGameService {
    List<Game> findAll();
    public Game getGameByID(UUID id);
    public Game newGame(String mapID, int playerCount);
    boolean addGame(Game game);
    public boolean updateGame(UUID id, Game game);
    public boolean deleteGameByID(UUID id);

    GameState getGameStateByID(UUID id);
    String updatePlayerState(UUID id, UUID playerID);

    boolean updatePlayerDeck(UUID id, UUID playerID, PlayerDeck playerDeck);

    Player getPlayer(UUID id, UUID playerID);
    PlayerDeck getPlayerDeck(UUID id, UUID playerID);

    boolean updatePlayerReady(UUID id, UUID playerID);
}
