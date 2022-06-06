package dk.dtu.compute.se.pisd.roborally_server;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.Game;
import dk.dtu.compute.se.pisd.roborally_server.model.GameState;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.model.Player;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.Pit;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.SpawnGear;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.Wall;
import dk.dtu.compute.se.pisd.roborally_server.server.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameLogicController gameController;

    @BeforeEach
    void setUp() {
        GameService gameService = new GameService();
        Game game = new Game();
        gameService.addGame(game);
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        game.setBoard(board);
        gameController = game.getGameLogicController();
        game.setPlayerCount(6);
        game.initializePlayers();
        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player player = game.getGameState().getPlayer(i);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        game.getGameState().setCurrentPlayer(0);
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.getGame().getBoard();
        GameState gameState = gameController.getGame().getGameState();
        Player player1 = gameState.getPlayer(0);
        Player player2 = gameState.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, gameState.getPlayer(gameState.getCurrentPlayer()), "Current player should be " + player2.getName() +"!");
    }

    @Test
    void moveForward() {
        Board board = gameController.getGame().getBoard();
        Player current = gameController.getGame().getGameState().getPlayer(gameController.getGame().getGameState().getCurrentPlayer());

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void moveWall() {
        Board board = gameController.getGame().getBoard();
        Player currentPlayer = gameController.getGame().getGameState().getPlayer(gameController.getGame().getGameState().getCurrentPlayer());
        board.getSpace(4, 4).setPlayer(currentPlayer);

        Space spaceWall = board.getSpace(4,5);
        new Wall(spaceWall, Heading.NORTH, false);

        currentPlayer.setHeading(Heading.SOUTH);
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should not have moved!");
        Assertions.assertEquals(4, currentPlayer.getSpace().y, "Player should not have moved!");
    }

    @Test
    void pushRobot() {
        Board board = gameController.getGame().getBoard();
        Player currentPlayer = gameController.getGame().getGameState().getPlayer(gameController.getGame().getGameState().getCurrentPlayer());
        Player otherPlayer = gameController.getGame().getGameState().getPlayer(3);

        board.getSpace(4, 4).setPlayer(currentPlayer);
        board.getSpace(4, 5).setPlayer(otherPlayer);

        Space spaceWall = board.getSpace(4,7);
        new Wall(spaceWall, Heading.NORTH, false);

        currentPlayer.setHeading(Heading.SOUTH);
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should not have moved in that direction!");
        Assertions.assertEquals(5, currentPlayer.getSpace().y, "Player should have moved!");
        Assertions.assertEquals(4, otherPlayer.getSpace().x, "Player should not have moved in that direction!");
        Assertions.assertEquals(6, otherPlayer.getSpace().y, "Player should have moved!");
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should not have moved!");
        Assertions.assertEquals(5, currentPlayer.getSpace().y, "Player should not have moved!");
        Assertions.assertEquals(4, otherPlayer.getSpace().x, "Player should not have moved!");
        Assertions.assertEquals(6, otherPlayer.getSpace().y, "Player should not have moved!");

    }

    @Test
    void mapFall() {
        Board board = gameController.getGame().getBoard();
        Player currentPlayer = gameController.getGame().getGameState().getPlayer(gameController.getGame().getGameState().getCurrentPlayer());

        Space spawnSpace = board.getSpace(4, 4);
        currentPlayer.setStartGear(new SpawnGear(gameController, spawnSpace, Heading.EAST));

        currentPlayer.setSpace(spawnSpace);
        currentPlayer.setHeading(Heading.NORTH);

        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should be on spawngear!");
        Assertions.assertEquals(4, currentPlayer.getSpace().y, "Player should be on spawngear!");
        gameController.forwardX(currentPlayer, 4);
        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should not have moved in that direction!");
        Assertions.assertEquals(0, currentPlayer.getSpace().y, "Player should have moved!");
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should have fallen off the map and rebooted!");
        Assertions.assertEquals(4, currentPlayer.getSpace().y, "Player should not have moved in that direction!");
    }

    @Test
    void pitFall() {
        Board board = gameController.getGame().getBoard();
        Player currentPlayer = gameController.getGame().getGameState().getPlayer(gameController.getGame().getGameState().getCurrentPlayer());

        Space spawnSpace = board.getSpace(4, 4);

        currentPlayer.setStartGear(new SpawnGear(gameController, spawnSpace, Heading.EAST));

        Space pitSpace = board.getSpace(6, 4);
        new Pit(gameController, pitSpace);

        currentPlayer.setSpace(spawnSpace);
        currentPlayer.setHeading(Heading.EAST);

        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should be on spawngear!");
        Assertions.assertEquals(4, currentPlayer.getSpace().y, "Player should be on spawngear!");
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(5, currentPlayer.getSpace().x, "Player should have have moved!");
        Assertions.assertEquals(4, currentPlayer.getSpace().y, "Player should not have moved in that direction!");
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(4, currentPlayer.getSpace().x, "Player should have fallen into pit and rebooted!");
        Assertions.assertEquals(4, currentPlayer.getSpace().y, "Player should not have moved in that direction!");
    }

    @Test
    void checkpoint() {
        Board board = gameController.getGame().getBoard();
        Player currentPlayer = gameController.getGame().getGameState().getPlayer(gameController.getGame().getGameState().getCurrentPlayer());
        Assertions.assertEquals(0, currentPlayer.getCurrentCheckpoint(), "Player should not have reached a checkpoint!");

        board.getSpace(4, 4).setPlayer(currentPlayer);

        Space spaceCheckpoint2 = board.getSpace(4,5);
        new Checkpoint(gameController, spaceCheckpoint2, 2);
        Space spaceCheckpoint1 = board.getSpace(4,6);
        new Checkpoint(gameController, spaceCheckpoint1, 1);

        currentPlayer.setHeading(Heading.SOUTH);
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(0, currentPlayer.getCurrentCheckpoint(), "Player should not have reached that checkpoint!");
        gameController.moveForward(currentPlayer);
        Assertions.assertEquals(1, currentPlayer.getCurrentCheckpoint(), "Player should have reached that checkpoint!");
        gameController.moveBackwards(currentPlayer);
        Assertions.assertEquals(2, currentPlayer.getCurrentCheckpoint(), "Player should have reached that checkpoint!");
    }
}