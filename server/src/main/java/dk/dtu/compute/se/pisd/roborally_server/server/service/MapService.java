package dk.dtu.compute.se.pisd.roborally_server.server.service;

import com.google.common.io.Resources;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Map Service Interface.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
@Service
public class MapService implements IMapService {
    @Autowired
    private IJSONService jsonService;

    /** The map list */
    private static final String[] maps = new String[] {"chop_shop_challenge", "defaultboard", "dizzy_highway", "risky_crossing"};

    /**
     * Finds all MapID's available on the server.
     *
     * @return the mapID's.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public JSONArray findAll() {
        JSONArray maps = new JSONArray();
        List<String> mapIDs = jsonService.getFolderJSON("maps");

        // Add the static maps
        for (String mapID : MapService.maps) {
            if (!mapIDs.contains(mapID)) {
                mapIDs.add(mapID);
            }
        }

        for (String mapID : mapIDs) {
            JSONObject map = new JSONObject();
            map.put("id", mapID);
            maps.put(map);
        }
        return maps;
    }

    /**
     * Gets the map layout of map with given mapID.
     *
     * @param id the mapID
     * @return the map layout
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public JSONObject getMapByID(String id) {
        if (id == null) {
            id = "defaultboard";
        }

        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource("maps/" + id + ".json").openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            return null;
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        return new JSONObject(tokener);
    }
}
