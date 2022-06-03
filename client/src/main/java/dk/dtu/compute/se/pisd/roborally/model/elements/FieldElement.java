package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * abstract clase field element for making the field obejcts
 */
public abstract class FieldElement {
    private Space space;
    private Heading direction;

    /**
     * cunstructer
     * @param space give the space to where the field should be
     */
    public FieldElement(Space space) {
        this.space = space;
        this.space.addFieldObject(this);
        this.direction = Heading.NORTH;
    }

    /**
     * cunstructer
     * @param space give the space to where the field should be
     */
    public FieldElement(Space space, Heading direction) {
        this(space);
        this.direction = direction;
    }

    /**
     * gets the space the field is on
     * @return space where the field is on
     */
    public Space getSpace() {
        return space;
    }

    /**
     * getter for direction - NOT EVERY FIELD WILL HAVE A DIRECTION!
     * @return spawnDirection which is the direction a wall or pushpanel should spawn
     */
    public Heading getDirection() {
        return this.direction;
    }
}
