package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
public class PushPanel extends ActionElement{
// TODO make this stuff
    /**
     * Constructer for action element
     *
     * @param gameController the game controller
     * @param space          the space
     */
    public PushPanel(GameController gameController, Space space, Heading direction) {

        super(gameController, space);
        Wall wallOn = new Wall(space,direction);
    }

    @Override
    public void activate() {

    }

    @Override
    public void doLandingAction() {

    }
}
