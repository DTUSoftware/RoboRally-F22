package dk.dtu.compute.se.pisd.roborally_server.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProgramCard extends Card {
    /** The command linked to the card */
    private Program program;

    /**
     * The CommandCard constructor.
     *
     * @param program the command to put on the card.
     */
    public ProgramCard(Program program) {
        super(CardType.PROGRAM);
        this.program = program;
    }

    public Program getCommand() {
        return program;
    }

    /**
     * Gets the displayName of the {@link Program Command} that's on the card.
     *
     * @return the displayName of the Command.
     */
    @JsonIgnore
    public String getName() {
        return program.displayName;
    }

    public void setCommand(Program program) {
        this.program = program;
    }
}
