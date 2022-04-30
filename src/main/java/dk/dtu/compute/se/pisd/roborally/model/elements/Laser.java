package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
// TODO make this stuff
public class Laser extends ActionElement{
    private Heading direction;

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

    @Override
    public void activate() {

    }

    @Override
    public void doLandingAction() {

    }
}
