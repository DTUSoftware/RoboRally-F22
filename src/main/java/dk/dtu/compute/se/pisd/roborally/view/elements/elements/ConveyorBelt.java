package dk.dtu.compute.se.pisd.roborally.view.elements.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * converyorbelt object
 */
public class ConveyorBelt extends ActionElement {
    private boolean color;
    private Heading direction;

    /**
     * Creates a new conveyor belt.
     *
     * @param gameController
     * @param space
     * @param color
     */
    public ConveyorBelt(GameController gameController, Space space, boolean color, Heading direction) {
        super(gameController, space);
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
        if (player != null) {
            // if blue
            if (color) {
                super.getGameController().moveDirectionX(player, direction, 2);
            }
            // if green
            else {
                super.getGameController().moveDirectionX(player, direction, 1);
            }
        }
    }
}
