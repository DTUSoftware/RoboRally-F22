package dk.dtu.compute.se.pisd.roborally_server.controller;

import dk.dtu.compute.se.pisd.roborally_server.service.IMapService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MapController {
    @Autowired
    private IMapService mapService;

    @GetMapping(value = "/maps", produces = "application/json")
    public ResponseEntity<String> getMaps() {
        JSONArray maps = mapService.findAll();
        return ResponseEntity.ok().body(maps.toString());
    }

    @GetMapping(value = "/maps/{id}", produces = "application/json")
    public ResponseEntity<String> getMapByID(@PathVariable String id) {
        JSONObject map = mapService.getMapByID(id);
        if (map != null) {
            return ResponseEntity.ok().body(map.toString());
        }
        else {
            return ResponseEntity.internalServerError().body("could not load map");
        }
    }
}
