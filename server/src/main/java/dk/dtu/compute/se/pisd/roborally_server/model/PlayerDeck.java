package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.Card;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.ProgramCard;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.UpgradeCard;

import java.util.ArrayList;
import java.util.Arrays;

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

    public PlayerDeck() {
        this.cards = new ArrayList<>(NO_CARDS);
        this.program = new ArrayList<>(NO_REGISTERS);
        this.upgrades = new ArrayList<>(NO_UPGRADES);

        this.energy = 5;
    }

    public PlayerDeck(Card[] cards, Card[] program, UpgradeCard[] upgrades) {
        this.cards = (ArrayList<Card>) Arrays.asList(cards);
        this.program = (ArrayList<Card>) Arrays.asList(program);
        this.upgrades = (ArrayList<UpgradeCard>) Arrays.asList(upgrades);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addEnergy(int add) {
        this.energy += add;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<Card> getProgram() {
        return program;
    }

    public ArrayList<UpgradeCard> getUpgrades() {
        return upgrades;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setProgram(ArrayList<Card> program) {
        this.program = program;
    }

    public void setUpgrades(ArrayList<UpgradeCard> upgrades) {
        this.upgrades = upgrades;
    }

    /**
     * the takeDamage part, where a bad card is given
     */
    public void takeDamage() {
        damageTaken = damageTaken + 1;
    }

    public int getDamage(){
        return damageTaken;
    }

    public void removeDamage() {
        if (damageTaken > 0) {
            damageTaken = damageTaken - 1;
        }
    }

    public void setDamage(int damage) {
        this.damageTaken = damage;
    }

    /**
     * Gets the {@link Card Card} in the player's
     * program, on the index i.
     *
     * @param i the index in the player's program to get the CommandCardField of.
     * @return the {@link Card Card}.
     */
    @JsonIgnore
    public Card getProgramField(int i) {
        if (i >= program.size()) {
            return null;
        }
        return program.get(i);
    }

    public void clearProgram() {
        program.clear();
    }

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
     */
    @JsonIgnore
    public int getUpgradesNum() {
        int size = upgrades.size();
        if (size < 8) {
            return 8;
        }
        return size;
    }

    public void setCardField(int i, Card card) {
        if (i >= cards.size()) {
            return;
        }
        cards.set(i, card);
    }

    public void setProgramField(int i, ProgramCard card) {
        if (i >= program.size()) {
            return;
        }
        program.set(i, card);
    }

    public void setUpgradeField(int i, UpgradeCard card) {
        if (i >= upgrades.size()) {
            return;
        }
        upgrades.set(i, card);
    }
}
