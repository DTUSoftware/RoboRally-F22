package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;


/**
 * priorityantenna, that determines which robot starts
 *
 * @author Mads Legard Nielsen
 */
public class PriorityAntenna extends ActionElement {

    /**
     * Constructer, that saves the priority antenna location on the game. board. The logic is inplemented at the
     * execute next step
     *
     * @param gameLogicController the game controller
     * @param space               give the space to where the field should be
     * @author Mads Legard Nielsen
     */
    public PriorityAntenna(GameLogicController gameLogicController, Space space) {
        super(gameLogicController, space);
        super.getGameController().getGame().getBoard().setPriorityAntenna(space);
    }

    /**
     * Not used
     */
    @Override
    public void activate() {

    }

    /**
     * Not used
     */
    @Override
    public void doLandingAction() {

    }

}
