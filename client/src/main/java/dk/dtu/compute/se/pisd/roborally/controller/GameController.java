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
import dk.dtu.compute.se.pisd.roborally.model.cards.*;
import dk.dtu.compute.se.pisd.roborally.server.GameService;
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
     *
     * @param roboRally the roborally class
     * @param board     the board to control.
     * @author Ekkart Kindler, ekki@dtu.dk
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
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Sets the gameID
     *
     * @param gameID gameID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    /**
     * gets gameID
     *
     * @return gameID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Update the gameState with new gamestate from the server.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
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

            if (player == null || !UUID.fromString(playerJSON.getString("id")).equals(player.getID())) {
                System.out.println("PLAYER UUID MISMATCH WITH SERVER! - CANCELING SYNC!");
                return;
            }

            JSONObject positionJSON = playerJSON.getJSONObject("position");
            updatePlayer(player,
                    playerJSON.getString("color"), playerJSON.getString("name"),
                    playerJSON.getInt("currentCheckpoint"), playerJSON.getBoolean("ready"),
                    positionJSON.getInt("x"), positionJSON.getInt("y"), Heading.valueOf(positionJSON.getString("heading")));

            // Update their deck
            // TODO: only update current player's deck? (local player)
            JSONObject playerDeck = GameService.getPlayerDeck(gameID, player.getID());
            if (playerDeck == null) {
                System.out.println("Could not fetch player deck for " + player.getName());
                continue;
            }

            updatePlayerDeck(player, playerDeck.getInt("energy"), playerDeck.getInt("damage"), playerDeck.getJSONArray("program"), playerDeck.getJSONArray("cards"), playerDeck.getJSONArray("upgrades"));
        }

        board.setPhase(gameState.getEnum(Phase.class, "phase"));
        board.setStep(gameState.getInt("step"));
        board.setCurrentPlayer(board.getPlayer(gameState.getInt("currentPlayer")));
    }

    /**
     * Updates a player.
     *
     * @param player            the player to update
     * @param color             color
     * @param name              name
     * @param currentCheckpoint current checkpoint
     * @param ready             ready status
     * @param x                 x-coordinate
     * @param y                 y-coordinate
     * @param heading           heading
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private void updatePlayer(Player player, String color, String name, int currentCheckpoint, boolean ready, int x, int y, Heading heading) {
        if (player.getColor() == null || !player.getColor().equals(color)) {
            player.setColor(color);
        }
        if (player.getName() == null || !player.getName().equals(name)) {
            player.setName(name);
        }
        if (player.getCurrentCheckpoint() != currentCheckpoint) {
            player.setCurrentCheckpoint(currentCheckpoint);
        }
        if (player.getSpace() == null || player.getSpace().x != x || player.getSpace().y != y) {
            player.setSpace(board.getSpace(x, y));
        }
        if (!player.getHeading().equals(heading)) {
            player.setHeading(heading);
        }
        if (player.isReady() != ready) {
            player.setReady(ready);
        }
    }

    /**
     * Updates a player's deck
     *
     * @param player   the player whoose deck to update
     * @param energy   energy
     * @param damage   damage
     * @param program  program JSON array
     * @param cards    cards JSON array
     * @param upgrades upgrades JSON array
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private void updatePlayerDeck(Player player, int energy, int damage, JSONArray program, JSONArray cards, JSONArray upgrades) {
        player.setEnergy(energy);
        player.setDamage(damage);

        // Program
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CardField field = player.getProgramField(i);
            field.setCard(null);
        }
        for (int i = 0; i < program.length(); i++) {
            JSONObject cardJSON = program.getJSONObject(i);
            Card card;
            switch (cardJSON.getEnum(CardType.class, "type")) {
                case PROGRAM:
                    card = new ProgramCard(Program.valueOf(cardJSON.getString("program")));
                    break;
                case DAMAGE:
                    card = new DamageCard(Damage.valueOf(cardJSON.getString("damage")));
                    break;
                default:
                    continue;
            }
            CardField field = player.getProgramField(i);
            field.setCard(card);
            field.setVisible(cardJSON.getBoolean("visible"));
        }

        // Cards
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CardField field = player.getCardField(i);
            field.setCard(null);
        }
        for (int i = 0; i < cards.length(); i++) {
            JSONObject cardJSON = cards.getJSONObject(i);
            Card card;
            switch (cardJSON.getEnum(CardType.class, "type")) {
                case PROGRAM:
                    card = new ProgramCard(Program.valueOf(cardJSON.getString("program")));
                    break;
                case DAMAGE:
                    card = new DamageCard(Damage.valueOf(cardJSON.getString("damage")));
                    break;
                default:
                    continue;
            }
            CardField field = player.getCardField(i);
            field.setCard(card);
            field.setVisible(cardJSON.getBoolean("visible"));
        }

        // TODO: Upgrades
    }

    /**
     * Moves a {@link Card Card} on a source
     * {@link CardField CommandCardField} to
     * another {@link CardField CommandCardField}.
     * Only moves the card, if there is a card on the source field, and there is no card on the target field.
     *
     * @param source The source {@link CardField CommandCardField}.
     * @param target The {@link CardField CommandCardField} to move the card to.
     * @return <code>true</code> if the card is moved, else <code>false</code>.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     */
    public boolean moveCards(@NotNull CardField source, @NotNull CardField target) {
        Card sourceCard = source.getCard();
        Card targetCard = target.getCard();
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
     *
     * @param player the player that wins the game
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void winTheGame(Player player) {
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
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private void resetGame() {
        // TODO: reset the game with same map and same players
    }
}
