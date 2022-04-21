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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The different commands that can be used to programmed with.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.

    /** Move forward */
    MOVE_1("Fwd"),
    /** Move 2 forward */
    MOVE_2("Fwd x2"),
    /** Move 3 forward */
    MOVE_3("Fwd x3"),
    /** Move backwards */
    MOVE_BACKWARDS("Backwd"),
    /** Do a u-turn*/
    U_TURN("U-turn"),
    /** Turn right */
    RIGHT("Turn Right"),
    /** Turn left */
    LEFT("Turn Left"),
    // XXX Assignment P3
    /** Left or Right */
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);

    /** The displayName of a command */
    final public String displayName;

    // XXX Assignment P3
    /**
     * The Command constructor.
     *
     * @param displayName the displayName of the Command.
     */
    // Command(String displayName) {
    //     this.displayName = displayName;
    // }
    // replaced by the code below:
    /**
     * list of options
     */
    final private List<Command> options;

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * checks if it's interactive
     * @return if its empty
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * gets the options
     * @return options
     */
    public List<Command> getOptions() {
        return options;
    }

}
