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
package dk.dtu.compute.se.pisd.roborally_server.model.board;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.RebootToken;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.SpawnGear;
import org.jetbrains.annotations.NotNull;

/**
 * The Map holds all the spaces.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {
    /** The width of the board */
    public final int width;
    /** The height of the board */
    public final int height;

    /** The name of the board */
    public final String mapName;
    private final Space[][] spaces;

    /** The game */
    private Game game;

    private Checkpoint[] checkpoints;
    private RebootToken[] rebootTokens;
    private SpawnGear[] spawnGears;
    private Space priorityAntenna;

    /**
     * Creates a new board.
     *
     * @param width the width of the board.
     * @param height the height of the board.
     * @param mapName the name of the board.
     */
    public Board(int width, int height, String mapName) {
        this.mapName = mapName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
    }

    /**
     * Creates a new board.
     *
     * @param width the width of the board.
     * @param height the height of the board.
     */
    public Board(int width, int height) {
        this(width, height, null);
    }

    /**
     * Gets the game running on the board.
     *
     * @return the game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game to run on the board.
     *
     * @param game the game.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets array of rebootTokens
     * 
     * @return rebootTokens
     */
    public RebootToken[] getRebootTokens() {
        return rebootTokens != null ? rebootTokens : new RebootToken[0];
    }

    /**
     * Sets rebootTokens
     * 
     * @param rebootTokens resets rebootTokens
     */
    public void setRebootTokens(RebootToken[] rebootTokens) {
        this.rebootTokens = rebootTokens;
    }

    /**
     * Gets array of spawnGears
     *
     * @return spawnGears
     */
    public SpawnGear[] getSpawnGears() {
        return spawnGears != null ? spawnGears : new SpawnGear[0];
    }

    /**
     * Sets spawnGears
     *
     * @param spawnGears resets spawnGears
     */
    public void setSpawnGears(SpawnGear[] spawnGears) {
        this.spawnGears = spawnGears;
    }

    /**
     * Gets a {@link dk.dtu.compute.se.pisd.roborally_server.model.board.Space Space} from its coordinates
     * on the board.
     *
     * @param x The x-coordinate of the Space.
     * @param y The y-coordinate of the Space.
     * @return The {@link dk.dtu.compute.se.pisd.roborally_server.model.board.Space Space}, if found, else null.
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
     * sets the position of the priority antenna
     * @author Mads Legard Nielsen
     * @param position
     */
    public void setPriorityAntenna(Space position){
        this.priorityAntenna = position;
    }

    /**
     * gets the position of the priority antenna
     * @author Mads Legard Nielsen
     * @return the Space / position for the priority antenna
     */
    public Space getPriorityAntennaPosition(){
        return this.priorityAntenna;
    }
    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
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
                y = y+1;
                break;
            case WEST:
                x = x-1;
                break;
            case NORTH:
                y = y-1;
                break;
            case EAST:
                x = x+1;
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
     * @return the name of the board.
     */
    public String getMapName() {
        return this.mapName;
    }
}
