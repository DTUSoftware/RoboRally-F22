package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
public class PushPanels extends ActionElement{
// TODO make this stuff
    /**
     * Constructer for action element
     *
     * @param gameController the game controller
     * @param space          the space
     */
    public PushPanels(GameController gameController, Space space, Heading direction) {
        super(gameController, space);
    }

    @Override
    public void activate() {

    }

    @Override
    public void doLandingAction() {

    }
}
