package dk.dtu.compute.se.pisd.roborally_server.model.cards;

public class CommandCard extends Card {
    private Command command;

    public CommandCard(Command command) {
        super();
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
