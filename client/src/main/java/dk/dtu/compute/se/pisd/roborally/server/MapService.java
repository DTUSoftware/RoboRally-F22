package dk.dtu.compute.se.pisd.roborally.server;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The Map Service.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class MapService {
    /**
     * Gets a map/board layout from server with mapname/mapID.
     *
     * @param mapName mapID/mapName
     * @return the map layout
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONObject getMap(String mapName) {
        JSONObject responseJSON = ServerConnector.sendRequest("/maps/" + mapName, ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONObject("result");
        }
        return null;
    }

    /**
     * Get all mapnames/mapID's on the server.
     *
     * @return the mapID's/mapNames
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static JSONArray getMaps() {
        JSONObject responseJSON = ServerConnector.sendRequest("/maps", ServerConnector.RequestType.GET);
        if (responseJSON.has("result")) {
            return responseJSON.getJSONArray("result");
        }
        return null;
    }
}
