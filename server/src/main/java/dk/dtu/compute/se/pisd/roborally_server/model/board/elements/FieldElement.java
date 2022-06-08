package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;

/**
 * abstract clase field element for making the field obejcts
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 * @author mads nielsen
 */
public abstract class FieldElement {
    private Space space;

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
    }

    /**
     * landing action method
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Legard Nielsen
     */
    public abstract void doLandingAction();

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
}
