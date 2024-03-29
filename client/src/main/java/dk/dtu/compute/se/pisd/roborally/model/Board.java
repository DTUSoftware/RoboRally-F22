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
import dk.dtu.compute.se.pisd.roborally.model.elements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.elements.RebootToken;
import dk.dtu.compute.se.pisd.roborally.model.elements.SpawnGear;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Board holds all the players, spaces and keeps track of the
 * current {@link dk.dtu.compute.se.pisd.roborally.model.Phase Phase}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {
    /**
     * The width of the board
     */
    public final int width;
    /**
     * The height of the board
     */
    public final int height;

    /**
     * The name of the board
     */
    public final String boardName;
    /**
     * The ID of the board
     */
    private Integer gameId;

    private final Space[][] spaces;
    private final List<Player> players = new ArrayList<>();

    private Player current;
    private Checkpoint[] checkpoints;
    private SpawnGear[] spawnGears;
    private RebootToken[] rebootTokens;

    private Phase phase = Phase.WAITING;

    private int step = 0;
    private boolean stepMode;

    /**
     * Creates a new board.
     *
     * @param width     the width of the board.
     * @param height    the height of the board.
     * @param boardName the name of the board.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Board(int width, int height, String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * Creates a new board.
     *
     * @param width  the width of the board.
     * @param height the height of the board.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Board(int width, int height) {
        this(width, height, null);
    }

    /**
     * Gets the ID of the game running on the board.
     *
     * @return the game ID.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Sets the ID of the game to run on the board.
     *
     * @param gameId the game ID.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Gets array of rebootTokens
     *
     * @return rebootTokens
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public RebootToken[] getRebootTokens() {
        return rebootTokens != null ? rebootTokens : new RebootToken[0];
    }

    /**
     * Sets rebootTokens
     *
     * @param rebootTokens resets rebootTokens
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setRebootTokens(RebootToken[] rebootTokens) {
        this.rebootTokens = rebootTokens;
    }

    /**
     * Gets array of spawnGears
     *
     * @return spawnGears
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public SpawnGear[] getSpawnGears() {
        return spawnGears != null ? spawnGears : new SpawnGear[0];
    }

    /**
     * Sets spawnGears
     *
     * @param spawnGears resets spawnGears
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setSpawnGears(SpawnGear[] spawnGears) {
        this.spawnGears = spawnGears;
    }

    /**
     * Gets a {@link dk.dtu.compute.se.pisd.roborally.model.Space Space} from its coordinates
     * on the board.
     *
     * @param x The x-coordinate of the Space.
     * @param y The y-coordinate of the Space.
     * @return The {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}, if found, else null.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * Gets the number of players.
     *
     * @return the number of players.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * Adds a player to the game.
     *
     * @param player The {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} to add to the game.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void addPlayer(@NotNull Player player) {
        if (!players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * Gets a player from the player number.
     *
     * @param i The player number of the Player.
     * @return The {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} if found, else null.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * get player from uuid
     *
     * @param playerID uuid
     * @return the player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Player getPlayerFromID(UUID playerID) {
        for (Player player : players) {
            if (player.getID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gets the current player.
     *
     * @return The current {@link dk.dtu.compute.se.pisd.roborally.model.Player Player}.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Sets the current player.
     *
     * @param player The {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} to become the new current player.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Changes the current player to the next player - ends the turn of the current player.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void endCurrentPlayerTurn() {
        setCurrentPlayer(getPlayer((getPlayerNumber(getCurrentPlayer()) + 1) % getPlayersNumber()));
        notifyChange();
    }

    /**
     * Gets the current {@link dk.dtu.compute.se.pisd.roborally.model.Phase Phase}.
     *
     * @return the current {@link dk.dtu.compute.se.pisd.roborally.model.Phase Phase}.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Change the {@link dk.dtu.compute.se.pisd.roborally.model.Phase Phase}.
     *
     * @param phase the new {@link dk.dtu.compute.se.pisd.roborally.model.Phase Phase}.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * Gets the current step.
     *
     * @return the current step.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the step.
     *
     * @param step the new step.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Whether the game currently is running in step mode.
     *
     * @return <code>true</code> if stepMode, else <code>false</code>.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * Enable/disable step mode.
     *
     * @param stepMode whether to enable or disable step mode.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * Gets the Player's player number.
     *
     * @param player The {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} to get the number of.
     * @return the Player's player number.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public int getPlayerNumber(@NotNull Player player) {
        return players.indexOf(player);
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space   the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        // TODO needs to be implemented based on the actual spaces
        //      and obstacles and walls placed there. For now it,
        //      just calculates the next space in the respective
        //      direction in a cyclic way.

        // XXX an other option (not for now) would be that null represents a hole
        //     or the edge of the board in which the players can fall

        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = y + 1;
                break;
            case WEST:
                x = x - 1;
                break;
            case NORTH:
                y = y - 1;
                break;
            case EAST:
                x = x + 1;
                break;
        }
//        Heading reverse = Heading.values()[(heading.ordinal() + 2)% Heading.values().length];

        if (x < 0 || y < 0 || x > width || y > height) {
            return null;
        }

        Space result = getSpace(x, y);
        return result;
    }

    /**
     * Gets the name of the board.
     *
     * @return the name of the board.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public String getBoardName() {
        return this.boardName;
    }

    /**
     * Gets a string representation of the current status of the game.
     *
     * @return The current status of the game.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player: " + getCurrentPlayer().getName() +
                ", Step: " + getStep() +
                ", Player checkpoint: " + getCurrentPlayer().getCurrentCheckpoint() +
                ", Player energy: " + getCurrentPlayer().getEnergy() +
                ", Player damage: " + getCurrentPlayer().getDamage();
    }
}
