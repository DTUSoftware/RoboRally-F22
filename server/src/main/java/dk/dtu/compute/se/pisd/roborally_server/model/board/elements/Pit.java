package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

// TODO make this stuff

/**
 * the pit object that forces a reboot
 */
public class Pit extends ActionElement{
    /**
     * Constructer for the Pit object
     * @param gameLogicController gamecontroller
     * @param space spacce
     */
    public Pit(GameLogicController gameLogicController, Space space){
        super(gameLogicController,space);

    }

    /**
     * does takeDamage to the player and reboots the player
     */
    @Override
    public void doLandingAction() {
        if (!getSpace().free()) {
            Player player = getSpace().getPlayer();

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

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        if (o instanceof ConveyorBelt || o instanceof PushPanel || o instanceof Gear || o instanceof Laser || o instanceof EnergySpace || o instanceof Checkpoint) {
            return 1;
        }
        return -1;
    }
}
