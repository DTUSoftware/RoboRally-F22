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
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main RoboRally application, run as a JavaFX application.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RoboRally extends Application {

    private static final int MIN_APP_WIDTH = 600;

    private Stage stage;
    private BorderPane boardRoot;
    // private RoboRallyMenuBar menuBar;

    // private AppController appController;

    /**
     * Initializes the Application.
     *
     * @throws Exception if not initialized.
     */
    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     * The function that runs when the application starts.
     *
     * @param primaryStage The primary {@link javafx.stage.Stage Stage} of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        AppController appController = new AppController(this);

        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);
        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);

        // aspt ratio - 2/3
        double size_percent = 0.90;
        boardRoot.prefWidthProperty().bind(stage.heightProperty().multiply(size_percent).divide(3).multiply(2));
        boardRoot.prefHeightProperty().bind(stage.heightProperty().multiply(size_percent));
        boardRoot.setMinWidth(stage.heightProperty().multiply(size_percent).divide(3).multiply(2).get());
        boardRoot.setMinHeight(stage.heightProperty().multiply(size_percent).get());
        createBoardView(null, appController);

        Scene primaryScene = new Scene(vbox);

        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    appController.exit();} );
//        stage.minWidthProperty().bind(primaryScene.heightProperty().multiply(2));
//        stage.minHeightProperty().bind(primaryScene.widthProperty().divide(2));
        stage.setResizable(true);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        System.out.println(primaryScene.heightProperty());
        System.out.println(primaryScene.widthProperty());
    }

    /**
     * Creates a new BoardView, and removes the old BoardView, if present.
     *
     * @param gameController The {@link dk.dtu.compute.se.pisd.roborally.controller.GameController GameController}.
     */
    public void createBoardView(GameController gameController, AppController appController) {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();

        if (gameController != null) {
            // create and add view for new board
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }
        else {
            if (appController != null) {
                Button startButton = new Button("New Game");
                startButton.setOnAction( e -> appController.newGame());
                boardRoot.setCenter(startButton);
            }
        }

        stage.sizeToScene();
    }

    /**
     * Stops the game.
     *
     * @throws Exception an exception thrown while tying to stop the game.
     */
    @Override
    public void stop() throws Exception {
        super.stop();

        // XXX just in case we need to do something here eventually;
        //     but right now the only way for the user to exit the app
        //     is delegated to the exit() method in the AppController,
        //     so that the AppController can take care of that.
    }

    /**
     * The main function in the application - launches the game.
     *
     * @param args any arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}