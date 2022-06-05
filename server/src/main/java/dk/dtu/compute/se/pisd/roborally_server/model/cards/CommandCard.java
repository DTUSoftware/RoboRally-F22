package dk.dtu.compute.se.pisd.roborally_server.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommandCard extends Card {
    /** The command linked to the card */
    private Command command;

    /**
     * The CommandCard constructor.
     *
     * @param command the command to put on the card.
     */
    public CommandCard(Command command) {
        super(CardType.COMMAND);
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    /**
     * Gets the displayName of the {@link dk.dtu.compute.se.pisd.roborally_server.model.cards.Command Command} that's on the card.
     *
     * @return the displayName of the Command.
     */
    @JsonIgnore
    public String getName() {
        return command.displayName;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
