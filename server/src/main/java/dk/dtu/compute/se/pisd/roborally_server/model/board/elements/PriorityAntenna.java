package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;

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
    public PriorityAntenna(GameLogicController gameLogicController, Space space) {
        super(gameLogicController,space);
        super.getGameController().getGame().getBoard().setPriorityAntenna(space);
    }

    @Override
    public void activate() {

    }

    @Override
    public void doLandingAction() {

    }

}
