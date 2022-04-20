package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public abstract class FieldElement {
    private Space space;

    public FieldElement(Space space) {
        this.space = space;
        this.space.addFieldObject(this);
    }

    public abstract void doLandingAction();
    public abstract void activate();

    public Space getSpace() {
        return space;
    }
}
