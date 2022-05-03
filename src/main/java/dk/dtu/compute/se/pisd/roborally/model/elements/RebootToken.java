package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class RebootToken extends FieldElement {
    private Heading direction;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Creates a new reboot token.
     *
     * @param space the space
     */
    public RebootToken(Space space, Heading direction, int x1, int y1, int x2, int y2) {
        super(space);
        this.direction = direction;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void doLandingAction() {

    }

    public Heading getDirection() {
        return direction;
    }

    public int getx1() {
        return x1;
    }

    public int gety1() {
        return y1;
    }

    public int getx2() {
        return x2;
    }

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
}
