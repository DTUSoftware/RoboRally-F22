package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * The rotating gear object
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 * @author Mads G. E. Hansen
 */
public class Gear extends ActionElement {
    private boolean direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameLogicController the gamecontroller
     * @param space               where to put the gear
     * @param direction           the direction of the gear
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Gear(GameLogicController gameLogicController, Space space, boolean direction) {
        super(gameLogicController, space);
        this.direction = direction;
    }

    /**
     * getter for the direction of the gear
     *
     * @return direction
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null && !player.isMovedByAction()) {
            // if right
            if (direction) {
                super.getGameController().turnRight(player);
            }
            // if left
            else {
                super.getGameController().turnLeft(player);
            }
            player.setMovedByAction(true);
        }

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

        if (o instanceof ConveyorBelt || o instanceof PushPanel) {
            return 1;
        } else if (o instanceof Laser || o instanceof EnergySpace) {
            return -1;
        }
        return -1;
    }
}
