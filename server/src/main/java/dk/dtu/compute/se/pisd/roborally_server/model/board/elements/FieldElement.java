package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;

/**
 * abstract clase field element for making the field obejcts
 * @author Marcus Sand
 * @author mads nielsen
 */
public abstract class FieldElement {
    private Space space;

    /**
     * cunstructer
     * @author Marcus Sand
     * @author Mads Legard Nielsen
     * @param space give the space to where the field should be
     */
    public FieldElement(Space space) {
        this.space = space;
        this.space.addFieldObject(this);
    }

    /**
     * landing action method
     * @author Marcus sand
     * @author Mads Legard Nielsen
     */
    public abstract void doLandingAction();

    /**
     * gets the space the field is on
     * @author Mads Nielsen
     * @author Marcus Sand
     * @return space where the field is on
     */
    public Space getSpace() {
        return space;
    }
}
