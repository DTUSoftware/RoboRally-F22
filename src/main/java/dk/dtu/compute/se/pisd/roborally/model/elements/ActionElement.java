package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * action element class
 */
public abstract class ActionElement extends FieldElement {

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
     * game controller getterclass
     * @return gamecontroller
     */
    public GameController getGameController() {
        return gameController;
    }
}
