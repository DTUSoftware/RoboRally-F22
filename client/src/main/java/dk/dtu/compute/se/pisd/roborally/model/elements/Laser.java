package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

// TODO make this stuff

/**
 * The laser object
 */
public class Laser extends FieldElement{
    private Heading direction;
    private int lazer;

    /**
     * constructor for the laser
     * @param space the space to put the lazer
     * @param direction the direction for the laser
     */
    public Laser(Space space, Heading direction, int lazer) {
        super(space);
        this.direction = direction;
        this.lazer = lazer;
    }

    /**
     * gets the heading
     * @return direction
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets the lazer number
     * @return lazer number
     */
    public int getLazer() {
        return lazer;
    }
}
