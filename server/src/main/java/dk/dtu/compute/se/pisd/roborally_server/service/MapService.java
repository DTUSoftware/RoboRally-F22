package dk.dtu.compute.se.pisd.roborally_server.service;

import com.google.common.io.Resources;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class MapService implements IMapService {
    @Autowired
    private IJSONService jsonService;

    @Override
    public JSONArray findAll() {
        JSONArray maps = new JSONArray();
        List<String> mapIDs = jsonService.getFolderJSON("maps");
        for (String mapID : mapIDs) {
            JSONObject map = new JSONObject();
            map.put("id", mapID);
            maps.put(map);
        }
        return maps;
    }

    @Override
    public JSONObject getMapByID(String id) {
        if (id == null) {
            id = "defaultboard";
        }

        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource("maps/" + id + ".json").openStream();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            return null;
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        return new JSONObject(tokener);
    }
}
