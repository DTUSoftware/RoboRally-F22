package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gear extends ActionElement {
    private boolean direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameController
     * @param space
     * @param direction
     */
    public Gear(GameController gameController, Space space, boolean direction) {
        super(gameController, space);
        this.direction = direction;
    }

    public boolean getDirection() {
        return direction;
    }

    @Override
    public void doLandingAction() {

    }

    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null) {
            // if right
            if (direction) {
                super.getGameController().turnRight(player);
            }
            // if left
            else {
                super.getGameController().turnLeft(player);
            }
        }

    }
}
