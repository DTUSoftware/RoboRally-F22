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
        JSONObject response = new JSONObject();
        JSONArray maps = mapService.findAll();
        if (maps != null) {
            response.put("status", "ok");
            response.put("result", maps);
        }
        else {
            response.put("status", "error");
            response.put("message", "could not load maps");
        }
        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping(value = "/maps/{id}", produces = "application/json")
    public ResponseEntity<String> getMapByID(@PathVariable String id) {
        JSONObject response = new JSONObject();
        JSONObject map = mapService.getMapByID(id);
        if (map != null) {
            response.put("status", "ok");
            response.put("result", map);
        }
        else {
            response.put("status", "error");
            response.put("message", "could not load map");
        }
        return ResponseEntity.ok().body(response.toString());
    }
}
