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
package dk.dtu.compute.se.pisd.roborally.controller;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.elements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.elements.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.elements.Gear;
import dk.dtu.compute.se.pisd.roborally.model.elements.Wall;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.loadBoard;

/**
 * Controls stuff that happens on the Application.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private List<String> MAP_OPTIONS = Arrays.asList("Basis map", "Fun map", "Not fun map");

    final private RoboRally roboRally;

    private GameController gameController;

    /**
     * The AppController constructor.
     *
     * @param roboRally the application instance.
     */
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    private List<String> getMapOptions() {
        File mapsFolder = new File(Resources.getResource(LoadBoard.BOARDSFOLDER).getFile());

        List<String> mapOptions = new ArrayList<>();
        for (File file : Objects.requireNonNull(mapsFolder.listFiles())) {
            String filename = file.getName();
            if (filename.contains(".json")) {
                mapOptions.add(file.getName().replace(".json", ""));
            }
        }
        return mapOptions;
    }

    /**
     * Starts a new game.
     */
    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> playerNumberResult = dialog.showAndWait();

        if (playerNumberResult.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            //TODO
            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            // Need to load file from game name -> playerNumberResult.get();
            List<String> mapOptions = getMapOptions();
            ChoiceDialog<String> dialogMap = new ChoiceDialog<>(mapOptions.get(0), mapOptions);
            dialogMap.setTitle("Maps");
            dialogMap.setHeaderText("Select the map");
            Optional<String> mapResult = dialogMap.showAndWait();

            if (mapResult.isPresent()) {
                gameController = new GameController(null);
                Board board = loadBoard(gameController, mapResult.get());
                gameController.setBoard(board);

                // debug adding
//                {
//                    Space space = board.getSpace(1, 2);
//                    new Wall(space, Heading.EAST);
//                    new Checkpoint(space, 2);
//
//                    space = board.getSpace(1,3);
//                    new ConveyorBelt(gameController, space, true, Heading.SOUTH);
//
//                    space = board.getSpace(1, 4);
//                    new Checkpoint(space, 1);
//                    new Wall(space, Heading.NORTH);
//
//                    space = board.getSpace(3, 4);
//                    new Gear(gameController, space, false);
//                    new Wall(space, Heading.SOUTH);
//                }

                int no = playerNumberResult.get();
                for (int i = 0; i < no; i++) {
                    Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                    board.addPlayer(player);
                    player.setSpace(board.getSpace(i % board.width, i));
                }
                // XXX: V2
                // board.setCurrentPlayer(board.getPlayer(0));
                gameController.startProgrammingPhase();

                roboRally.createBoardView(gameController, null);
            }
        }
    }

    /**
     * Saves the game, to be continued later.
     */
    public void saveGame() {
        // XXX needs to be implemented eventually
    }

    /**
     * Loads a saved game.
     */
    public void loadGame() {
        // XXX needs to be implememted eventually
        // for now, we just create a new game
        if (gameController == null) {
            newGame();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null, this);
            return true;
        }
        return false;
    }

    /**
     * Exits the application.
     * Note: not the game, the application itself.
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Checks whether a game is currently running.
     *
     * @return <code>true</code> if a game is running, else <code>false</code>.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    /**
     * The controller's update method, which is called when a subject changes.
     *
     * @param subject the subject which changed
     */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }
}
