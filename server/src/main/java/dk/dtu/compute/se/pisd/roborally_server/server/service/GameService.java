package dk.dtu.compute.se.pisd.roborally_server.server.service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState.loadGameState;
import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState.saveGameState;

@Service
public class GameService implements IGameService {
    @Autowired
    private IJSONService jsonService;

    private static HashMap<UUID, Game> games = new HashMap<>();

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
        if (!games.containsKey(id)) {
            return null;
        }
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
        games.put(game.getID(), game);
        return true;
    }

    @Override
    public Game loadSavedGame(UUID id) {
        List<String> gameStateNames = jsonService.getFolderJSON(LoadGameState.GAMESTATEFOLDER);
        ArrayList<String> gameStatesWithID = new ArrayList<>(gameStateNames.size());
        System.out.println(gameStateNames);
        for (String gameStateName : gameStateNames) {
            if (gameStateName.contains(" ")) {
                UUID gameID = null;
                try {
                    gameID = UUID.fromString(gameStateName.split(" ")[0]);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (gameID != null && gameID.equals(id)) {
                    gameStatesWithID.add(gameStateName);
                }
            }
        }
        System.out.println(gameStatesWithID);

        if (gameStatesWithID.isEmpty()) {
            return null;
        }

        // get newest gamestate
        String newestFilename = null;
        Date newestDate = null;
        for (String gameStateFileName : gameStatesWithID) {
            boolean newer = false;
            if (newestFilename == null || newestDate == null) {
                newer = true;
            }
            else {
                Date newDate = null;
                try {
                    newDate = LoadGameState.gameStateTimeFormat.parse(gameStateFileName.split(" ")[1].replace("\\(", "").replace(").json", ""));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (newDate != null && newDate.after(newestDate)) {
                    newer = true;
                }
            }

            if (newer) {
                newestFilename = gameStateFileName;
                try {
                    newestDate = LoadGameState.gameStateTimeFormat.parse(newestFilename.split(" ")[1].replace(".json", ""));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }

        System.out.println(newestFilename);

        Game game;
        if (games.containsKey(id)) {
            game = games.get(id);
        }
        else {
            game = new Game(id);
        }

        loadGameState(game, newestFilename);
        games.put(game.getID(), game);
        return game;
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
    public JSONArray findAllSavedGameStateUnique() {
        JSONArray gameStates = new JSONArray();
        List<String> gameStateNames = jsonService.getFolderJSON(LoadGameState.GAMESTATEFOLDER);
        System.out.println(gameStateNames);
        ArrayList<UUID> gameIDs = new ArrayList<>(gameStateNames.size());
        for (String gameStateName : gameStateNames) {
            if (gameStateName.contains(" ")) {
                UUID gameID = null;
                try {
                    gameID = UUID.fromString(gameStateName.split(" ")[0]);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (gameID != null && !gameIDs.contains(gameID)) {
                    gameIDs.add(gameID);

                    JSONObject gameState = new JSONObject();
                    gameState.put("id", gameStateName.split(" ")[0]);
                    gameStates.put(gameState);
                }
            }
        }
        return gameStates;
    }

    @Override
    public GameState getGameStateByID(UUID id) {
        return games.get(id).getGameState();
    }

    @Override
    public boolean saveGameStateByID(UUID id) {
        Game game = getGameByID(id);
        if (game == null) {
            return false;
        }
        saveGameState(game);
        return true;
    }

    @Override
    public String updatePlayerState(UUID id, UUID playerID) {
        return null;
    }

    @Override
    public boolean updatePlayerDeck(UUID id, UUID playerID, PlayerDeck playerDeck) {
        PlayerDeck currentPlayerDeck = getPlayerDeck(id, playerID);
        if (currentPlayerDeck == null) {
            return false;
        }

        // only check cards
        // TODO: card check to prevent cheating
        currentPlayerDeck.setCards(playerDeck.getCards());
        currentPlayerDeck.setProgram(playerDeck.getProgram());
        currentPlayerDeck.setUpgrades(playerDeck.getUpgrades());

        return true;
    }

    @Override
    public Player getPlayer(UUID id, UUID playerID) {
        Game game = getGameByID(id);
        if (game == null) {
            return null;
        }

        return game.getPlayer(playerID);
    }

    @Override
    public PlayerDeck getPlayerDeck(UUID id, UUID playerID) {
        Player player = getPlayer(id, playerID);
        if (player == null) {
            return null;
        }
        return player.getDeck();
    }

    public void resetReady(UUID id) {
        for (Player player : games.get(id).getPlayers()) {
            player.setReady(false);
        }
    }

    @Override
    public boolean updatePlayerReady(UUID id, UUID playerID) {
        // todo: update and let the controller handle it
        Game game = games.get(id);
        switch (game.getGameState().getPhase()) {
            case PROGRAMMING:
                getPlayer(id, playerID).setReady(true);
                if (game.getGameState().getReadyPlayers() == game.getPlayerCount()) {
                    game.getGameLogicController().finishProgrammingPhase();

                    CompletableFuture<Void> executionFuture = CompletableFuture.runAsync(() -> {
                        game.getGameLogicController().executePrograms();
                    });

                    // Reset ready when the activation phase is over
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (executionFuture.isDone()) {
                                resetReady(id);
                                this.cancel();
                            }
                        }
                    }, 0, 10);
                }
                return true;
            case WAITING:
                getPlayer(id, playerID).setReady(true);
                if (game.getGameState().getReadyPlayers() == game.getPlayerCount()) {
                    game.getGameLogicController().startProgrammingPhase();
                    resetReady(id);
                }
                return true;
            default:
                return false;
        }
    }
}
