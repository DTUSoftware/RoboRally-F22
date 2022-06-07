package dk.dtu.compute.se.pisd.roborally_server.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A card (abstract class).
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public abstract class Card {
    private CardType type;
    private boolean visible;

    /**
     * Card constructor.
     * @param type the type of card
     * @param visible whether it is visible
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Card(CardType type, boolean visible) {
        this.type = type;
        this.visible = visible;
    }

    /**
     * Create a card
     * @param type the type of card
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Card(CardType type) {
        this(type, true);
    }

    /**
     * Card visibility
     * @return whether the card is visible or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * Sets card visibility
     * @param visible visible
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns card type
     * @return card type
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public CardType getType() {
        return type;
    }

    /**
     * Gets the displayName of the enum that's on the card.
     * @return displayName
     */
    @JsonIgnore
    public abstract String getName();
}
