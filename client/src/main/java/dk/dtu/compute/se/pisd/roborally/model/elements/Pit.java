package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

// TODO make this stuff

/**
 * the pit object that forces a reboot
 *
 * @author Mads Legard Nielsen
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Pit extends FieldElement {
    /**
     * Constructer for the Pit object
     *
     * @param space spacce
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Pit(Space space) {
        super(space);
    }
}
