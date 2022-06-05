package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * SpawnGear class where players spawn
 */
public class SpawnGear extends SpawnableElement {

    /**
     * Creates a new spawn gear.
     *
     * @param gameLogicController the gamecontroller
     * @param space the space
     * @param spawnDirection the direction that the robot should face
     */
    public SpawnGear(GameLogicController gameLogicController, Space space, Heading spawnDirection) {
        super(gameLogicController, space, spawnDirection);
    }

    /**
     * not used
     */
    @Override
    public void doLandingAction() {

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
