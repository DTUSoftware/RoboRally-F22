package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends ActionElement {
    private Heading direction;
    private int register1;
    private int register2;

// TODO change push panels so it takes in the register

//Function that pulls the current step from Board. Should be the register.
public Board board;
//    int step = board.getStep();

    /**
     * Constructer for action element
     *
     * @param gameController the game controller
     * @param space          the space
     * @param direction the direction
     * @param register1 register 1
     * @param register2 register 2
     */
    public PushPanel(GameController gameController, Space space, Heading direction, int register1, int register2) {

        super(gameController, space);

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

    public int getRegister1() {
        return register1;
    }

    public int getRegister2() {
        return register2;
    }

    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null){
            super.getGameController().moveDirectionX(player, direction, 1);
        }

    }

    @Override
    public void doLandingAction() {

    }
}
