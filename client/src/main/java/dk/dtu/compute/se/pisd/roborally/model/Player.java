/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *  Copyright (C) 2022: Marcus Sand, mwasa@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * A Player in the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject {
    /**
     * The number of registers the player can have
     */
    final public static int NO_REGISTERS = 5;
    /**
     * The number of cards the player can have
     */
    final public static int NO_CARDS = 8;

    public Board board;

    /**
     * The name of the Player
     */
    private String name;
    /**
     * The color of the Player
     */
    private String color;

    private UUID id;

    /** keep track of the energy */
    private int energy = 5;

    private Space space;
    private Heading heading = SOUTH;
    private int currentCheckpoint;

    private boolean ready;

    private CardField[] program;
    private CardField[] cards;
    private ArrayList<CardField> upgrades;


    /**
     * Initializes a Player.
     *
     * @param id The player ID.
     */
    public Player(UUID id, Board board) {
        this.id = id;
        this.board = board;
        this.space = null;

        program = new CardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CardField(this);
        }

        cards = new CardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CardField(this);
        }

        upgrades = new ArrayList<>();
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    /**
     * Gets the name of the player.
     *
     * @return the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name the name of the player.
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Gets the color of the player.
     *
     * @return the color of the player.
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the player.
     *
     * @param color the color of the player.
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Gets which {@link dk.dtu.compute.se.pisd.roborally.model.Space Space} the player is currently on.
     *
     * @return the current {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Moves the player to another {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
     *
     * @param space the new {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace && (space == null || space.board != null)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * Gets the current {@link dk.dtu.compute.se.pisd.roborally.model.Heading Heading} of the player.
     *
     * @return the current {@link dk.dtu.compute.se.pisd.roborally.model.Heading Heading}.
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Changes the {@link dk.dtu.compute.se.pisd.roborally.model.Heading Heading} of the player.
     *
     * @param heading the new {@link dk.dtu.compute.se.pisd.roborally.model.Heading Heading}.
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Gets the {@link CardField CommandCardField} in the player's
     * program, on the index i.
     *
     * @param i the index in the player's program to get the CommandCardField of.
     * @return the {@link CardField CommandCardField}.
     */
    public CardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Gets the {@link CardField CommandCardField} in the player's
     * cards, on the index i.
     *
     * @param i the index in the player's cards to get the CommandCardField of.
     * @return the {@link CardField CommandCardField}.
     */
    public CardField getCardField(int i) {
        return cards[i];
    }

    /**
     * Gets the {@link CardField CommandCardField} in the player's
     * installed upgrades, on the index i.
     *
     * @param i the index in the player's upgrades to get the CommandCardField of.
     * @return the {@link CardField CommandCardField}.
     */
    public CardField getUpgradeField(int i) {
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
    public int getUpgradesNum() {
        int size = upgrades.size();
        if (size < 8) {
            return 8;
        }
        return size;
    }

    /**
     * gets current checkpoint
     * @return gets the current checkpoint
     */
    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    /**
     * sets the checkpoint
     * @param checkpoint sets the checkpoints :)
     */
    public void setCurrentCheckpoint(int checkpoint) {
        if (this.currentCheckpoint != checkpoint) {
            this.currentCheckpoint = checkpoint;
            notifyChange();
        }
    }

    /**
     * add x amount of energy to the player
     *
     * @param toAdd how much you want to add to the player
     */
    public void addEnergy(int toAdd) {
        this.energy += toAdd;
    }

    /**
     * subtracts x amount of energy, when for example paying to get other cards
     *
     * @param toSubtract the energy you wish to subtract
     */
    public void subtractEnergy(int toSubtract) {
        this.energy -= toSubtract;
    }

    /**
     * set the players energy
     *
     * @param energy the energy you want to set the players energy to
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setDamage(int damage) {

    }

    /**
     * gives the current energy of the player
     *
     * @return the current energy of the player
     */
    public int getEnergy() {
        return this.energy;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        if (this.ready != ready) {
            this.ready = ready;
            super.notifyChange();
        }
    }
}
