package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

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
