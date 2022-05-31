package dk.dtu.compute.se.pisd.roborally_server;

import java.net.http.HttpClient;
import java.time.Duration;

public class RoboRallyServer {
    public static HttpClient httpClient;

    public static void main(String[] args) {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build();


    }

}
