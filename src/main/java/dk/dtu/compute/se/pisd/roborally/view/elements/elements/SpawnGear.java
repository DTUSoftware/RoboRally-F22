package dk.dtu.compute.se.pisd.roborally.view.elements.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class SpawnGear extends FieldElement {
    private Heading spawnDirection;

    /**
     * Creates a new spawn gear.
     *
     * @param space
     * @param spawnDirection the direction that the robot should face
     */
    public SpawnGear(Space space, Heading spawnDirection) {
        super(space);
        this.spawnDirection = spawnDirection;
    }

    public Heading getDirection() {
        return spawnDirection;
    }

    @Override
    public void doLandingAction() {

    }
}
