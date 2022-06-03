package dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.IGameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * action element class
 */
public abstract class ActionElement extends FieldElement implements Comparable {

    private IGameLogicController gameLogicController;

    /**
     * Constructer for action element
     * @param gameLogicController the game controller
     * @param space the space
     */
    public ActionElement(IGameLogicController gameLogicController, Space space) {
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
     * @return gamelogiccontroller
     */
    public IGameLogicController getGameLogicController() {
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
