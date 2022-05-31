package dk.dtu.compute.se.pisd.roborally_server.model.elements;

import dk.dtu.compute.se.pisd.roborally_server.controller.GameController;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.Space;
import org.jetbrains.annotations.NotNull;

// TODO make this stuff

/**
 * The laser object
 */
public class Laser extends ActionElement{
    private Heading direction;
    private int lazer;

    /**
     * constructor for the laser
     * @param gameController the gamecontroller
     * @param space the space to put the lazer
     * @param direction the direction for the laser
     */
    public Laser(GameController gameController, Space space, Heading direction, int lazer) {
        super(gameController, space);
        this.direction = direction;
        this.lazer = lazer;
    }

    /**
     * gets the heading
     * @return direction
     */
    public Heading getDirection() {
        return direction;
    }

    /**
     * gets the lazer number
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
        }
        else if (o instanceof EnergySpace || o instanceof Checkpoint) {
            return -1;
        }
        return -1;
    }
}
