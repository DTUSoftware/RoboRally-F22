package dk.dtu.compute.se.pisd.roborally_server.server.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class Utility {
    public static ResponseEntity<String> getResponseEntity(Object result, String errorMessage) {
        JSONObject response = new JSONObject();
        if (result != null && !(result instanceof Boolean && !((boolean) result))) {
            response.put("status", "ok");
            if (!(result instanceof Boolean)) {
                response.put("result", result);
            }
            return ResponseEntity.ok().body(response.toString());
        }
        else {
            response.put("status", "error");
            response.put("message", errorMessage);
            return ResponseEntity.internalServerError().body(response.toString());
        }
    }
}
