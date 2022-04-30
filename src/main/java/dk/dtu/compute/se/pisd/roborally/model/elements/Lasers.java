package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
// TODO make this stuff
public class Lasers extends ActionElement{

    public Lasers(GameController gameController, Space space, Heading direction) {
        super(gameController, space);
    }

    @Override
    public void activate() {

    }

    @Override
    public void doLandingAction() {

    }
}
