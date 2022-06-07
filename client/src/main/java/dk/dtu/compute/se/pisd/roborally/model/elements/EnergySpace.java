package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * The energyspace object that gives the player energy
 *
 * @author Mads Legard Nielsen
 */
public class EnergySpace extends FieldElement {
    boolean hasEnergy = true;

    /**
     * The constructer for the energyspace
     *
     * @param space the place t put the energyspace
     * @author Mads Legard Nielsen
     */
    public EnergySpace(Space space) {
        super(space);

    }
}
