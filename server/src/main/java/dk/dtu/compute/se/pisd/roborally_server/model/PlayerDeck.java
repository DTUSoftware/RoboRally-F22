package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.Card;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.ProgramCard;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.UpgradeCard;
import dk.dtu.compute.se.pisd.roborally_server.server.service.GameService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A player's deck.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class PlayerDeck {
    private int energy;
    private int damageTaken = 0;

    private ArrayList<Card> cards;
    private ArrayList<Card> program;
    private ArrayList<UpgradeCard> upgrades;

    /**
     * The number of registers the player can have in their program
     */
    final public static int NO_REGISTERS = 5;

    /**
     * The number of cards the player can have
     */
    final public static int NO_CARDS = 8;

    /**
     * The number of upgrades the player can have
     */
    final public static int NO_UPGRADES = 8;

    /**
     * Creates a new playerdeck.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public PlayerDeck() {
        this.cards = new ArrayList<>(NO_CARDS);
        this.program = new ArrayList<>(NO_REGISTERS);
        this.upgrades = new ArrayList<>(NO_UPGRADES);

        this.energy = 5;
    }

    /**
     * Creates a new playerdeck from given cards.
     *
     * @param cards    the cards
     * @param program  the program
     * @param upgrades the upgrades
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public PlayerDeck(Card[] cards, Card[] program, UpgradeCard[] upgrades) {
        this.cards = (ArrayList<Card>) Arrays.asList(cards);
        this.program = (ArrayList<Card>) Arrays.asList(program);
        this.upgrades = (ArrayList<UpgradeCard>) Arrays.asList(upgrades);
    }

    /**
     * Gets player energy
     *
     * @return player energy
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets player energy
     *
     * @param energy player energy
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * Adds energy
     *
     * @param add energy to add
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void addEnergy(int add) {
        this.energy += add;
    }

    /**
     * Gets cards as list
     *
     * @return card list
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Gets program as list
     *
     * @return program list
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ArrayList<Card> getProgram() {
        return program;
    }

    /**
     * gets upgrades as list
     *
     * @return upgrade list
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ArrayList<UpgradeCard> getUpgrades() {
        return upgrades;
    }

    /**
     * Sets the cards to new list.
     *
     * @param cards the card list to apply
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Sets the program to new list
     *
     * @param program the list to apply
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setProgram(ArrayList<Card> program) {
        this.program = program;
    }

    /**
     * Sets the upgrades to new list
     *
     * @param upgrades the list to apply
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setUpgrades(ArrayList<UpgradeCard> upgrades) {
        this.upgrades = upgrades;
    }

    /**
     * the takeDamage part, where a bad card is given
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     */
    public void takeDamage() {
        damageTaken = damageTaken + 1;
    }

    /**
     * Gets damage taken
     *
     * @return damage
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public int getDamage() {
        return damageTaken;
    }

    /**
     * Removes damage
     *
     * @author Oscar Maxwell
     */
    public void removeDamage() {
        if (damageTaken > 0) {
            damageTaken = damageTaken - 1;
        }
    }

    /**
     * Sets damage taken
     *
     * @param damage damage taken
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setDamage(int damage) {
        this.damageTaken = damage;
    }

    /**
     * Gets the {@link Card Card} in the player's
     * program, on the index i.
     *
     * @param i the index in the player's program to get the CommandCardField of.
     * @return the {@link Card Card}.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Card getProgramField(int i) {
        if (i >= program.size()) {
            return null;
        }
        return program.get(i);
    }

    /**
     * Clears program.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void clearProgram() {
        program.clear();
    }

    /**
     * Populates deck with cards (NULL CARDS, PLEASE FILL IMMEDIATELY).
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void populateCards() {
        while (cards.size() < NO_CARDS) {
            cards.add(null);
        }
    }

    /**
     * Gets the {@link Card Card} in the player's
     * cards, on the index i.
     *
     * @param i the index in the player's cards to get the CommandCardField of.
     * @return the {@link Card Card}.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    @JsonIgnore
    public Card getCardField(int i) {
        if (i >= cards.size()) {
            return null;
        }
        return cards.get(i);
    }

    /**
     * Gets the {@link UpgradeCard UpgradeCard} in the player's
     * installed upgrades, on the index i.
     *
     * @param i the index in the player's upgrades to get the CommandCardField of.
     * @return the {@link UpgradeCard UpgradeCard}.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public UpgradeCard getUpgradeField(int i) {
        if (i >= upgrades.size()) {
            return null;
        }
        return upgrades.get(i);
    }

    /**
     * Gets amount of a players current upgrades
     *
     * @return 8 if amount is under 8 and the spicific amount if equal or above.
     * @author Nicolai Udbye
     */
    @JsonIgnore
    public int getUpgradesNum() {
        int size = upgrades.size();
        if (size < 8) {
            return 8;
        }
        return size;
    }

    /**
     * Sets a card field.
     *
     * @param i    the field to set
     * @param card the card to set at field
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setCardField(int i, Card card) {
        if (i >= cards.size()) {
            return;
        }
        cards.set(i, card);
    }

    /**
     * Sets a program field.
     *
     * @param i    the field to set
     * @param card the card to set at field
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setProgramField(int i, ProgramCard card) {
        if (i >= program.size()) {
            return;
        }
        program.set(i, card);
    }

    /**
     * Sets a upgrade field.
     *
     * @param i    the field to set
     * @param card the card to set at field
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setUpgradeField(int i, UpgradeCard card) {
        if (i >= upgrades.size()) {
            return;
        }
        upgrades.set(i, card);
    }
}
