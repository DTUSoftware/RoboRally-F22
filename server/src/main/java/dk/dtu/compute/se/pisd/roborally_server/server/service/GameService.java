package dk.dtu.compute.se.pisd.roborally_server.server.service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState;
import dk.dtu.compute.se.pisd.roborally_server.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState.loadGameState;
import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState.saveGameState;

/**
 * Game Service.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
@Service
public class GameService implements IGameService {
    @Autowired
    private IJSONService jsonService;

    private static HashMap<UUID, Game> games = new HashMap<>();

    /**
     * Creates a new gameService.
     */
    public GameService() {
//        games.put(10, new Game(10));
//        games.put(11, new Game(11));
//        games.put(12, new Game(12));
//        games.put(13, new Game(13));
    }

    /**
     * Finds all games.
     *
     * @return all games.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public List<Game> findAll() {
        return games.values().stream().toList();
    }

    /**
     * Gets game with ID.
     *
     * @param id the ID.
     * @return the game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public Game getGameByID(UUID id) {
        if (!games.containsKey(id)) {
            return null;
        }
        return games.get(id);
    }

    /**
     * Creates a new game with mapID and playerCount.
     *
     * @param mapID       mapID
     * @param playerCount player count
     * @return created game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public Game newGame(String mapID, int playerCount) {
        Game game = new Game(mapID, playerCount);
        games.put(game.getID(), game);
        return game;
    }

    /**
     * Adds a game to the service.
     *
     * @param game the game to add
     * @return whether the game was added or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean addGame(Game game) {
        games.put(game.getID(), game);
        return true;
    }

    /**
     * Loads a saved game
     *
     * @param id the game ID of game to load
     * @return the loaded game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public Game loadSavedGame(UUID id) {
        List<String> gameStateNames = jsonService.getFolderJSON(LoadGameState.GAMESTATEFOLDER);
        ArrayList<String> gameStatesWithID = new ArrayList<>(gameStateNames.size());
//        System.out.println(gameStateNames);
        for (String gameStateName : gameStateNames) {
            if (gameStateName.contains(" ")) {
                UUID gameID = null;
                try {
                    gameID = UUID.fromString(gameStateName.split(" ")[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (gameID != null && gameID.equals(id)) {
                    gameStatesWithID.add(gameStateName);
                }
            }
        }
//        System.out.println(gameStatesWithID);

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
            } else {
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

//        System.out.println(newestFilename);

        Game game = getGameByID(id);
        if (game == null) {
            game = new Game(id);
        }

        loadGameState(game, newestFilename);
        games.put(game.getID(), game);
        return game;
    }

    /**
     * Updates a game with id (doesn't update everything).
     *
     * @param id   gameid
     * @param game game to update with
     * @return whether it was updated
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean updateGame(UUID id, Game game) {
        Game gameFound = getGameByID(id);
        if (gameFound != null) {
            gameFound.setID(game.getID());
            gameFound.setMapID(game.getMapID());
            gameFound.setPlayerCount(game.getPlayerCount());
            return true;
        }
        return false;
    }

    /**
     * Deletes a game from the service.
     *
     * @param id the ID of the game to delete
     * @return whether it was deleted
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean deleteGameByID(UUID id) {
        if (games.containsKey(id)) {
            games.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Finds all saved gamestates with unique game ID's.
     *
     * @return the unique game ID's.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
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
                } catch (Exception e) {
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

    /**
     * Get gamestate from gameID.
     *
     * @param id gameID
     * @return gamestate
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public GameState getGameStateByID(UUID id) {
        Game game = getGameByID(id);
        if (game == null) {
            return null;
        }
        return game.getGameState();
    }

    /**
     * Save gamestate from game.
     *
     * @param id gameID of game
     * @return whether saved or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean saveGameStateByID(UUID id) {
        Game game = getGameByID(id);
        if (game == null) {
            return false;
        }
        saveGameState(game);
        return true;
    }

    /**
     * Updates a player's deck to new deck.
     *
     * @param id         gameID
     * @param playerID   playerID
     * @param playerDeck new deck
     * @return whether updated or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean updatePlayerDeck(UUID id, UUID playerID, PlayerDeck playerDeck) {
        if (playerDeck == null) {
            return false;
        }

        Player player = getPlayer(id, playerID);
        if (player == null || player.getReady() == true) {
            return false;
        }

        PlayerDeck currentPlayerDeck = player.getDeck();
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

    /**
     * Gets player.
     *
     * @param id       gameID
     * @param playerID playerID
     * @return the player, if in game.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public Player getPlayer(UUID id, UUID playerID) {
        Game game = getGameByID(id);
        if (game == null) {
            return null;
        }

        return game.getPlayer(playerID);
    }

    /**
     * Gets Player's Deck.
     *
     * @param id       gameID
     * @param playerID playerID
     * @return the deck of player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public PlayerDeck getPlayerDeck(UUID id, UUID playerID) {
        Player player = getPlayer(id, playerID);
        if (player == null) {
            return null;
        }
        return player.getDeck();
    }

    /**
     * Reset's every Player's ready status.
     *
     * @param id gameID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void resetReady(UUID id) {
        Game game = getGameByID(id);
        if (game != null) {
            for (Player player : game.getPlayers()) {
                player.setReady(false);
            }
        }
    }

    /**
     * Updates Player's ready status to Ready
     *
     * @param id       gameID
     * @param playerID playerID
     * @return if possible/valid change, return true.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean updatePlayerReady(UUID id, UUID playerID) {
        // todo: update and let the controller handle it?
        Game game = getGameByID(id);
        if (game == null) {
            return false;
        }

        Player player = getPlayer(id, playerID);
        if (player == null) {
            return false;
        }

        switch (game.getGameState().getPhase()) {
            case PROGRAMMING:
                player.setReady(true);
                game.getGameLogicController().debug(player.getName() + " is ready! " + game.getGameState().getReadyPlayers() + "/" + game.getPlayerCount());
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
                player.setReady(true);
                game.getGameLogicController().debug(player.getName() + " is ready! " + game.getGameState().getReadyPlayers() + "/" + game.getPlayerCount());
                if (game.getGameState().getReadyPlayers() == game.getPlayerCount()) {
                    game.getGameLogicController().startProgrammingPhase();
                    resetReady(id);
                }
                return true;
            default:
                return false;
        }
    }

    /**
     * Choose option of interaction.
     *
     * @param id         gameID
     * @param playerID   playerID
     * @param optionName card name of option
     * @return whether option was chosen successfully
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public boolean chooseInteractionOption(UUID id, UUID playerID, String optionName) {
        if (optionName == null || optionName.isEmpty() || optionName.equals("none")) {
            return false;
        }

        Game game = getGameByID(id);
        if (game == null) {
            return false;
        }

        Player player = getPlayer(id, playerID);
        if (player == null) {
            return false;
        }

        if (game.getGameState().getPhase() != Phase.PLAYER_INTERACTION) {
            return false;
        }

        if (game.getGameState().getCurrentPlayer() != game.getGameState().getPlayerNumber(player)) {
            return false;
        }

        return game.getGameLogicController().setCommandCardOptionAndContinue(optionName);
    }
}
