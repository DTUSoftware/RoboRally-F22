package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * the wall object
 */
public class Wall extends FieldElement {
    Heading direction;

    /**
     * constructer for the wall objct
     * @param space where its located
     * @param direction the direction of the wall
     */
    public Wall(Space space, Heading direction) {
        super(space);
        this.direction = direction;
    }

    /**
     * gets the heading
     * @return directioon
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * landingaction (not used)
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * activate not used
     */
    @Override
    public void activate() {

    }
}
