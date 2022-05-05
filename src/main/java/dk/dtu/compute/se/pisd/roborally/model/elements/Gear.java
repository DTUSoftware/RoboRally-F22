package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * The rotating gear object
 */
public class Gear extends ActionElement {
    private boolean direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameController the gamecontroller
     * @param space where to put the gear
     * @param direction the direction of the gear
     */
    public Gear(GameController gameController, Space space, boolean direction) {
        super(gameController, space);
        this.direction = direction;
    }

    /**
     * getter for the direction of the gear
     * @return direction
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * activates the gear and rotates the player.
     */
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
