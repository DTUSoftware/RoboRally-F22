package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * action element class
 */
public abstract class ActionElement extends FieldElement implements Comparable {

    private GameController gameController;

    /**
     * Constructer for action element
     * @param gameController the game controller
     * @param space the space
     */
    public ActionElement(GameController gameController, Space space) {
        super(space);
        this.gameController = gameController;
        this.gameController.addElement(this);
    }

    /**
     * activate method
     */
    public abstract void activate();

    /**
     * game controller getterclass
     * @return gamecontroller
     */
    public GameController getGameController() {
        return gameController;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        return 1;
    }
}
