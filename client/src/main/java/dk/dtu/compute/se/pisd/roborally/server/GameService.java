package dk.dtu.compute.se.pisd.roborally.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class GameService {
    public static JSONObject getGame(int id) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+id, ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static JSONObject newGame(String mapID, int playerCount) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games?mapID="+mapID+"&playerCount="+playerCount, ServerConnector.RequestType.POST);
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

    public static JSONObject getGameState(int gameID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState", ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static JSONObject getPlayer(int gameID, UUID playerID) {
        JSONObject responseJSON = ServerConnector.sendRequest("/games/"+gameID+"/gameState/"+playerID, ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }
}
