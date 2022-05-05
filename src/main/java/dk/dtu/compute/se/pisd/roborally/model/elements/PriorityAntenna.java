package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Space;

// TODO make this stuff

/**
 * priorityantenna, that determines which robot starts
 */
public class PriorityAntenna extends FieldElement{

    /**
     * cunstructer
     *
     * @param space give the space to where the field should be
     */
    public PriorityAntenna(Space space) {
        super(space);
    }

    @Override
    public void doLandingAction() {

    }
}
