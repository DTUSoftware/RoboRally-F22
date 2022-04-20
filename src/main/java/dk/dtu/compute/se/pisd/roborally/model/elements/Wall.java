package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Wall extends FieldElement {
    Heading direction;

    public Wall(Space space, Heading direction) {
        super(space);
        this.direction = direction;
    }

    public Heading getDirection() {
        return direction;
    }

    @Override
    public void doLandingAction() {

    }

    @Override
    public void activate() {

    }
}
