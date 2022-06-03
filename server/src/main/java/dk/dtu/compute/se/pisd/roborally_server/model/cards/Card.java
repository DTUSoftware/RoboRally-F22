package dk.dtu.compute.se.pisd.roborally_server.model.cards;

public class Card {
    private CardType type;

    private boolean visible;

    enum CardType {
        COMMAND,
        UPGRADE,
        DAMAGE
    }

    public boolean getVisible() {
        return visible;
    }

    public CardType getType() {
        return type;
    }
}
