package dk.dtu.compute.se.pisd.roborally_server.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Creates a new programcard.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class ProgramCard extends Card {
    /**
     * The command linked to the card
     */
    private Program program;

    /**
     * The CommandCard constructor.
     *
     * @param program the command to put on the card.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ProgramCard(Program program) {
        super(CardType.PROGRAM);
        this.program = program;
    }

    /**
     * Gets the program
     *
     * @return the program
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Gets the displayName of the {@link Program Command} that's on the card.
     *
     * @return the displayName of the Command.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    @Override
    public String getName() {
        return program.displayName;
    }

    /**
     * Sets the program
     *
     * @param program the program
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setProgram(Program program) {
        this.program = program;
    }
}
