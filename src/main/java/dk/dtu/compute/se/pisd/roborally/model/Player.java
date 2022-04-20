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

    /**
     * The Board the Player is playing on
     */
    final public Board board;

    /**
     * The name of the Player
     */
    private String name;
    /**
     * The color of the Player
     */
    private String color;
    /**
     * keep track of the power
     */
    private int power = 5;

    private Space space;
    private Heading heading = SOUTH;
    private int currentCheckpoint;

    private CommandCardField[] program;
    private CommandCardField[] cards;


    /**
     * Initializes a Player.
     *
     * @param board The board which the player belongs to.
     * @param color The color of the player.
     * @param name  The name of the player.
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
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
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
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
     * Gets the {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField} in the player's
     * program, on the index i.
     *
     * @param i the index in the player's program to get the CommandCardField of.
     * @return the {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField}.
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Gets the {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField} in the player's
     * cards, on the index i.
     *
     * @param i the index in the player's cards to get the CommandCardField of.
     * @return the {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField}.
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setCurrentCheckpoint(int checkpoint) {
        this.currentCheckpoint = checkpoint;
        notifyChange();
    }

    /**
     * set the players power
     *
     * @param power the power you want to set the players power to
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * gives the current power of the player
     *
     * @return the current power of the player
     */
    public int getPower() {
        return this.power;
    }

    /**
     * add x amount of power to the player
     *
     * @param toAdd how much you want to add to the player
     */
    public void addPower(int toAdd) {
        this.power += toAdd;
    }

    /**
     * subtracts x amount of power, when for example paying to get other cards
     *
     * @param toSubtract the power you wish to subtract
     */
    public void subtractPower(int toSubtract) {
        this.power -= toSubtract;
    }
}
