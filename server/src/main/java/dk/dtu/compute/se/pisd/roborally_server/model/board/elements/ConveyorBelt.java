package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * converyorbelt object
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class ConveyorBelt extends ActionElement {
    private boolean color;
    private Heading direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameLogicController the gamecontroller
     * @param space               the space to put the conveyorbelt
     * @param color               the color of the belt blue/green is true/false
     * @param direction           the direction for the conveyorbelt
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ConveyorBelt(GameLogicController gameLogicController, Space space, boolean color, Heading direction) {
        super(gameLogicController, space);
        this.color = color;
        this.direction = direction;
    }

    /**
     * gets the color
     *
     * @return color
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean getColor() {
        return color;
    }

    /**
     * gets the heading
     *
     * @return direction
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * activates converyorbelt
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null && !player.isMovedByAction()) {
            // if blue
            if (color) {
                super.getGameController().moveDirectionX(player, direction, 2);
            }
            // if green
            else {
                super.getGameController().moveDirectionX(player, direction, 1);
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

        if (o instanceof ConveyorBelt) {
            if (this.color) {
                return -1;
            } else {
                return 1;
            }
        }
        return -1;
    }
}
