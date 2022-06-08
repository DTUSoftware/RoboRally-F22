package dk.dtu.compute.se.pisd.roborally_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Spring Boot Application for the RoboRally Server.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
@SpringBootApplication
public class RoboRallyServerApplication {
    /**
     * Main Function for the RoboRally Server.
     *
     * @param args arguments to pass to the server.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static void main(String[] args) {
        SpringApplication.run(RoboRallyServerApplication.class, args);
    }
}
