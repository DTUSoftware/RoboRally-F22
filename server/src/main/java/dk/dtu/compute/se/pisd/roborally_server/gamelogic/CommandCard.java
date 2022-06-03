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
package dk.dtu.compute.se.pisd.roborally_server.gamelogic;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.Command;
import org.jetbrains.annotations.NotNull;

/**
 * A card holding a single {@link Command Command}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CommandCard extends Subject {
    /** The command linked to the card */
    final public Command command;

    /**
     * The CommandCard constructor.
     *
     * @param command the command to put on the card.
     */
    public CommandCard(@NotNull Command command) {
        this.command = command;
    }

    /**
     * Gets the displayName of the {@link Command Command} that's on the card.
     *
     * @return the displayName of the Command.
     */
    public String getName() {
        return command.displayName;
    }


}
