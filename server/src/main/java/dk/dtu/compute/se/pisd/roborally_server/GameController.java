package dk.dtu.compute.se.pisd.roborally_server;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
    @Autowired
    private IGameService gameService;

    @GetMapping("/games")
    public ResponseEntity<List<Game>> getGames() {
        List<Game> games = gameService.findAll();
        return ResponseEntity.ok().body(games);
    }

    @PostMapping("/games")
    public ResponseEntity<String > addGame(@RequestBody Game game) {
        boolean added = gameService.addGame(game);
        if(added) {
            return ResponseEntity.ok().body("added");
        }
        else {
            return ResponseEntity.internalServerError().body("not added");
        }

    }

    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGameByID(@PathVariable int id) {
        Game game = gameService.getGameByID(id);
        return ResponseEntity.ok().body(game);
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<String> updateGame(@PathVariable int id, @RequestBody Game game) {
        boolean added = gameService.updateGame(id, game);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable int id) {
        boolean deleted = gameService.deleteGameByID(id);
        if (deleted) {
            return ResponseEntity.ok().body("deleted");
        }
        else {
            return ResponseEntity.internalServerError().body("not deleted");
        }
    }
}
