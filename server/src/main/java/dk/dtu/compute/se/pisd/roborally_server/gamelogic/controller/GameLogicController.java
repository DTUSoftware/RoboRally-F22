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
package dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller;

import dk.dtu.compute.se.pisd.roborally_server.model.*;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.ActionElement;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.FieldElement;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.PriorityAntenna;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.Wall;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.*;
import dk.dtu.compute.se.pisd.roborally_server.server.service.GameService;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Controls stuff that happens on the {@link Game Game}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameLogicController {
    /**
     * The game linked to the controller
     */
    private Game game;

    /**
     * The elements on the boards with actions
     **/
    private SortedSet<ActionElement> actionElements = new TreeSet<>();

    /**
     * The GameController constructor.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param game the game class
     */
    public GameLogicController(Game game) {
        this.game = game;
    }

    /**
     * Adds an action element to the game.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param actionElement the element to add
     */
    public void addElement(ActionElement actionElement) {
        actionElements.add(actionElement);
        // System.out.println("Sorted set is: " + actionElements);
    }

    /**
     * Removes an action element from the game.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param actionElement the element to remove
     */
    public void removeElement(ActionElement actionElement) {
        actionElements.remove(actionElement);
    }

    /**
     * Activates all action elements on the game.getGameState().
     * Used after each round of register activations.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void activateElements() {
        String currentType = "  ";

        // the set is sorted by activation sequence
        for (ActionElement actionElement : actionElements) {
            if (!actionElement.getClass().getName().equals(currentType)) {
                currentType = actionElement.getClass().getName();
                // reset every player's moved by action
                for (int i = 0; i < game.getPlayerCount(); i++) {
                    game.getGameState().getPlayer(i).setMovedByAction(false);
                }
            }
            actionElement.activate();
        }
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the game.getGameState(). This method should eventually be deleted!
     * <p>
     * - the current player should be moved to the given space
     * (if it is free()
     * - and the current player should be set to the player
     * following the current player
     * - the counter of moves in the game should be increased by one
     * if the player is moved
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        if (space.free()) {
            space.setPlayer(game.getGameState().getPlayer(game.getGameState().getCurrentPlayer()));
            game.getGameState().endCurrentPlayerTurn();
        }

        if (space != null && space.board == game.getBoard()) {
            Player currentPlayer = game.getGameState().getPlayerCurrent();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int playerNumber = (game.getGameState().getPlayerNumber(currentPlayer) + 1) % game.getPlayerCount();
                game.getGameState().setCurrentPlayer(playerNumber);
            }
        }

    }

    // XXX: V2

    /**
     * Starts the programming phase.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void startProgrammingPhase() {
        game.getGameState().setPhase(Phase.PROGRAMMING);
        game.getGameState().setCurrentPlayer(0);
        game.getGameState().setStep(0);

        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player player = game.getGameState().getPlayer(i);
            if (player != null) {
                player.getDeck().clearProgram();

                player.getDeck().populateCards();
                for (int j = 0; j < PlayerDeck.NO_CARDS; j++) {
                    Card field;

                    if (20 < (int) ((Math.random() * (player.getDeck().getDamage() + 20)) + 1)) {
                        field = generateRandomDamageCard(); //TODO change if cards chance
                    } else {
                        field = generateRandomCommandCard(); //TODO change if cards chance
                    }
                    field.setVisible(true);
                    player.getDeck().setCardField(j, field);
                }
            }
        }
    }

    // XXX: V2

    /**
     * Generates a random command card.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @return the random {@link CommandCard CommandCard}.
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) ((Math.random() * commands.length));
        return new CommandCard(commands[random]);
    }

    /**
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Oscar Maxwell
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @return the random damage card
     */
    private DamageCard generateRandomDamageCard() {
        Damage[] damages = Damage.values();
        int random = (int) ((Math.random() * damages.length));
        return new DamageCard(damages[random]);
    }

    // XXX: V2

    /**
     * Ends the programming phase.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        game.getGameState().setPhase(Phase.ACTIVATION);
        game.getGameState().setCurrentPlayer(0);
        game.getGameState().setStep(0);
    }

    // XXX: V2

    /**
     * Make the programming field visible.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @param register the register to show?
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < PlayerDeck.NO_REGISTERS) {
            for (int i = 0; i < game.getPlayerCount(); i++) {
                Player player = game.getGameState().getPlayer(i);
                Card field = player.getDeck().getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2

    /**
     * Hides the program fields.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player player = game.getGameState().getPlayer(i);
            for (int j = 0; j < PlayerDeck.NO_REGISTERS; j++) {
                Card field = player.getDeck().getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2

    /**
     * Executes programs (disables step mode).
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void executePrograms() {
        game.getGameState().setStepMode(false);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Execute steps (enables step mode).
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void executeStep() {
        game.getGameState().setStepMode(true);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Continue programs.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (game.getGameState().getPhase() == Phase.ACTIVATION && !game.getGameState().isStepMode());
    }

    // XXX: V2

    /**
     * Calculates the distance between two object position
     *
     * @author Mads Nielsen
     * @param pos1 the space of the first object
     * @param pos2 the space of the second object
     * @return the distance
     */
    private double getDistance(Space pos1, Space pos2) {
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
    }

    /**
     * Executes the next step.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     * @author Oscar Maxwell
     */
    // TODO the stuff with the PriorityAntenna
    private void executeNextStep() {
        Player currentPlayer = game.getGameState().getPlayer(game.getGameState().getCurrentPlayer());
        int playerAmmount = game.getPlayerCount();
        ArrayList<Double> playerDistances = new ArrayList<Double>();
        Space prorityantennaPosition = game.getBoard().getPriorityAntennaPosition();
        for (int i = 0; i < playerAmmount; i++) {
            Player player2Check = game.getGameState().getPlayer(i);
            playerDistances.add(getDistance(prorityantennaPosition, player2Check.getSpace()));
        }
        for (int i = 0; i < playerAmmount; i++) {
            for (int j = 0; j < playerAmmount; j++){

            }
        }

        if (game.getGameState().getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = game.getGameState().getStep();
            if (step >= 0 && step < PlayerDeck.NO_REGISTERS) {
                CommandCard card = currentPlayer.getDeck().getProgramField(step);
                if (card != null) {
                    Command command = card.getCommand();
                    if (card.getCommand().isInteractive()) {
                        game.getGameState().setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }

                int nextPlayerNumber = game.getGameState().getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < game.getPlayerCount()) {
                    game.getGameState().setCurrentPlayer(nextPlayerNumber);
                } else {
                    // Activate all elements on the board
                    activateElements();

                    step++;
                    if (step < PlayerDeck.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        game.getGameState().setStep(step);
                        game.getGameState().setCurrentPlayer(0);
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
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @param player  the player to execute the command on
     * @param command the command to execute
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && game.hasPlayer(player) && command != null) {
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
//                case POWER_UP:
//                    this.powerUp(player);
//                    break;
//                case AGAIN:
//                    this.again(player);
//                    break;
//                case SPAM:
//                    this.SPAM(player);
//                    player.removeDamage();
//                    break;
//                case TROJAN_HORSE:
//                    this.TROJAN_HORSE(player);
//                    player.removeDamage();
//                    break;
//                case WORM:
//                    this.WORM(player);
//                    player.removeDamage();
//                    break;
//                case VIRUS:
//                    this.VIRUS(player);
//                    player.removeDamage();
//                    break;
//                case ENERGY_ROUTINE:
//                    this.powerUp(player);
//                    break;
//                case SANDBOX_ROUTINE:
//                    this.sandboxRoutine(player, command);
//                    break;
//                case WEASEL_ROUTINE:
//                    this.optionLeftRight(player, command);
//                    break;
//                case SPEED_ROUTINE:
//                    this.fastfastForward(player);
//                    break;
//                case SPAM_FOLDER:
//                    player.removeDamage();
//                    break;
//                case REPEAT_ROUTINE:
//                    this.again(player);
//                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Checks if a player can move in a certain direction.
     * For example, you cannot move from a field into another field, if there is a wall.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     * @author Oscar Maxwell
     * @param player The player to check if they can move.
     */
    private boolean canMove(@NotNull Player player, Heading direction) {
        Space space = player.getSpace();
        boolean canMove = false;
        if (player != null && game.hasPlayer(player) && space != null) {
            Space target = game.getBoard().getNeighbour(space, direction);
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
                player.reboot();
            }
        }
        return canMove;
    }

    /**
     * Moves the player backwards.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param player The player to move backwards.
     */
    public void moveBackwards(@NotNull Player player) {
        if (player != null && game.hasPlayer(player)) {
            Heading heading = player.getHeading().next().next();

            moveDirection(player, heading);
        }
    }

    /**
     * Moves a player in a certain direction WITHOUT CHANGING THE PLAYER'S HEADING.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     * @author Oscar Maxwell
     * @param player    the player
     * @param direction the direction
     */
    public void moveDirection(@NotNull Player player, Heading direction) {
        Space space = player.getSpace();
        if (player != null && game.hasPlayer(player) && space != null && canMove(player, direction)) {
            Space target = game.getBoard().getNeighbour(space, direction);
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
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param player The player to move forward.
     */
    public void moveForward(@NotNull Player player) {
        if (player != null && game.hasPlayer(player)) {
            Heading heading = player.getHeading();
            moveDirection(player, heading);
        }
    }

    /**
     * Move forward an x amount of times.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param player The player to fast-forward.
     */
    public void fastForward(@NotNull Player player) {
        forwardX(player, 2);
    }

    /**
     * the fast fast forward card
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param player the player to move
     */
    public void fastfastForward(@NotNull Player player) {
        forwardX(player, 3);
    }

    /**
     * Turns the player right.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @param player The player to turn right.
     */
    public void turnRight(@NotNull Player player) {
        if (player != null && game.hasPlayer(player)) {
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * Turns the player left.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @param player The player to turn left.
     */
    public void turnLeft(@NotNull Player player) {
        if (player != null && game.hasPlayer(player)) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * if you want to turn left or right
     *
     * @author Oscar Maxwell
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

    /**
     * @author Nicolai Udbye
     * @param player
     * @param command
     */
    public void sandboxRoutine(@NotNull Player player, Command command) {

        switch (command) {
            case MOVE_1:
                this.moveForward(player);
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
            case LEFT:
                this.turnLeft(player);
                break;
            case RIGHT:
                this.turnRight(player);
                break;
            case U_TURN:
                this.turnRight(player);
                this.turnRight(player);
                break;
        }
    }

    /**
     * @author Oscar Maxwell
     * @param player
     */
    public void powerUp (@NotNull Player player) {
        player.getDeck().addEnergy(1);
    }

    public void again (@NotNull Player player) {
        int step = game.getGameState().getStep();
        if (step <= 0) {
            return;
        }
        CommandCard card = player.getDeck().getProgramField(step - 1);
        if (card != null) {
            Command command = card.getCommand();
            if (command.isInteractive()) {
                game.getGameState().setPhase(Phase.PLAYER_INTERACTION);
                return;
            }
            executeCommand(player, command);
        }
    }

    /**
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @param player
     */
    public void SPAM (@NotNull Player player) {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * 9);  //commands[8] = SPAM Card //TODO Change of cards chance
        executeCommand(player, commands[random]);
    }

    public void TROJAN_HORSE(@NotNull Player player) {

        for (int i = 0; i < 2; i++)
            SPAM(player);
    }

    public void WORM(@NotNull Player player) {
        player.reboot();
    }

    public void VIRUS(@NotNull Player player) {
        Command[] commands = Command.values();
        Space playerSpace = player.getSpace();

        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player checkPlayer = game.getGameState().getPlayer(i);
            Space checkPlayerSpace = checkPlayer.getSpace();

            if (getDistance(playerSpace, checkPlayerSpace) > 0 && getDistance(playerSpace, checkPlayerSpace) < 6) {
                checkPlayer.getDeck().takeDamage();
            }
        }

        int random = (int) (Math.random() * 8);  //commands[8] = SPAM Card
        executeCommand(player, commands[random]);
    }


    /**
     * Moves a {@link Command Command} on a source {@link Card Card} to another {@link Card Card}.
     * Only moves the card, if there is a card on the source field, and there is no card on the target field.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     * @param source The source {@link Card card}.
     * @param target The {@link Card Card} to move the card to.
     * @return <code>true</code> if the card is moved, else <code>false</code>.
     */
    public boolean moveCards(@NotNull Card source, @NotNull Card target) {
        if (source.getType() != null && source.getType().equals(target.getType())) {
            switch (source.getType()) {
                case COMMAND:
                    Command sourceCommand = ((CommandCard) source).getCommand();
                    Command targetCommand = ((CommandCard) target).getCommand();
                    if (sourceCommand != null && targetCommand == null) {
                        ((CommandCard) target).setCommand(sourceCommand);
                        ((CommandCard) source).setCommand(null);
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     *
     * @author Oscar Maxwell
     * @param cardOptions the card used
     */
    public void executeCommandOptionAndContinue(Command cardOptions) {

        game.getGameState().setPhase(Phase.ACTIVATION);
        Player currentPlayer = game.getGameState().getPlayerCurrent();
        executeCommand(game.getGameState().getPlayerCurrent(), cardOptions);

        int step = game.getGameState().getStep();
        if (step >= 0 && step < PlayerDeck.NO_REGISTERS) {
            int nextPlayerNumber = game.getGameState().getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < game.getPlayerCount()) {
                game.getGameState().setCurrentPlayer(nextPlayerNumber);
            } else {
                step++;
                if (step < PlayerDeck.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    game.getGameState().setStep(step);
                    game.getGameState().setCurrentPlayer(0);
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
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Hansen
     * @author Mads Nielsen
     * @param player the player that wins the game
     */
    public void winTheGame(Player player) {
        // TODO: send won over server before killing
        if (game != null) {
            GameService gameService = new GameService();
            gameService.deleteGameByID(game.getID());
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
