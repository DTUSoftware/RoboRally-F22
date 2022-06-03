package dk.dtu.compute.se.pisd.roborally_server.service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService {
    HashMap<UUID, Game> games = new HashMap<>();

    public GameService() {
//        games.put(10, new Game(10));
//        games.put(11, new Game(11));
//        games.put(12, new Game(12));
//        games.put(13, new Game(13));
    }

    @Override
    public List<Game> findAll() {
        return games.values().stream().toList();
    }

    @Override
    public Game getGameByID(UUID id) {
        return games.get(id);
    }

    @Override
    public Game newGame(String mapID, int playerCount) {
        Game game = new Game(mapID, playerCount);
        games.put(game.getID(), game);
        return game;
    }

    @Override
    public boolean addGame(Game game) {
        return false;
    }

    @Override
    public boolean updateGame(UUID id, Game game) {
        Game gameFound = getGameByID(id);
        if (gameFound != null) {
            gameFound.setID(game.getID());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteGameByID(UUID id) {
        return false;
    }

    @Override
    public GameState getGameStateByID(UUID id) {
        return games.get(id).getGameState();
    }

    @Override
    public String updatePlayerState(UUID id, UUID playerID) {
        return null;
    }

    @Override
    public boolean updatePlayerDeck(UUID id, UUID playerID, PlayerDeck playerDeck) {
        return false;
    }

    @Override
    public Player getPlayer(UUID id, UUID playerID) {
        return games.get(id).getPlayer(playerID);
    }

    @Override
    public PlayerDeck getPlayerDeck(UUID id, UUID playerID) {
        return games.get(id).getPlayer(playerID).getDeck();
    }
}
