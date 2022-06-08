package dk.dtu.compute.se.pisd.roborally.server;

import dk.dtu.compute.se.pisd.roborally.model.CardField;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.cards.DamageCard;
import dk.dtu.compute.se.pisd.roborally.model.cards.ProgramCard;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpRequest;
import java.util.UUID;

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
     * Gets all saved games
     *
     * @return the games
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONArray getSavedGames() {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/savedGames", ServerConnector.RequestType.GET);
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONArray("result");
        }
        return null;
    }

    /**
     * Loads a saved game
     *
     * @param gameID       gameID
     * @return the new game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONObject loadSavedGame(UUID gameID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState/load", ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        if (responseJSON != null && responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    /**
     * Saves the given game
     *
     * @param gameID       gameID
     * @return whether or not it saved
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static boolean saveGame(UUID gameID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState/save", ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        return responseJSON != null && responseJSON.has("result");
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

    /**
     * Gets a player's gamestate as a JSONObject
     *
     * @param player the player
     * @return JSON object
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private static JSONObject getPlayerGameState(Player player) {
        JSONObject playerJSON = new JSONObject();

        playerJSON.put("name", player.getName());
        playerJSON.put("color", player.getColor());
        playerJSON.put("energy", player.getEnergy());
        playerJSON.put("currentCheckpoint", player.getCurrentCheckpoint());

        JSONObject position = new JSONObject();
        position.put("x", player.getSpace().x);
        position.put("y", player.getSpace().y);
        position.put("heading", player.getHeading().name());
        playerJSON.put("position", position);

        JSONArray program = new JSONArray();
        for (int j = 0; j < Player.NO_REGISTERS; j++) {
            JSONObject programCard = new JSONObject();
            CardField field = player.getProgramField(j);
            if (field != null && field.getCard() != null) {
                switch (field.getCard().getType()) {
                    case PROGRAM:
                        programCard.put("type", "PROGRAM");
                        programCard.put("program", ((ProgramCard) field.getCard()).getProgram().name());
                        break;
                    case DAMAGE:
                        programCard.put("type", "DAMAGE");
                        programCard.put("damage", ((DamageCard) field.getCard()).getDamage().name());
                        break;
                    default:
                        continue;
                }
                programCard.put("visible", field.isVisible());
                program.put(programCard);
            }
        }
        playerJSON.put("program", program);

        JSONArray cards = new JSONArray();
        for (int j = 0; j < Player.NO_CARDS; j++) {
            JSONObject cardsJSON = new JSONObject();
            CardField field = player.getCardField(j);
            if (field != null && field.getCard() != null) {
                switch (field.getCard().getType()) {
                    case PROGRAM:
                        cardsJSON.put("type", "PROGRAM");
                        cardsJSON.put("program", ((ProgramCard) field.getCard()).getProgram().name());
                        break;
                    case DAMAGE:
                        cardsJSON.put("type", "DAMAGE");
                        cardsJSON.put("damage", ((DamageCard) field.getCard()).getDamage().name());
                        break;
                    default:
                        continue;
                }
                cardsJSON.put("visible", field.isVisible());
                cards.put(cardsJSON);
            }
        }
        playerJSON.put("cards", cards);

        return playerJSON;
    }
}
