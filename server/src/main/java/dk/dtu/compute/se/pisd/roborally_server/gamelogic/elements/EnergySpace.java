package dk.dtu.compute.se.pisd.roborally_server.gamelogic.elements;

import dk.dtu.compute.se.pisd.roborally_server.controller.GameController;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.IGameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.Player;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.Space;
import org.jetbrains.annotations.NotNull;

/**
 * The energyspace object that gives the player energy
 */
public class EnergySpace extends ActionElement {
    boolean hasEnergy = true;

    /**
     * The constructer for the energyspace
     * @param gameLogicController the gamecontroller
     * @param space the place t put the energyspace
     */
    public EnergySpace(IGameLogicController gameLogicController, Space space) {
        super(gameLogicController, space);

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

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof ActionElement)) {
            throw new ClassCastException();
        }

        if (o instanceof ConveyorBelt || o instanceof PushPanel || o instanceof Gear || o instanceof Laser) {
            return 1;
        }
        else if (o instanceof Checkpoint) {
            return -1;
        }
        return -1;
    }
}
