package dk.dtu.compute.se.pisd.roborally_server.gamelogic.elements;

import dk.dtu.compute.se.pisd.roborally_server.controller.GameController;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.*;
import org.jetbrains.annotations.NotNull;

/**
 * converyorbelt object
 */
public class ConveyorBelt extends ActionElement {
    private boolean color;
    private Heading direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameLogicController the gamecontroller
     * @param space the space to put the conveyorbelt
     * @param color the color of the belt blue/green is true/false
     * @param direction the direction for the conveyorbelt
     */
    public ConveyorBelt(IGameLogicController gameLogicController, Space space, boolean color, Heading direction) {
        super(gameLogicController, space);
        this.color = color;
        this.direction = direction;
    }

    /**
     * gets the color
     * @return color
     */
    public boolean getColor() {
        return color;
    }

    /**
     * gets the heading
     * @return direction
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * do the landing action (not used)
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * activates onveryorbelt
     */
    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null && !player.isMovedByAction()) {
            // if blue
            if (color) {
                super.getGameLogicController().moveDirectionX(player, direction, 2);
            }
            // if green
            else {
                super.getGameLogicController().moveDirectionX(player, direction, 1);
            }
            player.setMovedByAction(true);
        }
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        if (o instanceof ConveyorBelt) {
            if (this.color) {
                return -1;
            }
            else {
                return 1;
            }
        }
        return -1;
    }
}
