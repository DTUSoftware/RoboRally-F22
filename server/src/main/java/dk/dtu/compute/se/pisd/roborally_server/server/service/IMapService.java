package dk.dtu.compute.se.pisd.roborally_server.server.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IMapService {
    JSONArray findAll();
    public JSONObject getMapByID(String id);
}
