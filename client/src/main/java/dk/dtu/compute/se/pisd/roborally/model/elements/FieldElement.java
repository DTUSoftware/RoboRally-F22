package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * abstract clase field element for making the field obejcts
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 * @author Mads Legard Nielsen
 */
public abstract class FieldElement {
    private Space space;
    private Heading direction;

    /**
     * cunstructer
     *
     * @param space give the space to where the field should be
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Legard Nielsen
     */
    public FieldElement(Space space) {
        this.space = space;
        this.space.addFieldObject(this);
        this.direction = Heading.NORTH;
    }

    /**
     * cunstructer
     *
     * @param space     give the space to where the field should be
     * @param direction the direction of the fieldElement
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Legard Nielsen
     */
    public FieldElement(Space space, Heading direction) {
        this(space);
        this.direction = direction;
    }

    /**
     * gets the space the field is on
     *
     * @return space where the field is on
     * @author Mads Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Space getSpace() {
        return space;
    }

    /**
     * getter for direction - NOT EVERY FIELD WILL HAVE A DIRECTION!
     *
     * @return spawnDirection which is the direction a wall or pushpanel should spawn
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Heading getDirection() {
        return this.direction;
    }
}
