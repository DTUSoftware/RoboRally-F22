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

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.elements.*;
import dk.dtu.compute.se.pisd.roborally.server.GameService;
import dk.dtu.compute.se.pisd.roborally.view.elements.*;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Controls stuff that happens on the {@link dk.dtu.compute.se.pisd.roborally.model.Board Board}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {
    /**
     * The board linked to the controller
     */
    public Board board;
    private UUID gameID;

    final private RoboRally roboRally;

    /**
     * The GameController constructor.
     * @param roboRally the roborally class
     * @param board the board to control.
     */
    public GameController(RoboRally roboRally, Board board) {
        this.board = board;
        this.roboRally = roboRally;
    }

    /**
     * Set the board for the gamecontroller to control.
     * Used for initialization that needs the gamecontroller.
     *
     * @param board the board.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }


    public void updateGameState() {
        JSONObject gameState = GameService.getGameState(gameID);
        if (gameState == null) {
            System.out.println("GameState is null!");
            return;
        }

        boolean createPlayers = board.getPlayersNumber() == 0;

        JSONArray players = gameState.getJSONArray("players");
        for (int i = 0; i < players.length(); i++) {
            JSONObject playerJSON = players.getJSONObject(i);

            if (createPlayers) {
                board.addPlayer(new Player(UUID.fromString(playerJSON.getString("id")), board));
            }

            Player player = board.getPlayer(i);

            if (!UUID.fromString(playerJSON.getString("id")).equals(player.getID())) {
                System.out.println("PLAYER UUID MISMATCH WITH SERVER! - CANCELING SYNC!");
                return;
            }

            JSONObject positionJSON = playerJSON.getJSONObject("position");
            updatePlayer(board.getPlayer(i),
                    playerJSON.getString("color"), playerJSON.getString("name"),
                    playerJSON.getInt("currentCheckpoint"),
                    positionJSON.getInt("x"), positionJSON.getInt("y"), Heading.valueOf(positionJSON.getString("heading")));

            // Update their deck
            // TODO: only update current player's deck? (local player)
            JSONObject playerDeck = GameService.getPlayerDeck(gameID, player.getID());
            if (playerDeck == null) {
                System.out.println("Could not fetch player deck for " + player.getName());
                continue;
            }

            updatePlayerDeck(player, playerDeck.getInt("energy"), playerDeck.getJSONArray("program"), playerDeck.getJSONArray("cards"), playerDeck.getJSONArray("upgrades"));
        }


        board.setPhase(gameState.getEnum(Phase.class, "phase"));
        board.setCurrentPlayer(board.getPlayer(gameState.getInt("currentPlayer")));
    }

    private void updatePlayer(Player player, String color, String name, int currentCheckpoint, int x, int y, Heading heading) {
        player.setColor(color);
        player.setName(name);
        player.setCurrentCheckpoint(currentCheckpoint);
        player.setSpace(board.getSpace(x, y));
        player.setHeading(heading);
    }

    private void updatePlayerDeck(Player player, int energy, JSONArray program, JSONArray cards, JSONArray upgrades) {
        player.setEnergy(energy);

        // Program
        for (int j = 0; j < program.length(); j++) {
            JSONObject programJSON = program.getJSONObject(j);
            CommandCard commandCard = new CommandCard(Command.valueOf(programJSON.getString("command")));
            CommandCardField field = player.getProgramField(j);
            field.setCard(commandCard);
            field.setVisible(programJSON.getBoolean("visible"));
        }

        // Cards
        for (int j = 0; j < cards.length(); j++) {
            JSONObject card = cards.getJSONObject(j);
            CommandCard commandCard = new CommandCard(Command.valueOf(card.getString("command")));
            CommandCardField field = player.getCardField(j);
            field.setCard(commandCard);
            field.setVisible(card.getBoolean("visible"));
        }

        // Upgrades
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     * <p>
     * - the current player should be moved to the given space
     * (if it is free()
     * - and the current player should be set to the player
     * following the current player
     * - the counter of moves in the game should be increased by one
     * if the player is moved
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        if (space.free()) {
            space.setPlayer(board.getCurrentPlayer());
            board.endCurrentPlayerTurn();
        }

        if (space != null && space.board == board) {
            Player currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
                board.setCurrentPlayer(board.getPlayer(playerNumber));
            }
        }

    }

    // XXX: V2

    /**
     * Starts the programming phase.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2

    /**
     * Generates a random command card.
     *
     * @return the random {@link dk.dtu.compute.se.pisd.roborally.model.CommandCard CommandCard}.
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2

    /**
     * Ends the programming phase.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2

    /**
     * Make the programming field visible.
     *
     * @param register the register to show?
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2

    /**
     * Hides the program fields.
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Moves a {@link dk.dtu.compute.se.pisd.roborally.model.CommandCard CommandCard} on a source
     * {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField} to
     * another {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField}.
     * Only moves the card, if there is a card on the source field, and there is no card on the target field.
     *
     * @param source The source {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField}.
     * @param target The {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField CommandCardField} to move the card to.
     * @return <code>true</code> if the card is moved, else <code>false</code>.
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * wins the game
     * @param player the player that wins the game
     */
    public void winTheGame(Player player){
        // show popup
        if (roboRally != null) {
            List<String> yesno = new ArrayList<>();
            yesno.add("Yes");
            yesno.add("Yes, and reset the board");
            yesno.add("No");
            yesno.add("No, exit game");

            ChoiceDialog<String> dialogContinue = new ChoiceDialog<>(yesno.get(0), yesno);
            dialogContinue.setTitle(player.getName() + " won the game!");
            dialogContinue.setHeaderText("Do you want to continue playing?");
            Optional<String> continueResult = dialogContinue.showAndWait();

            if (continueResult.isPresent()) {
                // yes
                if (continueResult.get().equals(yesno.get(0))) {
                    // do nothing
                }
                // yes, reset
                else if (continueResult.get().equals(yesno.get(1))) {
                    resetGame();
                }
                // no (go to menu)
                else if (continueResult.get().equals(yesno.get(2))) {
                    roboRally.createBoardView(null, null);
                }
                // no, exit (exit app)
                else if (continueResult.get().equals(yesno.get(3))) {
                    roboRally.exitApplication();
                }
            }
        }
    }

    /**
     * resets the game
     */
    private void resetGame() {
        // TODO: reset the game with same map and same players
    }

}
