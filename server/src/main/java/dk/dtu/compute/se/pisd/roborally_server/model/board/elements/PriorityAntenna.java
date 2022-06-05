package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

// TODO make this stuff
/**
 * priorityantenna, that determines which robot starts
 */
public class PriorityAntenna extends ActionElement {

    /**
     * cunstructer
     *
     * @param space give the space to where the field should be
     */
    public PriorityAntenna(GameController gameController, Space space) {
        super(gameController ,space);
        super.getGameController().board.setPriorityAntenna(space);
    }

    @Override
    public void activate() {

    }

    @Override
    public void doLandingAction() {

    }

}
