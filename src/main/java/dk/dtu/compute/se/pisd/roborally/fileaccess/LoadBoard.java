/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
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
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.elements.*;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

    /** The folder where the maps are saved */
    public static final String BOARDSFOLDER = "maps";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final int defaultBoardHeight = 8;
    private static final int defaultBoardWidth = 8;

    public static Board loadBoard(GameController gameController, String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource(BOARDSFOLDER + "/" + boardname + ".json").openStream();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            Board board = new Board(defaultBoardWidth,defaultBoardHeight, boardname);
            gameController.setBoard(board);
            return board;
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject boardJSON = new JSONObject(tokener);

		Board board = null;
		try {
            JSONObject size = boardJSON.getJSONObject("size");
            board = new Board(size.getInt("width"), size.getInt("height"), boardname);

            // Add the board to the gamecontroller
            gameController.setBoard(board);

            // add all spaces
            JSONArray boardObjects = boardJSON.getJSONArray("board");

            ArrayList<RebootToken> rebootTokens = new ArrayList<>();
            for (int i = 0; i < boardObjects.length(); i++) {
                JSONObject spaceJSON = boardObjects.getJSONObject(i);

                JSONObject positionJSON = spaceJSON.getJSONObject("position");
                Space space = board.getSpace(positionJSON.getInt("x"), positionJSON.getInt("y"));

                JSONArray elementsJSON = spaceJSON.getJSONArray("elements");
                for (int j = 0; j < elementsJSON.length(); j++) {
                    JSONObject elementJSON = elementsJSON.getJSONObject(j);
                    switch (elementJSON.getString("type")) {
                        case "checkpoint":
                            new Checkpoint(gameController,space, elementJSON.getInt("number"));
                            break;
                        case "conveyor_belt":
                            new ConveyorBelt(
                                    gameController,
                                    space,
                                    elementJSON.getBoolean("color"),
                                    Heading.valueOf(elementJSON.getString("direction"))
                            );
                            break;
                        case "energy_space":
                            new EnergySpace(gameController, space);
                            break;
                        case "gear":
                            new Gear(gameController, space, elementJSON.getBoolean("direction"));
                            break;
                        case "laser":
                            new Wall(space, Heading.valueOf(elementJSON.getString("direction")), true);
                            new Laser(gameController, space, Heading.valueOf(elementJSON.getString("direction")));
                            break;
                        case "pit":
                            new Pit(gameController, space);
                            break;
                        case "priority_antenna":
                            new PriorityAntenna(space);
                            break;
                        case "reboot_token":
                            JSONObject rebootBounds = elementJSON.getJSONObject("bounds");
                            rebootTokens.add(new RebootToken(gameController, space, Heading.valueOf(elementJSON.getString("direction")), rebootBounds.getInt("x1"), rebootBounds.getInt("y1"), rebootBounds.getInt("x2"), rebootBounds.getInt("y2")));
                            break;
                        case "spawn_gear":
                            new SpawnGear(gameController, space, Heading.valueOf(elementJSON.getString("direction")));
                            break;
                        case "wall":
                            new Wall(space, Heading.valueOf(elementJSON.getString("direction")), false);
                            break;
                        case "push_panel":
                            new Wall(space, Heading.valueOf(elementJSON.getString("direction")), true);
                            JSONObject pushPanel = elementJSON.getJSONObject("registers");
                            new PushPanel(gameController, space, Heading.valueOf(elementJSON.getString("direction")), pushPanel.getInt("register1"), pushPanel.getInt("register2"));
                            break;
                    }
                }
            }

            // add reboot tokens
            board.setRebootTokens(rebootTokens.toArray(new RebootToken[0]));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    }

    public static void saveBoard(Board board, String name) {
        JSONObject boardJSON = new JSONObject();

        JSONObject size = new JSONObject();
        size.put("width", board.width);
        size.put("height", board.height);
        boardJSON.put("size", size);

        JSONArray boardObjects = new JSONArray();
        for (int x=0; x<board.width; x++) {
            for (int y=0; y<board.height; y++) {
                JSONObject spaceJSON = new JSONObject();
                Space space = board.getSpace(x,y);

                JSONObject positionJSON = new JSONObject();
                positionJSON.put("x", space.x);
                positionJSON.put("y", space.y);
                spaceJSON.put("position", positionJSON);

                JSONArray elementsJSON = new JSONArray();
                for (FieldElement element : space.getFieldObjects()) {
                    JSONObject elementJSON = new JSONObject();
                    if (element instanceof Checkpoint) {
                        elementJSON.put("type", "checkpoint");
                        elementJSON.put("number", ((Checkpoint) element).getNumber());
                    }
                    else if (element instanceof ConveyorBelt) {
                        elementJSON.put("type", "conveyor_belt");
                        elementJSON.put("color", ((ConveyorBelt) element).getColor());
                        elementJSON.put("direction", ((ConveyorBelt) element).getDirection().name());
                    }
                    else if (element instanceof EnergySpace) {
                        elementJSON.put("type", "energy_space");
                    }
                    else if (element instanceof Gear) {
                        elementJSON.put("type", "gear");
                        elementJSON.put("direction", ((Gear) element).getDirection());
                    }
                    else if (element instanceof Laser) {
                        elementJSON.put("type", "laser");
                        elementJSON.put("direction", ((Laser) element).getDirection().name());
                    }
                    else if (element instanceof Pit) {
                        elementJSON.put("type", "pit");
                    }
                    else if (element instanceof PriorityAntenna) {
                        elementJSON.put("type", "priority_antenna");
                    }
                    else if (element instanceof PushPanel) {
                        elementJSON.put("type", "push_panel");
                        elementJSON.put("direction", ((PushPanel) element).getDirection().name());
                    }
                    else if (element instanceof RebootToken) {
                        elementJSON.put("type", "reboot_token");
                        JSONObject rebootBounds = new JSONObject();
                        rebootBounds.put("x1", ((RebootToken) element).getx1());
                        rebootBounds.put("y1", ((RebootToken) element).gety1());
                        rebootBounds.put("x2", ((RebootToken) element).getx2());
                        rebootBounds.put("y2", ((RebootToken) element).gety2());
                        elementJSON.put("bounds", rebootBounds);
                    }
                    else if (element instanceof SpawnGear) {
                        elementJSON.put("type", "spawn_gear");
                        elementJSON.put("direction", ((SpawnGear) element).getDirection().name());
                    }
                    else if (element instanceof Wall) {
                        elementJSON.put("type", "wall");
                        elementJSON.put("direction", ((Wall) element).getDirection().name());
                    }
                    else {
                        elementJSON.put("type", "undefined");
                    }
                    elementsJSON.put(elementJSON);
                }
                spaceJSON.put("elements", elementsJSON);

                boardObjects.put(spaceJSON);
            }
        }

        boardJSON.put("board", boardObjects);

        URL fileURL = Resources.getResource(BOARDSFOLDER + "/" + name + ".json");
        try (FileWriter file = new FileWriter(fileURL.getPath())) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(boardJSON.toString(2));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
