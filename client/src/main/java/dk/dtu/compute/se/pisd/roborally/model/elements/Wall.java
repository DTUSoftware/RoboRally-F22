package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * the wall object
 *
 * @author Mads Legard Nielsen
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Wall extends FieldElement {
    boolean invisible;

    /**
     * constructer for the wall objct
     *
     * @param space     where its located
     * @param direction the direction of the wall
     * @param invisible whether a wall is visible or not
     * @author Mads Legard Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Wall(Space space, Heading direction, boolean invisible) {
        super(space, direction);
        this.invisible = invisible;
    }

    /**
     * gets if invisible
     *
     * @return invisible
     * @author Oscar Maxwell
     */
    public boolean getBooleanInvisible() {
        return invisible;
    }
}
