package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * action element class
 */
public abstract class ActionElement extends FieldElement implements Comparable {

    private GameLogicController gameLogicController;

    /**
     * Constructer for action element
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
     */
    public abstract void activate();

    /**
     * game controller getterclass
     * @return gamecontroller
     */
    public GameLogicController getGameController() {
        return gameLogicController;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        return 1;
    }
}
