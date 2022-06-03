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
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

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

    final private RoboRally roboRally;

    /**
     * The elements on the boards with actions
     **/
    private SortedSet<ActionElement> actionElements = new TreeSet<>();

    /**
     * The GameController constructor.
     *
     * @param roboRally the roborally class
     * @param board     the board to control.
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

    /**
     * Adds an action element to the game.
     *
     * @param actionElement the element to add
     */
    public void addElement(ActionElement actionElement) {
        actionElements.add(actionElement);
        // System.out.println("Sorted set is: " + actionElements);
    }

    /**
     * Removes an action element from the game.
     *
     * @param actionElement the element to remove
     */
    public void removeElement(ActionElement actionElement) {
        actionElements.remove(actionElement);
    }

    /**
     * Activates all action elements on the board.
     * Used after each round of register activations.
     */
    public void activateElements() {
        String currentType = "  ";

        // the set is sorted by activation sequence
        for (ActionElement actionElement : actionElements) {
            if (!actionElement.getClass().getName().equals(currentType)) {
                currentType = actionElement.getClass().getName();
                // reset every player's moved by action
                for (int i = 0; i < board.getPlayersNumber(); i++) {
                    board.getPlayer(i).setMovedByAction(false);
                }
            }
            actionElement.activate();
        }
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

                for (int j = 0; j < Player.NO_COMMAND_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    
                    if (20 < (int) ((Math.random() * (player.getDamage() + 20)) + 1)){

                        field.setCard(generateRandomDamageCard(8, 11));
                        field.setVisible(true);
                    } else {
                        field.setCard(generateRandomCommandCard(0, 7));
                        field.setVisible(true);
                    }
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
    private CommandCard generateRandomCommandCard(int from, int to) {
        Command[] commands = Command.values();
        int random = (int) ((Math.random() * (to + 1 - from)) + from);
        return new CommandCard(commands[random]);
    }

    private CommandCard generateRandomDamageCard(int from, int to) {
        Command[] commands = Command.values();
        int random = (int) ((Math.random() * (to + 1 - from)) + from);
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

    // XXX: V2

    /**
     * Executes programs (disables step mode).
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Execute steps (enables step mode).
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Continue programs.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2

    /**
     * Calculates the distance between two object position
     *
     * @param pos1 the space of the first object
     * @param pos2 the space of the second object
     * @return the distance
     */
    private double getDistance(Space pos1, Space pos2) {
        return Math.sqrt(Math.pow(pos1.x-pos2.x,2) + Math.pow(pos1.y - pos2.y,2));
    }

    /**
     * Executes the next step.
     */
    // TODO the stuff with the PriorityAntenna
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (card.command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }

                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    // Activate all elements on the board
                    activateElements();

                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    // XXX: V2

    /**
     * Executes a command.
     *
     * @param player  the player to execute the command on
     * @param command the command to execute
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case MOVE_1:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case MOVE_2:
                    this.fastForward(player);
                    break;
                case MOVE_3:
                    this.fastfastForward(player);
                    break;
                case MOVE_BACKWARDS:
                    this.moveBackwards(player);
                    break;
                case U_TURN:
                    this.turnRight(player);
                    this.turnRight(player);
                    break;
                case OPTION_LEFT_RIGHT:
                    this.optionLeftRight(player, command);
                    break;
                case SPAM:
                    this.SPAM(player);
                    player.removeDamage();
                    break;
                case TROJAN_HORSE:
                    this.TROJAN_HORSE(player);
                    player.removeDamage();
                    break;
                case WORM:
                    this.WORM(player);
                    player.removeDamage();
                    break;
                case VIRUS:
                    this.VIRUS(player);
                    player.removeDamage();
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Checks if a player can move in a certain direction.
     * For example, you cannot move from a field into another field, if there is a wall.
     *
     * @param player The player to check if they can move.
     */
    private boolean canMove(@NotNull Player player, Heading direction) {
        Space space = player.getSpace();
        boolean canMove = false;
        if (player != null && player.board == board && space != null) {
            Space target = board.getNeighbour(space, direction);
            if (target != null) {
                canMove = true;
                for (FieldElement fieldElement : space.getFieldObjects()) {
                    if (fieldElement instanceof PriorityAntenna) {
                        canMove = false;
                    }
                    if (fieldElement instanceof Wall) {
                        if (((Wall) fieldElement).getDirection() == direction) {
                            canMove = false;
                        }
                    }
                }
                if (canMove) {
                    for (FieldElement fieldElement : target.getFieldObjects()) {
                        if (fieldElement instanceof Wall) {
                            if (((Wall) fieldElement).getDirection().next().next() == direction) {
                                canMove = false;
                            }
                        }
                    }
                }
            } else {
                player.takeDamage();
                player.reboot();
            }
        }
        return canMove;
    }

    /**
     * Moves the player backwards.
     *
     * @param player The player to move backwards.
     */
    public void moveBackwards(@NotNull Player player) {
        if (player != null && player.board == board) {
            Heading heading = player.getHeading().next().next();

            moveDirection(player, heading);
        }
    }

    /**
     * Moves a player in a certain direction WITHOUT CHANGING THE PLAYER'S HEADING.
     *
     * @param player    the player
     * @param direction the direction
     */
    public void moveDirection(@NotNull Player player, Heading direction) {
        Space space = player.getSpace();
        if (player != null && player.board == board && space != null && canMove(player, direction)) {
            Space target = board.getNeighbour(space, direction);
            if (target != null) {
                // when there is another player on the target. The other player is pushed away!
                if (target.free()) {
                    target.setPlayer(player);
                } else {
                    // check that we aren't trying to move the other player through a wall
                    Player otherPlayer = target.getPlayer();
                    if (canMove(otherPlayer, direction)) {
                        moveDirection(target.getPlayer(), direction);
                        target.setPlayer(player);
                    }
                }
            } else {
                player.takeDamage();
                player.reboot();
            }
        }
    }
    /*
    class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }
    */


    /**
     * Moves the player forward, with the current heading.
     *
     * @param player The player to move forward.
     */
    public void moveForward(@NotNull Player player) {
        if (player != null && player.board == board) {
            Heading heading = player.getHeading();
            moveDirection(player, heading);
        }
    }

    /**
     * Move forward an x amount of times.
     *
     * @param player the player to move forward
     * @param times  the amount of times to move forward
     */
    public void forwardX(@NotNull Player player, int times) {
        for (int i = times; i > 0; i--) {
            moveForward(player);
        }
    }

    /**
     * Move in a certain direction an x amount of times.
     *
     * @param player    the player to move
     * @param direction the direction to move
     * @param times     the amount of times to move
     */
    public void moveDirectionX(@NotNull Player player, Heading direction, int times) {
        for (int i = times; i > 0; i--) {
            moveDirection(player, direction);
        }
    }

    /**
     * Fast forwards (the card).
     *
     * @param player The player to fast-forward.
     */
    public void fastForward(@NotNull Player player) {
        forwardX(player, 2);
    }

    /**
     * the fast fast forward card
     *
     * @param player the player to move
     */
    public void fastfastForward(@NotNull Player player) {
        forwardX(player, 3);
    }

    /**
     * Turns the player right.
     *
     * @param player The player to turn right.
     */
    public void turnRight(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * Turns the player left.
     *
     * @param player The player to turn left.
     */
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * if you want to turn left or right
     *
     * @param player  the player to turn
     * @param command to go left or right
     */
    public void optionLeftRight(@NotNull Player player, Command command) {
        if (command.equals("LEFT")) {
            turnLeft(player);
        } else if (command.equals("RIGHT")) {
            turnRight(player);
        }
    }

    public void SPAM (@NotNull Player player) {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * 8);  //commands[8] = SPAM Card
        executeCommand(player, commands[random]);
    }

    public void TROJAN_HORSE (@NotNull Player player) {

        for ( int i = 0 ; i < 2 ; i++)
        SPAM(player);
    }

    public void WORM (@NotNull Player player) {
        player.reboot();
    }

    public void VIRUS (@NotNull Player player) {
        Command[] commands = Command.values();
        Space playerSpace = player.getSpace();

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player checkPlayer = board.getPlayer(i);
            Space checkPlayerSpace = checkPlayer.getSpace();

            if (getDistance(playerSpace, checkPlayerSpace) > 0 && getDistance(playerSpace, checkPlayerSpace) < 6) {
                checkPlayer.takeDamage();
            }
        }

        int random = (int) (Math.random() * 8);  //commands[8] = SPAM Card
        executeCommand(player, commands[random]);
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
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     *
     * @param cardOptions the card used
     */
    public void executeCommandOptionAndContinue(Command cardOptions) {

        board.setPhase(Phase.ACTIVATION);
        Player currentPlayer = board.getCurrentPlayer();
        executeCommand(board.getCurrentPlayer(), cardOptions);

        int step = board.getStep();
        if (step >= 0 && step < Player.NO_REGISTERS) {
            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                step++;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else {
                    startProgrammingPhase();
                }
            }
            continuePrograms();

        }

    }

    /**
     * wins the game
     *
     * @param player the player that wins the game
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
     */
    private void resetGame() {
        // TODO: reset the game with same map and same players
    }

}
