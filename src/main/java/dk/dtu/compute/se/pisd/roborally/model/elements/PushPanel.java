package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends ActionElement {
    private Heading direction;

// TODO change push panels so it takes in the register

    /**
     * Constructer for action element
     *
     * @param gameController the game controller
     * @param space          the space
     */
    //Function that pulls the current step from Board. Should be the register.
    public Board board;
    int step = board.getStep();

    public PushPanel(GameController gameController, Space space, Heading direction) {

        super(gameController, space);

        this.direction = direction;
        Wall wallOn = new Wall(space, direction.next().next());

    }

    /**
     * gets the heading
     *
     * @return direction
     */
    public Heading getDirection() {
        return direction;
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
