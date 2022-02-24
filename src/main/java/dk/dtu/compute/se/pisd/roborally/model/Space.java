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

/**
 * A 'field' on the {@link dk.dtu.compute.se.pisd.roborally.model.Board Board} which
 * the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player}s can be on.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Space extends Subject {
    /** The Board the Space is on */
    public final Board board;

    /** The x-coordinate of the Space */
    public final int x;
    /** The y-coordinate of the Space */
    public final int y;

    private Player player;

    /**
     * Initializes a Space on a {@link dk.dtu.compute.se.pisd.roborally.model.Board Board}.
     *
     * @param board the {@link dk.dtu.compute.se.pisd.roborally.model.Board Board} the Space belongs to.
     * @param x the x-coordinate of the Space.
     * @param y the y-coordinate of the Space.
     */
    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    /**
     * Gets the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} currently on the Space.
     *
     * @return the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player}.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} to be on the space.
     *
     * @param player the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player}.
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

}
