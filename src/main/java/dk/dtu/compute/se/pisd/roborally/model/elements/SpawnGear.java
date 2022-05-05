package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * SpawnGear class where players spawn
 */
public class SpawnGear extends SpawnableElement {

    /**
     * Creates a new spawn gear.
     *
     * @param gameController the gamecontroller
     * @param space the space
     * @param spawnDirection the direction that the robot should face
     */
    public SpawnGear(GameController gameController, Space space, Heading spawnDirection) {
        super(gameController, space, spawnDirection);
    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * not used
     */
    @Override
    public void activate() {

    }
}
