package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class EnergySpace extends ActionElement {
    boolean hasEnergy = true;

    public EnergySpace(GameController gameController, Space space) {
        super(gameController, space);

    }

    @Override
    public void doLandingAction() {

    }

    @Override
    public void activate() {
        // TODO make the thingy with the specific register reached
        Player player = super.getSpace().getPlayer();
        if (hasEnergy) {player.addPower(1);}


    }
}
