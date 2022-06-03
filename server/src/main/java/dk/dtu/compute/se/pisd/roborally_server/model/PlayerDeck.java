package dk.dtu.compute.se.pisd.roborally_server.model;

import dk.dtu.compute.se.pisd.roborally_server.model.cards.Card;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.CommandCard;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.UpgradeCard;

import java.util.ArrayList;

public class PlayerDeck {
    private int energy;

    private ArrayList<Card> cards;
    private ArrayList<CommandCard> program;
    private ArrayList<UpgradeCard> upgrades;

    public PlayerDeck() {
        this.cards = new ArrayList<>();
        this.program = new ArrayList<>();
        this.upgrades = new ArrayList<>();

        this.energy = 5;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<CommandCard> getProgram() {
        return program;
    }

    public ArrayList<UpgradeCard> getUpgrades() {
        return upgrades;
    }
}
