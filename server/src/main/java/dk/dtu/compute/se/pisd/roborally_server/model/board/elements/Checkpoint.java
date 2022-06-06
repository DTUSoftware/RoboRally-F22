package dk.dtu.compute.se.pisd.roborally_server.model.board.elements;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import org.jetbrains.annotations.NotNull;

/**
 * checkpoint class, inherits from FieldElement
 *
 * @author Mads Legard Nielsen
 */
public class Checkpoint extends ActionElement {
    /**
     * the number of the checkpoint
     */
    int number;
    //count every time a checkpoint is created
    static int numberOfCheckpointsCreated = 0;

    /**
     * constructer of the checkpoint class
     *
     * @param gamecontroller the gamecontroller
     * @param space          takes the space the checkpoint is on
     * @param number         the number that the checkpoint needs to be
     * @author Mads Legard Nielsen
     */
    public Checkpoint(GameLogicController gamecontroller, Space space, int number) {
        super(gamecontroller, space);
        numberOfCheckpointsCreated++;
        this.number = number;
    }

    /**
     * the number of checkpoints to go through
     *
     * @param checkpoints the amount of checkpoints
     * @author Mads Legard Nielsen
     */
    public static void setNumberOfCheckpointsCreated(int checkpoints) {
        numberOfCheckpointsCreated = checkpoints;
    }

    /**
     * sets the number which the checkpoint is
     * @param number the number the checkpoint is
     * @author Mads Legard Nielsen
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * gets the number which the checkpoint is
     * @author Mads Legard Nielsen
     * @return checkpoint number
     */
    public int getNumber() {
        return number;
    }

    /**
     * checks if the checkpoint is reached in the correct order
     * @author Mads Legard Nielsen
     * @param checkpointReached what checkpoint has been reached
     * @return True if it is, False if not
     */
    public boolean checkCheckpoint(int checkpointReached) {
        return checkpointReached == getNumber() - 1;
    }

    /**
     * checks if all the checkpoints are reached
     * @author Mads Legard Nielsen
     * @param checkpointsReached the number of checkpoints reached
     * @return true if yes false if no
     */
    public boolean allCheckpointsReached(int checkpointsReached) {
        return checkpointsReached == numberOfCheckpointsCreated;
    }

    /**
     * does the landing action for the checkpoint card, makes sure that the player that landed on the checkpoint
     * has the previous checkpoint, and checks if all checkpoints are reached for that player, where in that case the player wins.
     * @author Mads Legard Nielsen
     */
    @Override
    public void doLandingAction() {
        if (checkCheckpoint(super.getSpace().getPlayer().getCurrentCheckpoint())) {
            super.getSpace().getPlayer().setCurrentCheckpoint(getNumber());
            if (allCheckpointsReached(super.getSpace().getPlayer().getCurrentCheckpoint())) {
                super.getGameController().winTheGame(super.getSpace().getPlayer());
                //Use the button feature from left right, then find out how to quit / restart the game from scratch.
                //brug evt. new game function for restart.
                // Brug Wincondition nederst i Gamecontroller

            }
        }
    }

    /**
     * not used here
     */
    @Override
    public void activate() {

    }

    /**
     * for the activation order
     * @author Marcus Sand
     * @param o object to compare to.
     * @return integer that says the relation to the object -1 0 or 1, which is the order.
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
