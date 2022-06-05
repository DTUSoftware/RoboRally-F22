package dk.dtu.compute.se.pisd.roborally_server.gamelogic;

import dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.elements.ActionElement;
import dk.dtu.compute.se.pisd.roborally_server.model.Heading;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.model.Player;

public interface IGameLogicController {
    void addElement(ActionElement actionElement);
    void winTheGame(Player player);
    void moveDirectionX(Player player, Heading direction, int i);
    void turnRight(Player player);
    void turnLeft(Player player);
    void moveDirection(Player player, Heading direction);
}
