package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * action element class
 *
 * @author Mads Nielsen
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public abstract class ActionElement extends FieldElement implements Comparable {

    private GameLogicController gameLogicController;

    /**
     * Constructer for action element
     *
     * @param gameLogicController the game controller
     * @param space               the space
     * @author Mads Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ActionElement(GameLogicController gameLogicController, Space space) {
        super(space);
        this.gameLogicController = gameLogicController;
        this.gameLogicController.addElement(this);
    }

    /**
     * activate method
     *
     * @author Mads Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public abstract void activate();

    /**
     * game controller getterclass
     *
     * @return gamecontroller
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     */
    public GameLogicController getGameController() {
        return gameLogicController;
    }

    /**
     * for the activation order
     *
     * @param o object to compare to.
     * @return integer that says the relation to the object -1 0 or 1, which is the order.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        return 1;
    }
}
