package dk.dtu.compute.se.pisd.roborally.model.cards;

public abstract class Card {
    private CardType type;
    private boolean visible;

    public Card(CardType type, boolean visible) {
        this.type = type;
        this.visible = visible;
    }

    public Card(CardType type) {
        this(type, true);
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public CardType getType() {
        return type;
    }

    public abstract String getName();
}
