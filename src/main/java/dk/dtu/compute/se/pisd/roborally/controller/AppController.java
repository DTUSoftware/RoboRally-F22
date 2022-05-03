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
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadGameState;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.elements.*;
import javafx.scene.control.ChoiceDialog;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.loadBoard;
import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadGameState.loadGameState;
import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadGameState.saveGameState;

/**
 * Controls stuff that happens on the Application.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private AppDirs appDirs = AppDirsFactory.getInstance();

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

    private List<String> getFolderJSON(String foldername) {
        List<String> folderFiles = new ArrayList<>();

        // Resource folder files
        List<String> resourceFolderFiles = new ArrayList<>();
        URL mapsFolderURL = null;
        File mapsFolder = null;
        try {
            mapsFolderURL = Resources.getResource(foldername);
            mapsFolder = new File(mapsFolderURL.getFile());
        } catch (Exception e) {
            if (!e.toString().contains("gamestates not found")) {
                e.printStackTrace();
            }
        }

//        System.out.println("got folder - " + mapsFolder.getPath());

        if (mapsFolder != null && !mapsFolder.getPath().contains(".jar") && mapsFolder.listFiles() != null) {
            for (File file : Objects.requireNonNull(mapsFolder.listFiles())) {
                String filename = file.getName();
                System.out.println(filename);
                if (filename.contains(".json")) {
                    resourceFolderFiles.add(file.getName().replace(".json", ""));
                }
            }
        } else {
            // when we have a .jar file
            // https://mkyong.com/java/java-read-a-file-from-resources-folder/
            try {
                // get path of the current running JAR
                String jarPath = getClass().getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath();
                System.out.println("JAR Path :" + jarPath);

                // TODO: on some computers Java cannot read the maps from the resources folder in the compiled .jar file. fix it or smthn idk
                // file walks JAR
                URI uri = URI.create("jar:file:" + jarPath.replace(" ","%20"));
                try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    resourceFolderFiles = Files.walk(fs.getPath(foldername))
                            .filter(Files::isRegularFile)
                            .map(p -> p.toString().replace(foldername + "/", "").replace(foldername + "\\", "").replace(".json", ""))
                            .collect(Collectors.toList());
                } catch (NoSuchFileException e) {
                    if (!e.toString().contains("gamestates")) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        folderFiles.addAll(resourceFolderFiles);

        // Appdata folder files
        List<String> appdataFolderFiles = new ArrayList<>();
        String appdataFolder = appDirs.getUserDataDir("RoboRally", "prod", "DTU");
        mapsFolder = new File(appdataFolder + "/" + foldername);
        if (mapsFolder.listFiles() != null) {
            for (File file : Objects.requireNonNull(mapsFolder.listFiles())) {
                String filename = file.getName();
                System.out.println(filename);
                if (filename.contains(".json")) {
                    appdataFolderFiles.add(file.getName().replace(".json", ""));
                }
            }
        }
        folderFiles.addAll(appdataFolderFiles);

        return folderFiles;
    }

    private List<String> getMapOptions() {
        List<String> mapOptions = getFolderJSON(LoadBoard.BOARDSFOLDER);
        // TODO: on some computers Java cannot read the maps from the resources folder in the compiled .jar file. this is a temp fix
        if (!mapOptions.contains("dizzy_highway")) {
            mapOptions.add("dizzy_highway");
        }
        return mapOptions;
    }

    private List<String> getGameStateOptions() {
        return getFolderJSON(LoadGameState.GAMESTATEFOLDER);
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

                final String BOARDSFOLDER = "maps";
                InputStream inputStream = null;
                try {
                    inputStream = Resources.getResource(BOARDSFOLDER + "/" + mapResult.get() + ".json").openStream();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                JSONTokener tokener = new JSONTokener(inputStream);
                JSONObject boardJSON = new JSONObject(tokener);
                JSONArray boardObjects = boardJSON.getJSONArray("board");

                String spawn_gear_element;
                JSONArray elementsJSON = null;
                Space space = null;
                int l = 0;

                int no = playerNumberResult.get();
                for (int i = 0; i < no; i++) {
                    Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                    board.addPlayer(player);
                    //TODO: skriv linjen under om til at skaffe et gear til hver spiller, og gem det

                    outerloop:
                    for (; l < boardObjects.length(); l++) {
                        JSONObject spaceJSON = boardObjects.getJSONObject(l);

                        JSONObject positionJSON = spaceJSON.getJSONObject("position");
                        space = board.getSpace(positionJSON.getInt("x"), positionJSON.getInt("y"));

                        elementsJSON = spaceJSON.getJSONArray("elements");

                        for (int j = 0; j < elementsJSON.length(); j++) {
                            JSONObject elementJSON = elementsJSON.getJSONObject(j);
                            if (elementJSON.getString("type").equals("spawn_gear")) {

                                player.setSpace(space);
                                player.setHeading(Heading.valueOf(elementJSON.getString("direction")));
                                player.setStartGearSpace(space, Heading.valueOf(elementJSON.getString("direction")));
                                l++;
                                break outerloop;
                            }
                        }
                    }

                }
                // XXX: V2
                // board.setCurrentPlayer(board.getPlayer(0));
                gameController.startProgrammingPhase();

                roboRally.createBoardView(gameController, null);
            }
        }
    }

    /**
     * Saves the game.
     */
    public void saveGame() {
        if (gameController != null) {
            saveGameState(gameController.board);
        }
    }

    /**
     * Loads a saved game.
     */
    public void loadGame() {
        List<String> gameStateOptions = getGameStateOptions();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(gameStateOptions.get(0), gameStateOptions);
        dialog.setTitle("Game Load");
        dialog.setHeaderText("Select a saved game to continue");
        Optional<String> gameStateResult = dialog.showAndWait();

        if (gameStateResult.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            gameController = new GameController(null);
            loadGameState(gameController, gameStateResult.get());

            roboRally.createBoardView(gameController, null);
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
