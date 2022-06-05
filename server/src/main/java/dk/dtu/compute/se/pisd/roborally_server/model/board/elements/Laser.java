package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import org.jetbrains.annotations.NotNull;

// TODO make this stuff

/**
 * The laser object
 */
public class Laser extends ActionElement {
    private Heading direction;
    private int lazer;

    /**
     * constructor for the laser
     *
     * @param gameLogicController the gamecontroller
     * @param space          the space to put the lazer
     * @param direction      the direction for the laser
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
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets the lazer number
     *
     * @return lazer number
     */
    public int getLazer() {
        return lazer;
    }

    /**
     * not used
     */

    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (player != null && !player.isMovedByAction()) {
            if (this.lazer == 1 || this.lazer == 4) {
                player.takeDamage();
            } else if (this.lazer == 2 || this.lazer == 5) {
                player.takeDamage();
                player.takeDamage();
            } else if (this.lazer == 3 || this.lazer == 6) {
                player.takeDamage();
                player.takeDamage();
                player.takeDamage();
            }
        }


    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }

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