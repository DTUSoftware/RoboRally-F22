package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

// TODO make this stuff

/**
 * The laser object
 * @author Oscar Maxwell
 * @author Nicolai Udbye
 * @author Mads Hansen
 */
public class Laser extends ActionElement {
    private Heading direction;
    private int lazer;

    /**
     * constructor for the laser
     *
     * @param gameLogicController the gamecontroller
     * @param space               the space to put the lazer
     * @param direction           the direction for the laser
     * @param lazer               the number of the lazer type
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @author Mads Hansen
     */
    public Laser(GameLogicController gameLogicController, Space space, Heading direction, int lazer) {
        super(gameLogicController, space);
        this.direction = direction;
        this.lazer = lazer;
    }

    /**
     * gets the heading
     *
     * @return direction
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @author Mads Hansen
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets the lazer number
     *
     * @return lazer number
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @author Mads Hansen
     */
    public int getLazer() {
        return lazer;
    }

    /**
     * activates the laser
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @author Mads Hansen
     */
    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null && !player.isMovedByAction()) {
            if (this.lazer == 1 || this.lazer == 4) {
                player.getDeck().takeDamage();
            } else if (this.lazer == 2 || this.lazer == 5) {
                player.getDeck().takeDamage();
                player.getDeck().takeDamage();
            } else if (this.lazer == 3 || this.lazer == 6) {
                player.getDeck().takeDamage();
                player.getDeck().takeDamage();
                player.getDeck().takeDamage();
            }
        }


    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

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

        if (o instanceof ConveyorBelt || o instanceof PushPanel || o instanceof Gear) {
            return 1;
        } else if (o instanceof EnergySpace || o instanceof Checkpoint) {
            return -1;
        }
        return -1;
    }
}