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
 * @author Marcus S. (s215827)
 */
public class ServerConnector {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    enum RequestType {
        POST,
        GET,
        PUT,
        DELETE
    }

    public ServerConnector() {}

    private static JSONObject getServerDetails() {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource("config.json").openStream();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            return null;
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject configJSON = new JSONObject(tokener);

        return configJSON.getJSONObject("server");
    }

    private static String getServerURL() {
        JSONObject serverDetails = getServerDetails();
        if (serverDetails != null) {
            int port = serverDetails.getInt("port");
            String address = serverDetails.getString("address");
            if (port == 80) {
                return address;
            }
            else {
                return address + ":" + Integer.toString(port);
            }
        }
        return "http://localhost";
    }

    static JSONObject sendRequest(String endpoint, RequestType requestType, HttpRequest.BodyPublisher body) {
        HttpRequest request;

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.setHeader("User-Agent", "RoboRally Client");
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

        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = null;
        try {
            result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        }

        if (result != null) {
            System.out.println(result);
            return new JSONObject(result);
        }

        return null;
    }

    static JSONObject sendRequest(String endpoint, RequestType requestType) {
        return sendRequest(endpoint, requestType, null);
    }
}
