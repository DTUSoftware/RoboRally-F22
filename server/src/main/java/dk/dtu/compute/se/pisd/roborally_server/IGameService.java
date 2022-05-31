package dk.dtu.compute.se.pisd.roborally_server;

import java.util.List;

public interface IGameService {
    List<Game> findAll();
    public Game getGameByID(int id);
    boolean addGame(Game game);
    public boolean updateGame(int id, Game game);
    public boolean deleteGameByID(int id);
}
