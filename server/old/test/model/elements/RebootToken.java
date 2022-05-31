package dk.dtu.compute.se.pisd.roborally_server.model.elements;

import dk.dtu.compute.se.pisd.roborally_server.controller.GameController;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * the reboottoken object for rebooting the robots
 */
public class RebootToken extends SpawnableElement {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Creates a new reboot token.
     *
     * @param gameController the gamecontroller
     * @param space the space
     * @param direction the direction to put other players, and for players to face
     * @param x1 The x-coordinate for the first corner of the bounds
     * @param y1 The y-coordinate for the first corner of the bounds
     * @param x2 The x-coordinate for the second corner of the bounds
     * @param y2 The y-coordinate for the second corner of the bounds
     */
    public RebootToken(GameController gameController, Space space, Heading direction, int x1, int y1, int x2, int y2) {
        super(gameController, space, direction);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * retruns x coordinate 1
     * @return x1
     */
    public int getx1() {
        return x1;
    }

    /**
     * returns y coordinate 1
     * @return y1
     */
    public int gety1() {
        return y1;
    }

    /**
     * returns x coordinate 2
     * @return x2
     */
    public int getx2() {
        return x2;
    }

    /**
     * returns y coordinate 2
     * @return y2
     */
    public int gety2() {
        return y2;
    }

    /**
     * Returns whether a given space is within the bounds of this reboot token.
     *
     * @param space the space to check whether it's in the bounds or not.
     * @return whether or not the space is in the bounds.
     */
    public boolean isWithinBounds(Space space) {
        return (
                        (
                                ((this.x1 <= this.x2) && (space.x >= this.x1 && space.x <= this.x2))
                                ||
                                ((this.x1 > this.x2) && (space.x < this.x1 && space.x >= this.x2))
                        )
                        &&
                        (
                                ((this.y1 <= this.y2) && (space.y >= this.y1 && space.y <= this.y2))
                                ||
                                ((this.y1 > this.y2) && (space.y < this.y1 && space.y >= this.y2))
                        )
                );
    }

    /**
     * not used
     */
    @Override
    public void activate() {

    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        if (o instanceof ConveyorBelt || o instanceof PushPanel || o instanceof Gear || o instanceof Laser || o instanceof EnergySpace || o instanceof Checkpoint) {
            return 1;
        }
        return -1;
    }
}
