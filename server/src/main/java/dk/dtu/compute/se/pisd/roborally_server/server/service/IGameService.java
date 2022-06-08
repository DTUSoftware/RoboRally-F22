package dk.dtu.compute.se.pisd.roborally_server.server.service;

import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.PlayerDeck;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Game Service Interface.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public interface IGameService {
    /**
     * Finds all games.
     *
     * @return all games.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    List<Game> findAll();

    /**
     * Gets game with ID.
     *
     * @param id the ID.
     * @return the game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    Game getGameByID(UUID id);

    /**
     * Creates a new game with mapID and playerCount.
     *
     * @param mapID       mapID
     * @param playerCount player count
     * @return created game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    Game newGame(String mapID, int playerCount);

    /**
     * Loads a saved game
     *
     * @param id the game ID of game to load
     * @return the loaded game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    Game loadSavedGame(UUID id);

    /**
     * Adds a game to the service.
     *
     * @param game the game to add
     * @return whether the game was added or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean addGame(Game game);

    /**
     * Updates a game with id.
     *
     * @param id   gameid
     * @param game game to update with
     * @return whether it was updated
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean updateGame(UUID id, Game game);

    /**
     * Deletes a game from the service.
     *
     * @param id the ID of the game to delete
     * @return whether it was deleted
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean deleteGameByID(UUID id);

    /**
     * Finds all saved gamestates with unique game ID's.
     *
     * @return the unique game ID's.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    JSONArray findAllSavedGameStateUnique();

    /**
     * Get gamestate from gameID.
     *
     * @param id gameID
     * @return gamestate
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    GameState getGameStateByID(UUID id);

    /**
     * Save gamestate from game.
     *
     * @param id gameID of game
     * @return whether saved or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean saveGameStateByID(UUID id);

    /**
     * Updates a player's deck to new deck.
     *
     * @param id         gameID
     * @param playerID   playerID
     * @param playerDeck new deck
     * @return whether updated or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean updatePlayerDeck(UUID id, UUID playerID, PlayerDeck playerDeck);

    /**
     * Gets player.
     *
     * @param id       gameID
     * @param playerID playerID
     * @return the player, if in game.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    Player getPlayer(UUID id, UUID playerID);

    /**
     * Gets Player's Deck.
     *
     * @param id       gameID
     * @param playerID playerID
     * @return the deck of player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    PlayerDeck getPlayerDeck(UUID id, UUID playerID);

    /**
     * Updates Player's ready status to Ready
     *
     * @param id       gameID
     * @param playerID playerID
     * @return if possible/valid change, return true.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean updatePlayerReady(UUID id, UUID playerID);

    /**
     * Choose option of interaction.
     *
     * @param id         gameID
     * @param playerID   playerID
     * @param optionName card name of option
     * @return whether option was chosen successfully
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    boolean chooseInteractionOption(UUID id, UUID playerID, String optionName);
}
