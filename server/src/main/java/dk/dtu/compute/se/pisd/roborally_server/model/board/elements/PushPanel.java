package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;


/**
 * the pushpanel object that pushes player on certain registers
 */
public class PushPanel extends ActionElement {
    private Heading direction;
    private int register1;
    private int register2;

// TODO change push panels so it takes in the register

//Function that pulls the current step from Board. Should be the register.
    /**
     * the board.
     */
    public Board board;
//    int step = board.getStep();

    /**
     * Constructer for action element
     *
     * @param gameLogicController the game controller
     * @param space          the space
     * @param direction the direction
     * @param register1 register 1
     * @param register2 register 2
     */
    public PushPanel(GameLogicController gameLogicController, Space space, Heading direction, int register1, int register2) {

        super(gameLogicController, space);

        this.direction = direction;
        this.register1 = register1;
        this.register2 = register2;


    }

    /**
     * gets the heading
     *
     * @return direction
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * the first register where the pushpanel reacts
     * @return register1
     */
    public int getRegister1() {
        return register1;
    }

    /**
     * the second register where pushpanel reacts
     * @return register2
     */
    public int getRegister2() {
        return register2;
    }

    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null && !player.isMovedByAction()){
            if (getRegister1() == super.getGameController().getGame().getGameState().getStep()|| getRegister2() == super.getGameController().getGame().getGameState().getStep()) {
                super.getGameController().moveDirectionX(player, direction.next().next(), 1);
                player.setMovedByAction(true);
            }
        }

    }

    @Override
    public void doLandingAction() {

    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        if (o instanceof ConveyorBelt) {
            return 1;
        }
        else if (o instanceof Gear || o instanceof Laser || o instanceof EnergySpace || o instanceof Checkpoint) {
            return -1;
        }
        return -1;
    }
}
