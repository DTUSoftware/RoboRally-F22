package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * converyorbelt object
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class ConveyorBelt extends FieldElement {
    private boolean color;
    private Heading direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param space     the space to put the conveyorbelt
     * @param color     the color of the belt blue/green is true/false
     * @param direction the direction for the conveyorbelt
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ConveyorBelt(Space space, boolean color, Heading direction) {
        super(space);
        this.color = color;
        this.direction = direction;
    }

    /**
     * gets the color
     *
     * @return color
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean getColor() {
        return color;
    }

    /**
     * gets the heading
     *
     * @return direction
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Heading getDirection() {
        return direction;
    }
}
