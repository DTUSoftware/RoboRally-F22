package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * the reboottoken object for rebooting the robots
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class RebootToken extends FieldElement {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Creates a new reboot token.
     *
     * @param space     the space
     * @param direction the direction to put other players, and for players to face
     * @param x1        The x-coordinate for the first corner of the bounds
     * @param y1        The y-coordinate for the first corner of the bounds
     * @param x2        The x-coordinate for the second corner of the bounds
     * @param y2        The y-coordinate for the second corner of the bounds
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public RebootToken(Space space, Heading direction, int x1, int y1, int x2, int y2) {
        super(space);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * retruns x coordinate 1
     *
     * @return x1
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getx1() {
        return x1;
    }

    /**
     * returns y coordinate 1
     *
     * @return y1
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int gety1() {
        return y1;
    }

    /**
     * returns x coordinate 2
     *
     * @return x2
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getx2() {
        return x2;
    }

    /**
     * returns y coordinate 2
     *
     * @return y2
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int gety2() {
        return y2;
    }

    /**
     * Returns whether a given space is within the bounds of this reboot token.
     *
     * @param space the space to check whether it's in the bounds or not.
     * @return whether or not the space is in the bounds.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
}
