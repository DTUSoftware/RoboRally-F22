package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;

/**
 * the wall object
 * @author Mads Legard Nielsen
 * @author Marcus Sand
 */
public class Wall extends FieldElement {
    Heading direction;
    boolean invisible;

    /**
     * constructer for the wall objct
     * @author Mads Legard Nielsen
     * @author Marcus Sand
     * @param space where its located
     * @param direction the direction of the wall
     * @param invisible whether a wall is visible or not
     */
    public Wall(Space space, Heading direction, boolean invisible) {
        super(space);
        this.direction = direction;
        this.invisible = invisible;
    }


    /**
     * gets the heading
     * @author Mads Legard Nielsen
     * @author Marcus Sand
     * @return directioon
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets if invisible
     * @author Oscar Maxwell
     * @return invisible
     */

    public boolean getBooleanInvisible() {return invisible;}

    /**
     * landingaction (not used)
     */
    @Override
    public void doLandingAction() {

    }
}
