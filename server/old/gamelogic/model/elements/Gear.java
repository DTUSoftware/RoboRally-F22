package dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.IGameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * The rotating gear object
 */
public class Gear extends ActionElement {
    private boolean direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameLogicController the gamecontroller
     * @param space where to put the gear
     * @param direction the direction of the gear
     */
    public Gear(IGameLogicController gameLogicController, Space space, boolean direction) {
        super(gameLogicController, space);
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
        if (player != null && !player.isMovedByAction()) {
            // if right
            if (direction) {
                super.getGameLogicController().turnRight(player);
            }
            // if left
            else {
                super.getGameLogicController().turnLeft(player);
            }
            player.setMovedByAction(true);
        }

    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        if (o instanceof ConveyorBelt || o instanceof PushPanel) {
            return 1;
        }
        else if (o instanceof Laser || o instanceof EnergySpace) {
            return -1;
        }
        return -1;
    }
}