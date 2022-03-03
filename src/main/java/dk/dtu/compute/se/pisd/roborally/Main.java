/*
 *  This file is part of the initial project provided for the
 *  course "Advanced Programming (02324)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
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
package dk.dtu.compute.se.pisd.roborally;

/**
 * The main class for the game.
 * Makes the game able to compile and run through Maven,
 * without having to add JavaFX to the module-path.
 */
public class Main {
    /**
     * The main function - runs the main function in {@link dk.dtu.compute.se.pisd.roborally.RoboRally RoboRally}.
     *
     * @param args the arguments to pass to {@link dk.dtu.compute.se.pisd.roborally.RoboRally RoboRally}.
     */
    public static void main(String[] args) {
        RoboRally.main(args);
    }
}