package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;


/**
 * the pit object that forces a reboot
 *
 * @author Mads Legard Nielsen
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Pit extends ActionElement {
    /**
     * Constructer for the Pit object
     *
     * @param gameLogicController gamecontroller
     * @param space               spacce
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Pit(GameLogicController gameLogicController, Space space) {
        super(gameLogicController, space);

    }

    /**
     * does takeDamage to the player and reboots the player
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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


    /**
     * for the activation order
     *
     * @param o object to compare to.
     * @return integer that says the relation to the object -1 0 or 1, which is the order.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
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
