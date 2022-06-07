package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * The energyspace object that gives the player energy
 *
 * @author Mads Legard Nielsen
 */
public class EnergySpace extends ActionElement {
    boolean hasEnergy = true;

    /**
     * The constructer for the energyspace
     *
     * @param gameLogicController the gamecontroller
     * @param space               the place t put the energyspace
     * @author Mads Legard Nielsen
     */
    public EnergySpace(GameLogicController gameLogicController, Space space) {
        super(gameLogicController, space);

    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

    }

    /**
     * gives the player energy if the is the first one to land on the unused energy field, and does the register 5
     * rule where it adds energy to the player if it's landed on during the 5th register
     *
     * @author Mads Legard Nielsen
     */
    @Override
    public void activate() {
        if (!super.getSpace().free()) {
            Player player = super.getSpace().getPlayer();
            if (hasEnergy) {
                player.getDeck().addEnergy(1);
                hasEnergy = false;
            } else if (super.getGameController().getGame().getGameState().getStep() == 5) {
                player.getDeck().addEnergy(1);
            }
        }
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

        if (o instanceof ConveyorBelt || o instanceof PushPanel || o instanceof Gear || o instanceof Laser) {
            return 1;
        } else if (o instanceof Checkpoint) {
            return -1;
        }
        return -1;
    }
}
