package dk.dtu.compute.se.pisd.roborally_server.service;

import java.util.ArrayList;
import java.util.List;

import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService {
    ArrayList<Game> games = new ArrayList<>();

    public GameService() {
        games.add(new Game(10));
        games.add(new Game(11));
        games.add(new Game(12));
        games.add(new Game(13));
    }

    @Override
    public List<Game> findAll() {
        return games;
    }

    @Override
    public Game getGameByID(int id) {
        for(Game game : games) {
            if(game.getID() == id) {
                return game;
            }
        }
        return null;
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
        }
        return false;
    }

    @Override
    public boolean deleteGameByID(int id) {
        return false;
    }

    @Override
    public GameState getGameStateByID(int id) {
        return null;
    }

    @Override
    public String updatePlayerState(int id, int playerID) {
        return null;
    }
}
