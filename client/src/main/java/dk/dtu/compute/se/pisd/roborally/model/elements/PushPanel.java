package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * the pushpanel object that pushes player on certain registers
 */
public class PushPanel extends FieldElement {
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
     * @param space          the space
     * @param direction the direction
     * @param register1 register 1
     * @param register2 register 2
     */
    public PushPanel(Space space, Heading direction, int register1, int register2) {
        super(space, direction);
        this.register1 = register1;
        this.register2 = register2;
    }

    /**
     * the first register where the pushpanel reacts
     * @return register1
     */
    public int getRegister1() {
        return register1;
    }

    /**
     * the scond register where pushpanel reacts
     * @return register2
     */
    public int getRegister2() {
        return register2;
    }
}
