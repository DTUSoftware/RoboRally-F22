package dk.dtu.compute.se.pisd.roborally.model.elements;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * checkpoint class, inherits from FieldElement
 *
 * @author Mads Legard Nielsen
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Checkpoint extends FieldElement {
    /**
     * the number of the checkpoint
     */
    int number;
    //count every time a checkpoint is created
    static int numberOfCheckpointsCreated = 0;

    /**
     * constructer of the checkpoint class
     *
     * @param space  takes the space the checkpoint is on
     * @param number the number that the checkpoint needs to be
     * @author Mads Legard Nielsen
     */
    public Checkpoint(Space space, int number) {
        super(space);
        numberOfCheckpointsCreated++;
        this.number = number;
    }

    /**
     * the number of checkpoints to go through
     *
     * @param checkpoints the amount of checkpoints
     * @author Mads Legard Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static void setNumberOfCheckpointsCreated(int checkpoints) {
        numberOfCheckpointsCreated = checkpoints;
    }

    /**
     * sets the number which the checkpoint is
     *
     * @param number the number the checkpoint is
     * @author Mads Legard Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * gets the number which the checkpoint is
     *
     * @return checkpoint number
     * @author Mads Legard Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getNumber() {
        return number;
    }

    /**
     * checks if the checkpoint is reached in the correct order
     *
     * @param checkpointReached what checkpoint has been reached
     * @return True if it is, False if not
     * @author Mads Legard Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean checkCheckpoint(int checkpointReached) {
        return checkpointReached == getNumber() - 1;
    }

    /**
     * if all the checkpoints are reached
     *
     * @param checkpointsReached the number of checkpoints reached
     * @return true if yes false if no
     * @author Mads Legard Nielsen
     */
    public boolean allCheckpointsReached(int checkpointsReached) {
        return checkpointsReached == numberOfCheckpointsCreated;
    }
}
