package dk.dtu.compute.se.pisd.roborally.view.elements.elements;

import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * abstract clase field element for making the field obejcts
 */
public abstract class FieldElement {
    private Space space;

    /**
     * cunstructer
     * @param space give the space to where the field should be
     */
    public FieldElement(Space space) {
        this.space = space;
        this.space.addFieldObject(this);
    }

    /**
     * landing action method
     */
    public abstract void doLandingAction();

    /**
     * gets the space the field is on
     * @return space where the field is on
     */
    public Space getSpace() {
        return space;
    }
}
