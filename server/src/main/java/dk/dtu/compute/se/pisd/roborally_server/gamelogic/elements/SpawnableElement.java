package dk.dtu.compute.se.pisd.roborally_server.gamelogic.elements;

import dk.dtu.compute.se.pisd.roborally_server.controller.GameController;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.*;

/**
 * Spawnablelement abstract class that extends actionelement and helps spawning the elements.
 */
public abstract class SpawnableElement extends ActionElement {
    private Heading spawnDirection;

    /**
     * Constructer for spawnableelement
     *
     * @param gameLogicController the game controller
     * @param space          the space
     * @param spawnDirection the direction
     */
    public SpawnableElement(IGameLogicController gameLogicController, Space space, Heading spawnDirection) {
        super(gameLogicController, space);
        this.spawnDirection = spawnDirection;
    }

    /**
     * getter for direction
     * @return spawnDirection which is the direction a wall or pushpanel should spawn
     */
    public Heading getDirection() {
        return spawnDirection;
    }

    /**
     * Spawns a player at a certain space.
     * @param player takes the player
     */
    public void spawnPlayer(Player player) {
        player.setHeading(getDirection());
        if (getSpace().free() || getSpace().getPlayer() == player) {
            player.setSpace(getSpace());
        }
        else {
            try {
                super.getGameLogicController().moveDirection(player, getDirection());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
