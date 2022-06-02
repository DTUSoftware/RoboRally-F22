package dk.dtu.compute.se.pisd.roborally_server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService {
    HashMap<Integer, Game> games = new HashMap<>();
    HashMap<Integer, GameState> gamestates = new HashMap<>();
    HashMap<Integer, HashMap<Integer, Player>> players = new HashMap<>();

    public GameService() {
        games.put(10, new Game(10));
        games.put(11, new Game(11));
        games.put(12, new Game(12));
        games.put(13, new Game(13));

        players.put(10, new HashMap<>());
        players.get(10).put(1, new Player(1));

        gamestates.put(10, new GameState());
    }

    @Override
    public List<Game> findAll() {
        return games.values().stream().toList();
    }

    @Override
    public Game getGameByID(int id) {
        return games.get(id);
    }

    @Override
    public boolean addGame(Game game) {
        return false;
    }

    @Override
    public boolean updateGame(int id, Game game) {
        Game gameFound = getGameByID(id);
        if (gameFound != null) {
            gameFound.setID(game.getID());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteGameByID(int id) {
        return false;
    }

    @Override
    public GameState getGameStateByID(int id) {
        return gamestates.get(id);
    }

    @Override
    public String updatePlayerState(int id, int playerID) {
        return null;
    }

    @Override
    public boolean updatePlayerDeck(int id, int playerID, Player player) {
        return false;
    }

    @Override
    public Player getPlayerDeck(int id, int playerID) {
        return players.get(id).get(playerID);
    }
}
