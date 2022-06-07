package dk.dtu.compute.se.pisd.roborally.server;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpRequest;
import java.util.UUID;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadGameState.getPlayerGameState;

/**
 * The GameService, that sends queries to the ServerConnector at certain endpoints.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class GameService {
    /**
     * Gets a game with certain ID
     *
     * @param id the game ID
     * @return the game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONObject getGame(UUID id) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/" + id, ServerConnector.RequestType.GET);
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    /**
     * Creates a new game
     *
     * @param mapID       mapID of map
     * @param playerCount playercount
     * @return the new game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONObject newGame(String mapID, int playerCount) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games?mapID=" + mapID + "&playerCount=" + playerCount, ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    /**
     * Gets all active games
     *
     * @return the games
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONArray getGames() {
        JSONObject responseJSON = ServerConnector.sendRequest("/games", ServerConnector.RequestType.GET);
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONArray("result");
        }
        return null;
    }

    /**
     * Get gamestate of game
     *
     * @param gameID gameID
     * @return the gameState
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONObject getGameState(UUID gameID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/" + gameID + "/gameState", ServerConnector.RequestType.GET);
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    /**
     * Get a player's deck
     *
     * @param gameID   the gameID
     * @param playerID the playerID
     * @return the player's deck
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONObject getPlayerDeck(UUID gameID, UUID playerID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/" + gameID + "/gameState/" + playerID, ServerConnector.RequestType.GET);
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    /**
     * Marks a player as ready
     *
     * @param gameID   the game ID
     * @param playerID the playerID
     * @return whether it was successful
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static boolean markPlayerReady(UUID gameID, UUID playerID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/" + gameID + "/gameState/" + playerID + "/ready", ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        return responseJSON != null && responseJSON.has("result");
    }

    /**
     * Updates a player's gamedeck on the server with the local deck, if valid.
     *
     * @param gameID the gameID
     * @param player the playerID
     * @return whether it was updated successfully
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static boolean updatePlayerDeck(UUID gameID, Player player) {
        JSONObject playerGameState = getPlayerGameState(player);
        JSONObject responseJSON = ServerConnector.sendRequest("/games/" + gameID + "/gameState/" + player.getID(), ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(playerGameState.toString()));
        return responseJSON != null && responseJSON.has("result");
    }

    /**
     * Allows a player to choose an option of cards.
     *
     * @param gameID     the gameID
     * @param playerID   the playerID
     * @param cardOption chosen option
     * @return successful or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static boolean chooseOption(UUID gameID, UUID playerID, String cardOption) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/" + gameID + "/gameState/" + playerID + "/chooseOption?cardOption=" + cardOption, ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        return responseJSON != null && responseJSON.has("result");
    }
}
