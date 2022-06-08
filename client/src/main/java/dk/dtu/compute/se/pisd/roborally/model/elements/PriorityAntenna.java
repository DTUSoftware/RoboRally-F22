package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * priorityantenna, that determines which robot starts
 *
 * @author Mads Legard Nielsen
 */
public class PriorityAntenna extends FieldElement {

    /**
     * cunstructer
     *
     * @param space give the space to where the field should be
     * @author Mads Legard Nielsen
     */
    public PriorityAntenna(Space space) {
        super(space);
    }
}
