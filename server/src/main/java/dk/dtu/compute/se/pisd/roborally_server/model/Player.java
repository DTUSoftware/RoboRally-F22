package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.FieldElement;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.RebootToken;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.SpawnGear;
import dk.dtu.compute.se.pisd.roborally_server.server.service.GameService;

import java.util.UUID;

/**
 * A Player.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class Player {
    private UUID id;
    private String name;
    private String color;
    private int currentCheckpoint;
    private Position position;
    private boolean ready;

    private PlayerDeck deck;
    private UUID gameID;
    private SpawnGear spawnGear;
    private boolean movedByAction = false;

    /**
     * The player's position.
     * Using inner-class due to wanting to return it like this with the API.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    class Position {
        private int x;
        private int y;
        private Heading heading;

        /**
         * Creates a new position with x, y and heading.
         *
         * @param x       x-coordinate
         * @param y       y-coordinate
         * @param heading heading
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        Position(int x, int y, Heading heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }

        /**
         * Creates a new position in origo.
         *
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        Position() {
            this(0, 0, null);
        }

        /**
         * Gets X-coordinate.
         *
         * @return x-coordinate
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        public int getX() {
            return x;
        }

        /**
         * Sets x
         *
         * @param x x-coordinate
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * Gets y-coordinate
         *
         * @return y-coordinate
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        public int getY() {
            return y;
        }

        /**
         * Sets y-coordinate
         *
         * @param y y-coordinate
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        public void setY(int y) {
            this.y = y;
        }

        /**
         * gets heading
         *
         * @return heading
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        public Heading getHeading() {
            return heading;
        }

        /**
         * Sets heading
         *
         * @param heading heading
         * @author Marcus Sand, mwasa@dtu.dk (s215827)
         */
        public void setHeading(Heading heading) {
            this.heading = heading;
        }
    }

    /**
     * Creates a new player with UUID id, and in game with game ID
     *
     * @param id     playerID
     * @param gameID gameID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Player(UUID id, UUID gameID) {
        this.id = id;
        this.gameID = gameID;
        this.position = new Position();
        this.deck = new PlayerDeck();
        this.ready = false;
    }

    /**
     * Creates a plyer in game with gameID, with playerID, and with name.
     *
     * @param id     playerID
     * @param gameID gameID
     * @param name   name of player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Player(UUID id, UUID gameID, String name) {
        this(id, gameID);
        this.name = name;
    }

    /**
     * gets ID of player
     *
     * @return ID
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public UUID getID() {
        return id;
    }

    /**
     * Sets the player's ID.
     * ONLY USE FROM GAME! (can break server and client)
     *
     * @param id UUID of player.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setID(UUID id) {
        this.id = id;
    }

    /**
     * Gets name of player.
     *
     * @return name of player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of player
     *
     * @param name name of player
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets color of player.
     *
     * @return player color
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets player color
     *
     * @param color player color
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets current player checkpoint
     *
     * @return current checkpoint
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     */
    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    /**
     * Sets current checkpoint
     *
     * @param currentCheckpoint current checkpoint
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Mads Nielsen
     */
    public void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    /**
     * Gets position of player.
     *
     * @return player position
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets position of player.
     *
     * @param position the position
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Sets position using x, y and heading
     *
     * @param x       x-coordinates
     * @param y       y-coordinates
     * @param heading heading
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setPosition(int x, int y, Heading heading) {
        if (this.position == null) {
            this.position = new Position();
        }
        this.position.setX(x);
        this.position.setY(y);
        this.position.setHeading(heading);
    }

    /**
     * Sets the position using x and y-coordinates.
     * Requires position to not be null (player exisiting position).
     *
     * @param x x-coordinates
     * @param y y-coordinates
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setPosition(int x, int y) {
        if (this.position != null) {
            this.position.setX(x);
            this.position.setY(y);
        }
    }

    /**
     * Set player space (by changing position).
     *
     * @param space the space
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setSpace(Space space) {
        if (space != null) {
            this.position.setX(space.x);
            this.position.setY(space.y);
        }
    }

    /**
     * Gets a space the player is on.
     *
     * @return the space
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Space getSpace() {
        return getGame().getBoard().getSpace(position.getX(), position.getY());
    }

    /**
     * Sets the player heading.
     *
     * @param direction the heading
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setHeading(Heading direction) {
        this.position.setHeading(direction);
    }

    /**
     * Gets player spawngear
     *
     * @return the spawngear
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public SpawnGear getStartGear() {
        return spawnGear;
    }

    /**
     * Sets spawngear
     *
     * @param spawnGear spawngear
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setStartGear(SpawnGear spawnGear) {
        this.spawnGear = spawnGear;
    }

    /**
     * Gets heading
     *
     * @return heading
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Heading getHeading() {
        return this.position.getHeading();
    }

    /**
     * gets player deck
     *
     * @return the PlayerDeck
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public PlayerDeck getDeck() {
        return this.deck;
    }

    /**
     * Set player deck
     *
     * @param deck the deck
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setDeck(PlayerDeck deck) {
        this.deck = deck;
    }

    /**
     * Gets whether the player is ready or not (for example, done programming).
     *
     * @return whether the player is ready or not.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public boolean getReady() {
        return ready;
    }

    /**
     * Sets ready.
     *
     * @param ready ready or not, here I come
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * Returns whether the player has moved or not for this action.
     *
     * @return whether they have moved or not
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public boolean isMovedByAction() {
        return movedByAction;
    }

    /**
     * Sets the player bool of whether the player has moved from this action.
     *
     * @param movedByAction whether they have moved
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setMovedByAction(boolean movedByAction) {
        this.movedByAction = movedByAction;
    }

    /**
     * Get player game (by making call through GameService).
     *
     * @return game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @JsonIgnore
    public Game getGame() {
        return (new GameService()).getGameByID(gameID);
    }

    /**
     * Sets the player game. (Does NOT update in GameService).
     *
     * @param gameID ID of game
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setGame(UUID gameID) {
        this.gameID = gameID;
    }

    /**
     * Reboot/respawn the player.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     */
    public void reboot() {
        getGame().getGameLogicController().debug("Respawning " + getName());
        getDeck().clearProgram();
        getGame().getGameLogicController().stopMovement();

        for (int i = 0; i < 2; i++) {
            this.getDeck().takeDamage();
        }

        RebootToken[] rebootTokens = getGame().getBoard().getRebootTokens();
        RebootToken rebootToken = null;
        for (RebootToken rebootToken1 : rebootTokens) {
            if (rebootToken1.isWithinBounds(getSpace())) {
                rebootToken = rebootToken1;
                break;
            }
        }

        if (rebootToken != null) {
            rebootToken.spawnPlayer(this);
        } else {
            if (spawnGear != null) {
                FieldElement[] fieldElements = spawnGear.getSpace().getFieldObjects();
                for (FieldElement fieldElement : fieldElements) {
                    if (fieldElement instanceof SpawnGear) {
                        ((SpawnGear) fieldElement).spawnPlayer(this);
                        break;
                    }
                }
            } else {
                System.out.println("Cannot reboot player, no reboot tokens or start gears assigned!");
            }
        }
    }
}
