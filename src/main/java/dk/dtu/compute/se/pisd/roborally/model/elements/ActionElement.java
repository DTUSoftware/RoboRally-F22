package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public abstract class ActionElement extends FieldElement {
    private GameController gameController;

    public ActionElement(GameController gameController, Space space) {
        super(space);
        this.gameController = gameController;
        this.gameController.addElement(this);
    }

    public GameController getGameController() {
        return gameController;
    }
}
