package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
// TODO make this stuff
public class Pit extends ActionElement{
    public Pit(GameController gameController, Space space){
        super(gameController,space);

    }

    @Override
    public void doLandingAction() {
        if (!getSpace().free()) {
            Player player = getSpace().getPlayer();

            // give bad card
            player.damage();

            // reboot the player
            player.reboot();
        }

    }

    @Override
    public void activate() {

    }
}
