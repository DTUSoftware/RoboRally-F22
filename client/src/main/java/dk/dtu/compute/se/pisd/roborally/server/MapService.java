package dk.dtu.compute.se.pisd.roborally.server;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapService {
    public static JSONObject getMap(String mapName) {
        JSONObject responseJSON = ServerConnector.sendRequest("/maps/"+mapName, ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    public static JSONArray getMaps() {
        JSONObject responseJSON = ServerConnector.sendRequest("/maps", ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONArray("result");
        }
        return null;
    }
}
