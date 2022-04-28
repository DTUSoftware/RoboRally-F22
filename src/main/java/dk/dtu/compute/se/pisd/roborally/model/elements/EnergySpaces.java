package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class EnergySpaces extends ActionElement {
    boolean hasEnergy = true;

    public EnergySpaces(GameController gameController, Space space) {
        super(gameController, space);

    }

    @Override
    public void doLandingAction() {

    }

    @Override
    public void activate() {
        Player player = super.getSpace().getPlayer();
        if (hasEnergy) {player.addPower(1);}


    }
}
