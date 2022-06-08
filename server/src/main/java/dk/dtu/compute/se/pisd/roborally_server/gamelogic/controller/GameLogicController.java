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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Controls stuff that happens on the {@link Game Game}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class GameLogicController {
    /**
     * The game linked to the controller
     */
    private Game game;

    /** The logger used to log game-related stuff */
    private final Logger logger = LoggerFactory.getLogger(GameLogicController.class);

    /**
     * The elements on the boards with actions
     **/
    private SortedSet<ActionElement> actionElements = new TreeSet<>();

    /**
     * I don't care about this little shitty part of memory used to store this
     */
    private String cardOption = null;

    /** Whether to stop movement of current player, for example on reboot */
    private boolean stopMovement = false;

    /**
     * Queue of players for step
     */
    ArrayDeque<Integer> playerQueue = new ArrayDeque<>();

    /**
     * The GameController constructor.
     *
     * @param game the game class
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public GameLogicController(Game game) {
        this.game = game;
    }

    /**
     * Adds an action element to the game.
     *
     * @param actionElement the element to add
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void addElement(ActionElement actionElement) {
        actionElements.add(actionElement);
        // System.out.println("Sorted set is: " + actionElements);
    }

    /**
     * Removes an action element from the game.
     *
     * @param actionElement the element to remove
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @param space the space to which the current player should move
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @return the random {@link ProgramCard CommandCard}.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private ProgramCard generateRandomCommandCard() {
        Program[] programs = Program.values();
        int random = (int) ((Math.random() * programs.length));
        return new ProgramCard(programs[random]);
    }

    /**
     * @return the random damage card
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Oscar Maxwell
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @param register the register to show?
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < PlayerDeck.NO_REGISTERS) {
            for (int i = 0; i < game.getPlayerCount(); i++) {
                Player player = game.getGameState().getPlayer(i);
                Card field = player.getDeck().getProgramField(register);
                if (field != null) {
                    field.setVisible(true);
                }
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
                if (field != null) {
                    field.setVisible(false);
                }
            }
        }
    }

    // XXX: V2

    /**
     * Executes programs (disables step mode).
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void executePrograms() {
        game.getGameState().setStepMode(false);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Execute steps (enables step mode).
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void executeStep() {
        game.getGameState().setStepMode(true);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Continue programs, and sleep after each step to allow the clients to watch the steps.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private void continuePrograms() {
        do {
            executeNextStep();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (game.getGameState().getPhase() == Phase.ACTIVATION && !game.getGameState().isStepMode());
    }

    // XXX: V2

    /**
     * Calculates the distance between two object position
     *
     * @param pos1 the space of the first object
     * @param pos2 the space of the second object
     * @return the distance
     * @author Mads Nielsen
     */
    private double getDistance(Space pos1, Space pos2) {
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
    }

    /**
     * Choose the order of the players, depending on priority antenna location and player locations.
     *
     * @author Mads Nielsen
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private void priorityAntennaMath() {
        int playerAmount = game.getPlayerCount();
        double[] playerDistances = new double[playerAmount];
        Space priorityAntennaPosition = game.getBoard().getPriorityAntennaPosition();
        for (int i = 0; i < playerAmount; i++) {
            Player player2Check = game.getGameState().getPlayer(i);
            playerDistances[i] = getDistance(priorityAntennaPosition, player2Check.getSpace());
        }
        double smallestdistance;
        int playerSmallestdistance = 0;
        Integer[] playerMoveOrder = new Integer[playerAmount];
        for (int i = 0; i < playerAmount; i++) {
            smallestdistance = Double.MAX_VALUE;
            for (int j = 0; j < playerAmount; j++) {
                if (smallestdistance > playerDistances[j]) {
                    smallestdistance = playerDistances[j];
                    playerSmallestdistance = j;
                } else if (smallestdistance == playerDistances[j]) {
                    if ((priorityAntennaPosition.x - game.getGameState().getPlayer(j).getSpace().x) < (priorityAntennaPosition.x - game.getGameState().getPlayer(playerSmallestdistance).getSpace().x)) {
                        smallestdistance = playerDistances[j];
                        playerSmallestdistance = j;
                    }
                }
            }
            playerDistances[playerSmallestdistance] = Double.MAX_VALUE;
            playerMoveOrder[i] = playerSmallestdistance;
        }
        // Fill up the queue from the beginning (playerMoveOrder is made that way)
        for (int i = 0; i < playerAmount; i++) {
            playerQueue.add(playerMoveOrder[i]);
        }
    }

    /**
     * Executes the next step in accordance with priority antenna order.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     * @author Oscar Maxwell
     */
    private void executeNextStep() {
        // ======== Priority Antenna choosing ======
        if (playerQueue.isEmpty()) {
            priorityAntennaMath();
        }
        game.getGameState().setCurrentPlayer(playerQueue.remove());
//        System.out.println("Step: " + game.getGameState().getStep());
//        System.out.println("Current player: " + game.getGameState().getCurrentPlayer());
        Player currentPlayer = game.getGameState().getPlayer(game.getGameState().getCurrentPlayer());

        // Do the activation
        if (game.getGameState().getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = game.getGameState().getStep();
            if (step >= 0 && step < PlayerDeck.NO_REGISTERS) {
                Card card = currentPlayer.getDeck().getProgramField(step);
                if (card != null) {
                    switch (card.getType()) {
                        case PROGRAM:
                            Program program = ((ProgramCard) card).getProgram();
                            executeProgram(currentPlayer, program);
                            break;
                        case DAMAGE:
                            Damage damage = ((DamageCard) card).getDamage();
                            executeDamage(currentPlayer, damage);
                            break;
                    }
                    this.cardOption = null;
                }

                if (playerQueue.isEmpty()) {
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
     * @param player  the player to execute the command on
     * @param program the command to execute
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    private void executeProgram(@NotNull Player player, Program program) {
        if (player != null && game.hasPlayer(player) && program != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            debug("Executing " + program.name() + " on player: " + player.getName());

            if (program.isInteractive()) {
                game.getGameState().setPhase(Phase.PLAYER_INTERACTION);
                this.cardOption = null;
                while (game.getGameState().getPhase() == Phase.PLAYER_INTERACTION || this.cardOption == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // get card from option
                try {
                    program = Program.valueOf(cardOption);
                    debug("Changing execution to " + program.name() + " on player: " + player.getName());
                    this.cardOption = null;
                } catch (IllegalArgumentException e) {
                    try {
                        Damage damage_check = Damage.valueOf(cardOption);
                        debug("Card chosen is damage, changing... On player: " + player.getName());
                        executeDamage(player, damage_check);
                    } catch (IllegalArgumentException e2) {
                        e.printStackTrace();
                    }
                    this.cardOption = null;
                    return;
                }
            }

            switch (program) {
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
                case ENERGY_ROUTINE:
                    this.powerUp(player);
                    break;
                case SANDBOX_ROUTINE:
                    this.sandboxRoutine(player, program);
                    break;
                case WEASEL_ROUTINE:
                    this.optionLeftRight(player, program);
                    break;
                case SPEED_ROUTINE:
                    this.fastfastForward(player);
                    break;
                case POWER_UP:
                    this.powerUp(player);
                    break;
                case AGAIN:
                    this.again(player, game.getGameState().getStep());
                    break;
                case REPEAT_ROUTINE:
                    this.again(player, game.getGameState().getStep());
                    break;
                case SPAM_FOLDER:
                    player.getDeck().removeDamage();
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Executes damage.
     *
     * @param player the player to execute the command on
     * @param damage the damage to execute
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    private void executeDamage(@NotNull Player player, Damage damage) {
        if (player != null && game.hasPlayer(player) && damage != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            debug("Executing " + damage.name() + " on player: " + player.getName());

            if (damage.isInteractive()) {
                game.getGameState().setPhase(Phase.PLAYER_INTERACTION);
                this.cardOption = null;
                while (game.getGameState().getPhase() == Phase.PLAYER_INTERACTION || this.cardOption == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // get card from option
                try {
                    damage = Damage.valueOf(cardOption);
                    debug("Changing execution to " + damage.name() + " on player: " + player.getName());
                    this.cardOption = null;
                } catch (IllegalArgumentException e) {
                    try {
                        Program program_check = Program.valueOf(cardOption);
                        debug("Card chosen is program, changing... On player: " + player.getName());
                        executeProgram(player, program_check);
                    } catch (IllegalArgumentException e2) {
                        e.printStackTrace();
                    }
                    this.cardOption = null;
                    return;
                }
            }

            switch (damage) {
                case SPAM:
                    this.SPAM(player);
                    player.getDeck().removeDamage();
                    break;
                case TROJAN_HORSE:
                    this.TROJAN_HORSE(player);
                    player.getDeck().removeDamage();
                    break;
                case WORM:
                    this.WORM(player);
                    player.getDeck().removeDamage();
                    break;
                case VIRUS:
                    this.VIRUS(player);
                    player.getDeck().removeDamage();
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
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     * @author Oscar Maxwell
     */
    private boolean canMove(@NotNull Player player, Heading direction) {
        Space space = player.getSpace();
        boolean canMove = false;
        if (player != null && game.hasPlayer(player) && space != null) {
            Space target = game.getBoard().getNeighbour(space, direction);
            if (target != null) {
                canMove = true;
                for (FieldElement fieldElement : space.getFieldObjects()) {
                    if (fieldElement instanceof Wall) {
                        if (((Wall) fieldElement).getDirection() == direction) {
                            canMove = false;
                        }
                    }
                }
                if (canMove) {
                    for (FieldElement fieldElement : target.getFieldObjects()) {
                        if (fieldElement instanceof PriorityAntenna) {
                            canMove = false;
                        }
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
     * @param player The player to move backwards.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
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
     * @param player    the player
     * @param direction the direction
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     * @author Oscar Maxwell
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
                space.setPlayer(null);
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
     * @param player The player to move forward.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void moveForward(@NotNull Player player) {
        forwardX(player, 1);
    }

    /**
     * Move forward an x amount of times.
     *
     * @param player the player to move forward
     * @param times  the amount of times to move forward
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void forwardX(@NotNull Player player, int times) {
        if (player != null && game.hasPlayer(player)) {
            moveDirectionX(player, player.getHeading(), times);
        }
    }

    /**
     * Move in a certain direction an x amount of times.
     *
     * @param player    the player to move
     * @param direction the direction to move
     * @param times     the amount of times to move
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void moveDirectionX(@NotNull Player player, Heading direction, int times) {
        stopMovement = false;
        for (int i = times; i > 0; i--) {
            if (stopMovement) {
                break;
            }
            moveDirection(player, direction);
        }
    }

    /**
     * Fast forwards (the card).
     *
     * @param player The player to fast-forward.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void fastForward(@NotNull Player player) {
        forwardX(player, 2);
    }

    /**
     * the fast fast forward card
     *
     * @param player the player to move
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void fastfastForward(@NotNull Player player) {
        forwardX(player, 3);
    }

    /**
     * Turns the player right.
     *
     * @param player The player to turn right.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void turnRight(@NotNull Player player) {
        if (player != null && game.hasPlayer(player)) {
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * Turns the player left.
     *
     * @param player The player to turn left.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    public void turnLeft(@NotNull Player player) {
        if (player != null && game.hasPlayer(player)) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * if you want to turn left or right
     *
     * @param player  the player to turn
     * @param program to go left or right
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void optionLeftRight(@NotNull Player player, Program program) {
        if (program.equals(Program.LEFT)) {
            turnLeft(player);
        } else if (program.equals(Program.RIGHT)) {
            turnRight(player);
        }
    }

    /**
     * Does the sandbox routine
     * @param player the player to affect
     * @param program the routine that was chosen
     * @author Nicolai Udbye
     */
    public void sandboxRoutine(@NotNull Player player, Program program) {
        switch (program) {
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
     * Power up card command
     * @param player the player to power up
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void powerUp(@NotNull Player player) {
        player.getDeck().addEnergy(1);
    }

    /**
     * executes the last command again
     * @param player the player
     * @param step which step the again card is on (is used if multiple again cards have been placed next to each other)
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void again(@NotNull Player player, int step) {
        if (step <= 0) {
            return;
        }
        Card card = player.getDeck().getProgramField(step - 1);
        if (card != null) {
            switch (card.getType()) {
                case PROGRAM:
                    Program program = ((ProgramCard) card).getProgram();

                    debug("Repeating " + program.name());

                    if (program == Program.AGAIN || program == Program.REPEAT_ROUTINE) {
                        again(player, step - 1);
                    } else {
                        executeProgram(player, program);
                    }
                    break;
                case DAMAGE:
                    Damage damage = ((DamageCard) card).getDamage();
                    executeDamage(player, damage);
                    break;
            }
        }
    }

    /**
     * Executes a SPAM card
     * @param player the player to take SPAM
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void SPAM(@NotNull Player player) {
        Program[] programs = Program.values();
        int random = (int) (Math.random() * 9);  //commands[8] = SPAM Card //TODO Change of cards chance
        executeProgram(player, programs[random]);
    }

    /**
     * Executes a trojan horse
     * @param player the player
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void TROJAN_HORSE(@NotNull Player player) {
        for (int i = 0; i < 2; i++)
            SPAM(player);
    }

    /**
     * Does a worm on player
     * @param player the player
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void WORM(@NotNull Player player) {
        player.reboot();
    }

    /**
     * Gives the player a virus
     * @param player the player
     * @author Oscar Maxwell
     * @author Nicolai Udbye
     */
    public void VIRUS(@NotNull Player player) {
        Program[] programs = Program.values();
        Space playerSpace = player.getSpace();

        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player checkPlayer = game.getGameState().getPlayer(i);
            Space checkPlayerSpace = checkPlayer.getSpace();

            if (getDistance(playerSpace, checkPlayerSpace) > 0 && getDistance(playerSpace, checkPlayerSpace) < 6) {
                checkPlayer.getDeck().takeDamage();
            }
        }

        int random = (int) (Math.random() * 8);  //commands[8] = SPAM Card
        executeProgram(player, programs[random]);
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     *
     * @param cardOption the card used
     * @return whether the option was set successfully
     * @author Oscar Maxwell
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean setCommandCardOptionAndContinue(String cardOption) {
        if (!cardOption.isEmpty()) {
            // TODO: Verify cardOption - right now a player can execute any card they want through the API
            this.cardOption = cardOption;
            game.getGameState().setPhase(Phase.ACTIVATION);
            return true;
        }
        return false;
    }

    /**
     * wins the game
     *
     * @param player the player that wins the game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Hansen
     * @author Mads Nielsen
     */
    public void winTheGame(Player player) {
        if (game != null) {
            game.getGameState().setPhase(Phase.GAME_WON);
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

    /**
     * Gets the game
     * @return the game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game
     * @param game the game to set
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Stops current player's movement.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void stopMovement() {
        debug("Stopping player movement for current player!");
        this.stopMovement = true;
    }

    /**
     * Debug log function.
     *
     * @param message the message to log
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void debug(String message) {
        logger.info(game.getID()+" - " + message);
    }
}
