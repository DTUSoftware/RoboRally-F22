package dk.dtu.compute.se.pisd.roborally_server.server.controller;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;
import dk.dtu.compute.se.pisd.roborally_server.server.service.IGameService;
import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadGameState.getPlayerDeckFromJSON;
import static dk.dtu.compute.se.pisd.roborally_server.server.controller.Utility.getResponseEntity;

/**
 * The RESTController for everything related to the game.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
@RestController
public class GameController {
    @Autowired
    private IGameService gameService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gets all games.
     *
     * @return JSON with all games.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/games", produces = "application/json")
    public ResponseEntity<String> getGames() {
        List<Game> games = gameService.findAll();
        JSONArray gamesJSON = new JSONArray();
        for (Game game : games) {
            try {
                JSONObject gameJSON = new JSONObject(objectMapper.writeValueAsString(game));
                gamesJSON.put(gameJSON);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return getResponseEntity(gamesJSON, "could not get games");
    }

    /**
     * Adds a new game to the server.
     *
     * @param mapID       ID of map
     * @param playerCount amount of players
     * @return created game object, with gameID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @PostMapping(value = "/games", produces = "application/json")
    public ResponseEntity<String> addGame(@RequestParam(defaultValue = "defaultboard") String mapID, @RequestParam(defaultValue = "2") int playerCount) {
        Game game = gameService.newGame(mapID, playerCount);
        JSONObject gameJSON = null;
        if (game != null) {
            try {
                gameJSON = new JSONObject(objectMapper.writeValueAsString(game));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return getResponseEntity(gameJSON, "game not added");
    }

    /**
     * Gets all saved games (from server's appdata).
     *
     * @return the saved games, if any
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/games/savedGames", produces = "application/json")
    public ResponseEntity<String> getGameStates() {
        JSONArray gameStatesUnique = gameService.findAllSavedGameStateUnique();
        return getResponseEntity(gameStatesUnique, "could not find any saved games");
    }

    /**
     * Gets a game with ID
     *
     * @param id game ID
     * @return the game, if found
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> getGameByID(@PathVariable UUID id) {
        Game game = gameService.getGameByID(id);
        JSONObject gameJSON = null;
        if (game != null) {
            try {
                gameJSON = new JSONObject(objectMapper.writeValueAsString(game));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return getResponseEntity(gameJSON, "could not get game");
    }

    /**
     * Updates a game.
     *
     * @param id   game ID
     * @param game new game
     * @return whether the game was updated or not
     */
    @PutMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> updateGame(@PathVariable UUID id, @RequestBody Game game) {
        boolean updated = gameService.updateGame(id, game);
        return getResponseEntity(updated, "game not updated");
    }

    /**
     * Deletes a game from the server.
     *
     * @param id id of game
     * @return whether it was deleted or not.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @DeleteMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> deleteGame(@PathVariable UUID id) {
        boolean deleted = gameService.deleteGameByID(id);
        return getResponseEntity(deleted, "game not deleted");
    }

    /**
     * Gets gamestate of game with ID.
     *
     * @param id the gameID
     * @return gamestate, if found
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/games/{id}/gameState", produces = "application/json")
    public ResponseEntity<String> getGameStateByID(@PathVariable UUID id) {
        GameState gameState = gameService.getGameStateByID(id);
        JSONObject gameStateJSON = null;
        if (gameState != null) {
            try {
                gameStateJSON = new JSONObject(objectMapper.writeValueAsString(gameState));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return getResponseEntity(gameStateJSON, "gamestate not found");
    }

    /**
     * Saves gamestate of game to server.
     *
     * @param id gameID to save
     * @return whether it was saved or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @PostMapping(value = "/games/{id}/gameState/save", produces = "application/json")
    public ResponseEntity<String> saveGameState(@PathVariable UUID id) {
        boolean status = gameService.saveGameStateByID(id);
        return getResponseEntity(status, "could not save game state!");
    }

    /**
     * Loads the newest gameState of game with ID.
     *
     * @param id gameID
     * @return game that was loaded/created/updated, if successful
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @PostMapping(value = "/games/{id}/gameState/load", produces = "application/json")
    public ResponseEntity<String> loadGameState(@PathVariable UUID id) {
        Game game = gameService.loadSavedGame(id);
        JSONObject gameJSON = null;
        if (game != null) {
            try {
                gameJSON = new JSONObject(objectMapper.writeValueAsString(game));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return getResponseEntity(gameJSON, "gamestate not loaded");
    }

    /**
     * Gets player deck of player
     *
     * @param id       gameID
     * @param playerID playerID
     * @return the player's deck
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/games/{id}/gameState/{playerID}", produces = "application/json")
    public ResponseEntity<String> getPlayerDeck(@PathVariable UUID id, @PathVariable UUID playerID) {
        PlayerDeck playerDeck = gameService.getPlayerDeck(id, playerID);
        JSONObject playerDeckJSON = null;
        try {
            playerDeckJSON = new JSONObject(objectMapper.writeValueAsString(playerDeck));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return getResponseEntity(playerDeckJSON, "could not get player deck");
    }

    /**
     * Updates a player's deck (if new deck is legal).
     *
     * @param id             ID of game
     * @param playerID       playerID
     * @param playerDeckJSON player deck as JSON (like gameState)
     * @return whether the deck was updated
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @PostMapping(value = "/games/{id}/gameState/{playerID}", produces = "application/json")
    public ResponseEntity<String> updatePlayerDeck(@PathVariable UUID id, @PathVariable UUID playerID, @RequestBody String playerDeckJSON) {
        PlayerDeck playerDeck = getPlayerDeckFromJSON(new JSONObject(playerDeckJSON));
        boolean status = gameService.updatePlayerDeck(id, playerID, playerDeck);
        return getResponseEntity(status, "player deck not updated");
    }

    /**
     * Marks a player as ready.
     *
     * @param id       ID of game
     * @param playerID ID of player
     * @return whether player was marked as ready or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @PostMapping(value = "/games/{id}/gameState/{playerID}/ready", produces = "application/json")
    public ResponseEntity<String> playerProgrammingDone(@PathVariable UUID id, @PathVariable UUID playerID) {
        boolean status = gameService.updatePlayerReady(id, playerID);
        return getResponseEntity(status, "could not mark as ready!");
    }

    /**
     * Chooses an option, for example CARD options.
     *
     * @param id         ID of game
     * @param playerID   ID of player
     * @param cardOption option chosen
     * @return whether option was chosen successfully
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @PostMapping(value = "/games/{id}/gameState/{playerID}/chooseOption", produces = "application/json")
    public ResponseEntity<String> playerActivationChooseOption(@PathVariable UUID id, @PathVariable UUID playerID, @RequestParam(defaultValue = "none") String cardOption) {
        boolean status = gameService.chooseInteractionOption(id, playerID, cardOption);
        return getResponseEntity(status, "could not choose option!");
    }
}
