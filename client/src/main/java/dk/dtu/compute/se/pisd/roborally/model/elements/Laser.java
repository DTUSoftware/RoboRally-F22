package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * The laser object
 *
 * @author Mads G. E. Hansen
 */
public class Laser extends FieldElement {
    private Heading direction;
    private int lazer;

    /**
     * constructor for the laser
     *
     * @param space     the space to put the lazer
     * @param direction the direction for the laser
     * @author Mads G. E. Hansen
     */
    public Laser(Space space, Heading direction, int lazer) {
        super(space);
        this.direction = direction;
        this.lazer = lazer;
    }

    /**
     * gets the heading
     *
     * @return direction
     * @author Mads G. E. Hansen
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets the lazer number
     *
     * @return lazer number
     * @author Mads G. E. Hansen
     */
    public int getLazer() {
        return lazer;
    }
}
