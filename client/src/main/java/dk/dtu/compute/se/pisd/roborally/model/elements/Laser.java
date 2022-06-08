package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * The laser object
 *
 * @author Oscar Maxwell
 * @author Nicolai Udbye
 * @author Mads G. E. Hansen
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Laser extends FieldElement {
    private Heading direction;
    private int lazer;

    /**
     * constructor for the laser
     *
     * @param space     the space to put the lazer
     * @param direction the direction for the laser
     * @param lazer     the number of the lazer
     * @author Mads G. E. Hansen
     * @author Oscar Maxwell
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets the lazer number
     *
     * @return lazer number
     * @author Mads G. E. Hansen
     * @author Oscar Maxwell
     */
    public int getLazer() {
        return lazer;
    }
}
