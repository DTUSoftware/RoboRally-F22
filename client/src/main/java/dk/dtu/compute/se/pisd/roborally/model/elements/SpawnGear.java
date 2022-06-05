package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * SpawnGear class where players spawn
 */
public class SpawnGear extends FieldElement {

    /**
     * Creates a new spawn gear.
     *
     * @param space the space
     * @param spawnDirection the direction that the robot should face
     */
    public SpawnGear(Space space, Heading spawnDirection) {
        super(space, spawnDirection);
    }
}
