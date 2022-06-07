package dk.dtu.compute.se.pisd.roborally.server;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The connector used to send requests to the server (and possibly hold a WebSocket connection in the future..?)
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class ServerConnector {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    /**
     * The type of request to send.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    enum RequestType {
        POST,
        GET,
        PUT,
        DELETE
    }

    /**
     * Creates a new serverConnector.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ServerConnector() {
    }

    /**
     * Gets server address and port from config.
     *
     * @return the server address and port
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private static JSONObject getServerDetails() {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource("config.json").openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            return null;
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject configJSON = new JSONObject(tokener);

        return configJSON.getJSONObject("server");
    }

    /**
     * Gets URL of server, using server details from config.
     *
     * @return the server URL
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private static String getServerURL() {
        JSONObject serverDetails = getServerDetails();
        if (serverDetails != null) {
            int port = serverDetails.getInt("port");
            String address = serverDetails.getString("address");
            if (port == 80) {
                return address;
            } else {
                return address + ":" + Integer.toString(port);
            }
        }
        return "http://localhost";
    }

    /**
     * Package-private function to send requests to the server, using given endpoint, requesttype and body.
     *
     * @param endpoint    the endpoint to ask (do NOT include server address and/or port)
     * @param requestType the requestType
     * @param body        the body
     * @return the response
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    static JSONObject sendRequest(String endpoint, RequestType requestType, HttpRequest.BodyPublisher body) {
        HttpRequest request;

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.setHeader("User-Agent", "RoboRally Client");
        requestBuilder.setHeader("Accept", "application/json");
        requestBuilder.setHeader("Content-Type", "application/json");
        requestBuilder.uri(URI.create(getServerURL() + endpoint));

        switch (requestType) {
            case POST:
                if (body == null) {
                    return null;
                }
                requestBuilder.POST(body);
                break;
            case GET:
                requestBuilder.GET();
                break;
            case PUT:
                if (body == null) {
                    return null;
                }
                requestBuilder.PUT(body);
                break;
            case DELETE:
                requestBuilder.DELETE();
                break;
        }

        request = requestBuilder.build();

        System.out.println(request.toString());

        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = null;
        try {
            result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            System.out.println("Could not get response from server!");
            // e.printStackTrace();
        }

        if (result != null) {
            System.out.println(result);
            return new JSONObject(result);
        }

        return null;
    }

    /**
     * Package-private function to send a request to endpoint and with requestType.
     *
     * @param endpoint    the endpoint (do NOT include server address and/or port)
     * @param requestType the requestType
     * @return the response
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    static JSONObject sendRequest(String endpoint, RequestType requestType) {
        return sendRequest(endpoint, requestType, null);
    }
}
