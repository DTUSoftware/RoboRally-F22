package dk.dtu.compute.se.pisd.roborally_server.controller;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;
import dk.dtu.compute.se.pisd.roborally_server.service.IGameService;
import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dk.dtu.compute.se.pisd.roborally_server.controller.Utility.getResponseEntity;

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
    public ResponseEntity<String> addGame(@RequestBody Game game) {
        boolean added = gameService.addGame(game);
        return getResponseEntity(added, "game not added");
    }

    @GetMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> getGameByID(@PathVariable int id) {
        Game game = gameService.getGameByID(id);
        JSONObject gameJSON = null;
        try {
            gameJSON = new JSONObject(objectMapper.writeValueAsString(game));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return getResponseEntity(gameJSON, "could not get game");
    }

    @PutMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> updateGame(@PathVariable int id, @RequestBody Game game) {
        boolean updated = gameService.updateGame(id, game);
        return getResponseEntity(updated, "game not updated");
    }

    @DeleteMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> deleteGame(@PathVariable int id) {
        boolean deleted = gameService.deleteGameByID(id);
        return getResponseEntity(deleted, "game not deleted");
    }

    @GetMapping(value = "/games/{id}/gameState", produces = "application/json")
    public ResponseEntity<String> getGameStateByID(@PathVariable int id) {
        GameState gameState = gameService.getGameStateByID(id);
        JSONObject gameStateJSON = null;
        try {
            gameStateJSON = new JSONObject(objectMapper.writeValueAsString(gameState));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return getResponseEntity(gameStateJSON, "gamestate not found");
    }

    @GetMapping(value = "/games/{id}/gameState/{playerID}", produces = "application/json")
    public ResponseEntity<String> getPlayerDeck(@PathVariable int id, @PathVariable UUID playerID) {
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
    public ResponseEntity<String> updatePlayerDeck(@PathVariable int id, @PathVariable UUID playerID, @RequestBody PlayerDeck playerDeck) {
        boolean status = gameService.updatePlayerDeck(id, playerID, playerDeck);
        return getResponseEntity(status, "player deck not updated");
    }
}
