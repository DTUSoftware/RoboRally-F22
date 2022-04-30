package dk.dtu.compute.se.pisd.roborally.view.elements.elements;

import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * checkpoint class, inherits from FieldElement
 */
public class Checkpoint extends FieldElement {
    /**
     * the number of the checkpoint
     */
    int number;

    /**
     * constructer of the checkpoint class
     * @param space takes the space the checkpoint is on
     * @param number the number that the checkpoint needs to be
     */
    public Checkpoint(Space space, int number) {
        super(space);
        this.number = number;
    }

    /**
     * sets the number which the checkpoint is
     *
     * @param number the number the checkpoint is
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * gets the number which the checkpoint is
     *
     * @return checkpoint number
     */
    public int getNumber() {
        return number;
    }

    /**
     * checks if the checkpoint is reached in the correct order
     *
     * @param checkpointReached what checkpoint has been reached
     * @return True if it is, False if not
     */
    public boolean checkCheckpoint(int checkpointReached) {
        if (checkpointReached == getNumber() - 1) {
            return true;
        }
        return false;
    }

    /**
     * does the landing action for the checkpoint card
     */
    @Override
    public void doLandingAction() {
        if (checkCheckpoint(super.getSpace().getPlayer().getCurrentCheckpoint())) {
            super.getSpace().getPlayer().setCurrentCheckpoint(getNumber());
            /*
            if(allCheckpointsReached){
                du må gå i mål
            }
             */
        }
    }
}
