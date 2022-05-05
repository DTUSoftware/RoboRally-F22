package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
// TODO make this stuff

/**
 * the pit object that forces a reboot
 */
public class Pit extends ActionElement{
    /**
     * Constructer for the Pit object
     * @param gameController gamecontroller
     * @param space spacce
     */
    public Pit(GameController gameController, Space space){
        super(gameController,space);

    }

    /**
     * does damage to the player and reboots the player
     */
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

    /**
     * not used
     */
    @Override
    public void activate() {

    }
}
