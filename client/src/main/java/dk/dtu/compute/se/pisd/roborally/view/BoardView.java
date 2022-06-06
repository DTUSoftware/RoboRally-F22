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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

/**
 * The BoardView is the visual representation of a {@link dk.dtu.compute.se.pisd.roborally.model.Board Board}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class BoardView extends VBox implements ViewObserver {

    private Board board;

    private ScrollPane mainBoardScrollPane;
    private GridPane mainBoardPane;
    private SpaceView[][] spaces;

    private ScrollPane playersViewScrollPane;
    private PlayersView playersView;

    private Label statusLabel;

    private SpaceEventHandler spaceEventHandler;

    /**
     * The BoardView constructor.
     *
     * @param gameController the {@link dk.dtu.compute.se.pisd.roborally.controller.GameController GameController}
     *                       that controls this board.
     */
    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;


        mainBoardPane = new GridPane();
        HBox mainBoardPaneHBox = new HBox();
        mainBoardPaneHBox.getChildren().add(mainBoardPane);
        mainBoardPaneHBox.setAlignment(Pos.CENTER);
        VBox mainBoardPaneVBox = new VBox();
        mainBoardPaneVBox.getChildren().add(mainBoardPaneHBox);
        mainBoardPaneVBox.setAlignment(Pos.CENTER);
        mainBoardScrollPane = new ScrollPane();
        mainBoardScrollPane.setContent(mainBoardPaneVBox);
        mainBoardScrollPane.setFitToWidth(true);
        mainBoardScrollPane.setFitToHeight(true);

//        mainBoardScrollPane.minWidth(60*8);
//        mainBoardScrollPane.setFitToHeight(true);
//        mainBoardPane.setClip(new Rectangle(400, 400));
        playersViewScrollPane = new ScrollPane();
        playersViewScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        playersView = new PlayersView(gameController);
        playersViewScrollPane.setContent(playersView);
        playersViewScrollPane.setFitToWidth(true);
        statusLabel = new Label("<no status>");

//        BorderPane borderPane = new BorderPane();
//        borderPane.setTop(mainBoardScrollPane);
//        VBox bottomChildren = new VBox(playersViewScrollPane, statusLabel);
//        borderPane.setBottom(bottomChildren);
//        borderPane.prefHeightProperty().bind(this.heightProperty());
//        this.getChildren().add(borderPane);

        this.getChildren().add(mainBoardScrollPane);
        VBox.setVgrow(mainBoardScrollPane, Priority.ALWAYS);
        this.getChildren().add(playersViewScrollPane);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];

        spaceEventHandler = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
                // disable the debug click when clicking on a space
//                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }

        board.attach(this);
        update(board);
    }

    /**
     * Updates the board view, when changes are made to the {@link dk.dtu.compute.se.pisd.roborally.model.Board Board}.
     *
     * @param subject the subject which changed (usually the {@link dk.dtu.compute.se.pisd.roborally.model.Board Board}).
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            Phase phase = board.getPhase();
            statusLabel.setText(board.getStatusMessage());
        }
    }

    /**
     * XXX this handler and its uses should eventually be deleted! This is just to help test the
     * behaviour of the game by being able to explicitly move the players on the board!
     */
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

        final public GameController gameController;

        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }

        /**
         * Handles mouse events, like moving the current player to a space that is clicked.
         *
         * @param event the {@link javafx.scene.input.MouseEvent MouseEvent}.
         */
        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board) {
//                    gameController.moveCurrentPlayerToSpace(space);
                    event.consume();
                }
            }
        }

    }

}
