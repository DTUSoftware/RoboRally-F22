package dk.dtu.compute.se.pisd.roborally.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpRequest;
import java.util.UUID;

public class GameService {
    public static JSONObject getGame(UUID id) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+id, ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static JSONObject newGame(String mapID, int playerCount) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games?mapID="+mapID+"&playerCount="+playerCount, ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static JSONArray getGames() {
        JSONObject responseJSON = ServerConnector.sendRequest("/games", ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONArray("result");
        }
        return null;
    }

    public static JSONObject getGameState(UUID gameID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState", ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static JSONObject getPlayerDeck(UUID gameID, UUID playerID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState/"+playerID, ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static boolean markPlayerReady(UUID gameID, UUID playerID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState/"+playerID+"/ready", ServerConnector.RequestType.POST, HttpRequest.BodyPublishers.ofString(""));
        if (responseJSON.has("result")) {
            return true;
        }
        return false;
    }
}
