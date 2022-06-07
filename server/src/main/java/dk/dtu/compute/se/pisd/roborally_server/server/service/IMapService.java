package dk.dtu.compute.se.pisd.roborally_server.server.service;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Map Service Interface.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public interface IMapService {
    /**
     * Finds all MapID's available on the server.
     *
     * @return the mapID's.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    JSONArray findAll();

    /**
     * Gets the map layout of map with given mapID.
     *
     * @param id the mapID
     * @return the map layout
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public JSONObject getMapByID(String id);
}
