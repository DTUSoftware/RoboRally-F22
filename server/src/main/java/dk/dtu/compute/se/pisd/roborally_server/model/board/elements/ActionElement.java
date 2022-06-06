package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * action element class
 * @author Mads Nielsen
 * @author Marcus Sand
 */
public abstract class ActionElement extends FieldElement implements Comparable {

    private GameLogicController gameLogicController;

    /**
     * Constructer for action element
     * @author Mads Nielsen
     * @author Marcus Sand
     * @param gameLogicController the game controller
     * @param space the space
     */
    public ActionElement(GameLogicController gameLogicController, Space space) {
        super(space);
        this.gameLogicController = gameLogicController;
        this.gameLogicController.addElement(this);
    }

    /**
     * activate method
     * @author Mads Nielsen
     * @author Marcus Sand
     */
    public abstract void activate();

    /**
     * game controller getterclass
     * @author Marcus Sand
     * @author Mads Nielsen
     * @return gamecontroller
     */
    public GameLogicController getGameController() {
        return gameLogicController;
    }
    /**
     * for the activation order
     * @author Marcus Sand
     * @param o object to compare to.
     * @return integer that says the relation to the object -1 0 or 1, which is the order.
     */
    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        return 1;
    }
}
