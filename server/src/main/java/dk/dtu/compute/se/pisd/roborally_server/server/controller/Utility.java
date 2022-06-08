package dk.dtu.compute.se.pisd.roborally_server.server.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for function that's used in all RESTControllers.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Utility {
    /**
     * Gets a responseEntity from given result and error message, if result was "bad"/error.
     *
     * @param result       the result, if any (can also be negative result)
     * @param errorMessage error to give in case of negative result
     * @return the ResponseEntity
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static ResponseEntity<String> getResponseEntity(Object result, String errorMessage) {
        JSONObject response = new JSONObject();
        if (result != null && !(result instanceof Boolean && !((boolean) result))) {
            response.put("status", "ok");
            if (!(result instanceof Boolean)) {
                response.put("result", result);
            }
            return ResponseEntity.ok().body(response.toString());
        } else {
            response.put("status", "error");
            response.put("message", errorMessage);
            return ResponseEntity.internalServerError().body(response.toString());
        }
    }
}
