package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
// TODO make this stuff

/**
 * The laser object
 */
public class Laser extends ActionElement{
    private Heading direction;

    /**
     * constructor for the laser
     * @param gameController the gamecontroller
     * @param space the space to put the lazer
     * @param direction the direction for the laser
     */
    public Laser(GameController gameController, Space space, Heading direction) {
        super(gameController, space);
        this.direction = direction;
    }

    /**
     * gets the heading
     * @return direction
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * not used
     */
    @Override
    public void activate() {

    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }
}
