package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * The energyspace object that gives the player energy
 */
public class EnergySpace extends ActionElement {
    boolean hasEnergy = true;

    /**
     * The constructer for the energyspace
     * @param gameController the gamecontroller
     * @param space the place t put the energyspace
     */
    public EnergySpace(GameController gameController, Space space) {
        super(gameController, space);

    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * gives the palyer energy
     */
    @Override
    public void activate() {
        // TODO make the thingy with the specific register reached
        if (!super.getSpace().free()) {
            Player player = super.getSpace().getPlayer();
            if (hasEnergy) {
                player.addPower(1);
                hasEnergy = false;
            }
        }
    }
}
