package dk.dtu.compute.se.pisd.roborally_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Space;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.FieldElement;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.RebootToken;
import dk.dtu.compute.se.pisd.roborally_server.model.board.elements.SpawnGear;
import dk.dtu.compute.se.pisd.roborally_server.server.service.GameService;

import java.util.UUID;

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

    class Position {
        private int x;
        private int y;
        private Heading heading;

        Position(int x, int y, Heading heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }

        Position() {
            this(0, 0, null);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Heading getHeading() {
            return heading;
        }

        public void setHeading(Heading heading) {
            this.heading = heading;
        }
    }

    public Player(UUID id, UUID gameID) {
        this.id = id;
        this.gameID = gameID;
        this.position = new Position();
        this.deck = new PlayerDeck();
        this.ready = false;
    }

    public Player(UUID id, UUID gameID, String name) {
        this(id, gameID);
        this.name = name;
    }

    public UUID getID() {
        return id;
    }

    /**
     * Sets the player's ID. ONLY USE FROM GAME.
     * @param id
     */
    public void setID(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y, Heading heading) {
        if (this.position == null) {
            this.position = new Position();
        }
        this.position.setX(x);
        this.position.setY(y);
        this.position.setHeading(heading);
    }

    public void setPosition(int x, int y) {
        if (this.position != null) {
            this.position.setX(x);
            this.position.setY(y);
        }
    }

    public void setSpace(Space space) {
        if (space != null) {
            this.position.setX(space.x);
            this.position.setY(space.y);
        }
    }

    @JsonIgnore
    public Space getSpace() {
        return getGame().getBoard().getSpace(position.getX(), position.getY());
    }

    public void setHeading(Heading direction) {
        this.position.setHeading(direction);
    }

    @JsonIgnore
    public SpawnGear getStartGear() {
        return spawnGear;
    }

    public void setStartGear(SpawnGear spawnGear) {
        this.spawnGear = spawnGear;
    }

    @JsonIgnore
    public Heading getHeading() {
        return this.position.getHeading();
    }

    @JsonIgnore
    public PlayerDeck getDeck() {
        return this.deck;
    }

    public void setDeck(PlayerDeck deck) {
        this.deck = deck;
    }

    public boolean getReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * Returns whether the player has moved or not for this action.
     * @return whether they have moved or not
     */
    @JsonIgnore
    public boolean isMovedByAction() {
        return movedByAction;
    }

    /**
     * Sets the player bool of whether the player has moved from this action.
     * @param movedByAction whether they have moved
     */
    public void setMovedByAction(boolean movedByAction) {
        this.movedByAction = movedByAction;
    }

    @JsonIgnore
    public Game getGame() {
        return (new GameService()).getGameByID(gameID);
    }

    public void setGame(UUID gameID) {
        this.gameID = gameID;
    }

    /**
     * Reboot/respawn the player.
     */
    public void reboot() {

        for (int i = 0; i < 2 ; i++) {
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
        }
        else {
            if (spawnGear != null) {
                FieldElement[] fieldElements = spawnGear.getSpace().getFieldObjects();
                for (FieldElement fieldElement : fieldElements) {
                    if (fieldElement instanceof SpawnGear) {
                        ((SpawnGear) fieldElement).spawnPlayer(this);
                        break;
                    }
                }
            }
            else {
                System.out.println("Cannot reboot player, no reboot tokens or start gears assigned!");
            }
        }
    }
}
