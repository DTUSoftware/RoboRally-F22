package dk.dtu.compute.se.pisd.roborally_server.server.controller;

import dk.dtu.compute.se.pisd.roborally_server.server.service.IMapService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dk.dtu.compute.se.pisd.roborally_server.server.controller.Utility.getResponseEntity;

/**
 * RESTController for the maps.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
@RestController
public class MapController {
    @Autowired
    private IMapService mapService;

    /**
     * Gets all mapID's on the server.
     *
     * @return the mapID's.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/maps", produces = "application/json")
    public ResponseEntity<String> getMaps() {
        JSONArray maps = mapService.findAll();
        return getResponseEntity(maps, "could not load maps");
    }

    /**
     * Gets the map layout of map with specified mapID.
     *
     * @param id mapID.
     * @return the map layout
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @GetMapping(value = "/maps/{id}", produces = "application/json")
    public ResponseEntity<String> getMapByID(@PathVariable String id) {
        JSONObject map = mapService.getMapByID(id);
        return getResponseEntity(map, "could not get map");
    }
}
