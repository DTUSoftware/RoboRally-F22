package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * The rotating gear object
 *
 * @author Mads G. E. Hansen
 */
public class Gear extends FieldElement {
    private boolean direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param space     where to put the gear
     * @param direction the direction of the gear
     * @author Mads G. E. Hansen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Gear(Space space, boolean direction) {
        super(space);
        this.direction = direction;
    }

    /**
     * getter for the direction of the gear
     *
     * @return direction
     * @author Mads G. E. Hansen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean getGearDirection() {
        return direction;
    }
}
