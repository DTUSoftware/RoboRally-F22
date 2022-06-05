package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * The energyspace object that gives the player energy
 */
public class EnergySpace extends FieldElement {
    boolean hasEnergy = true;

    /**
     * The constructer for the energyspace
     * @param space the place t put the energyspace
     */
    public EnergySpace(Space space) {
        super(space);

    }
}
