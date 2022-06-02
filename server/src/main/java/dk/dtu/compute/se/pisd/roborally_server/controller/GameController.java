package dk.dtu.compute.se.pisd.roborally_server.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.service.IGameService;
import dk.dtu.compute.se.pisd.roborally_server.model.Game;
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

    @GetMapping(value = "/games", produces = "application/json")
    public ResponseEntity<String> getGames() {
        List<Game> games = gameService.findAll();
        return getResponseEntity(games, "could not get games");
    }

    @PostMapping(value = "/games", produces = "application/json")
    public ResponseEntity<String> addGame(@RequestBody Game game) {
        boolean added = gameService.addGame(game);
        return getResponseEntity(added, "game not added");
    }

    @GetMapping(value = "/games/{id}", produces = "application/json")
    public ResponseEntity<String> getGameByID(@PathVariable int id) {
        Game game = gameService.getGameByID(id);
        ObjectMapper objectMapper = new ObjectMapper();
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
        return getResponseEntity(gameState, "gamestate not found");
    }

    @GetMapping(value = "/games/{id}/gameState/{playerID}", produces = "application/json")
    public ResponseEntity<String> getPlayer(@PathVariable int id, @PathVariable int playerID) {
        Player player = gameService.getPlayerDeck(id, playerID);
        return getResponseEntity(player, "could not get player deck");
    }

    @PostMapping(value = "/games/{id}/gameState/{playerID}", produces = "application/json")
    public ResponseEntity<String> updatePlayer(@PathVariable int id, @PathVariable int playerID, @RequestBody Player player) {
        boolean status = gameService.updatePlayerDeck(id, playerID, player);
        return getResponseEntity(status, "player deck not updated");
    }
}
