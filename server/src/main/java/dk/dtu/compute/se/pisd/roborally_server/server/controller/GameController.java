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

@RestController
public class GameController {
    @Autowired
    private IGameService gameService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    @GetMapping(value = "/games/savedGames", produces = "application/json")
    public ResponseEntity<String> getGameStates() {
        JSONArray gameStatesUnique = gameService.findAllSavedGameStateUnique();
        return getResponseEntity(gameStatesUnique, "could not find any saved games");
    }

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

    @PutMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> updateGame(@PathVariable UUID id, @RequestBody Game game) {
        boolean updated = gameService.updateGame(id, game);
        return getResponseEntity(updated, "game not updated");
    }

    @DeleteMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> deleteGame(@PathVariable UUID id) {
        boolean deleted = gameService.deleteGameByID(id);
        return getResponseEntity(deleted, "game not deleted");
    }

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

    @PostMapping(value = "/games/{id}/gameState/save", produces = "application/json")
    public ResponseEntity<String> saveGameState(@PathVariable UUID id) {
        boolean status = gameService.saveGameStateByID(id);
        return getResponseEntity(status, "could not save game state!");
    }

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

    @PostMapping(value = "/games/{id}/gameState/{playerID}", produces = "application/json")
    public ResponseEntity<String> updatePlayerDeck(@PathVariable UUID id, @PathVariable UUID playerID, @RequestBody String playerDeckJSON) {
        PlayerDeck playerDeck = getPlayerDeckFromJSON(new JSONObject(playerDeckJSON));
        boolean status = gameService.updatePlayerDeck(id, playerID, playerDeck);
        return getResponseEntity(status, "player deck not updated");
    }

    @PostMapping(value = "/games/{id}/gameState/{playerID}/ready", produces = "application/json")
    public ResponseEntity<String> playerProgrammingDone(@PathVariable UUID id, @PathVariable UUID playerID) {
        boolean status = gameService.updatePlayerReady(id, playerID);
        return getResponseEntity(status, "could not mark as ready!");
    }

    @PostMapping(value = "/games/{id}/gameState/{playerID}/chooseOption", produces = "application/json")
    public ResponseEntity<String> playerActivationChooseOption(@PathVariable UUID id, @PathVariable UUID playerID, @RequestParam(defaultValue = "none") String cardOption) {
        boolean status = gameService.chooseInteractionOption(id, playerID, cardOption);
        return getResponseEntity(status, "could not choose option!");
    }
}
