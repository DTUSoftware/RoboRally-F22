package dk.dtu.compute.se.pisd.roborally_server.fileaccess;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally_server.gamelogic.controller.GameLogicController;
import dk.dtu.compute.se.pisd.roborally_server.model.*;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.Command;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.CommandCard;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadBoard.loadBoard;

/**
 * Loads the state of the game
 */
public class LoadGameState {

    /** Where we save the game state */
    public static final String GAMESTATEFOLDER = "gamestates";
    static final private AppDirs appDirs = AppDirsFactory.getInstance();

    /**
     * Loads a map, players and board from a saved gamestate.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param gameController the game controller.
     * @param filename the filename of the saved gamestate (without .json).
     */
//    public static void loadGameState(GameLogicController gameController, String filename) {
//        InputStream inputStream = null;
//        try {
//            inputStream = Resources.getResource(GAMESTATEFOLDER + "/" + filename + ".json").openStream();
//        }
//        catch (Exception e) {
//            if (!e.toString().contains("not found")) {
//                e.printStackTrace();
//            }
//        }
//        if (inputStream == null) {
//            String appdataFolder = appDirs.getUserDataDir("RoboRally", "prod", "DTU");
//            appdataFolder = appdataFolder + "/" + GAMESTATEFOLDER + "/" + filename + ".json";
//            try {
//                inputStream = new FileInputStream(appdataFolder);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (inputStream == null) {
//                loadBoard(gameController, null);
//                return;
//            }
//        }
//
//        JSONTokener tokener = new JSONTokener(inputStream);
//        JSONObject gameStateJSON = new JSONObject(tokener);
//
//        Board board = loadBoard(gameController, gameStateJSON.getString("map"));
//        Game game = board.getGame();
//        GameState gameState = game.getGameState();
//        gameState.setPhase(Phase.valueOf(gameStateJSON.getString("phase")));
//        gameState.setStep(gameStateJSON.getInt("step"));
//
//        JSONArray players = gameStateJSON.getJSONArray("players");
//        for (int i = 0; i < players.length(); i++) {
//            JSONObject playerJSON = players.getJSONObject(i);
//            Player player = new Player(board, playerJSON.getString("color"), playerJSON.getString("name"));
//
//            player.setPower(playerJSON.getInt("power"));
//            player.setEnergy(playerJSON.getInt("energy"));
//            player.setCurrentCheckpoint(playerJSON.getInt("currentCheckpoint"));
//
//            JSONObject positionJSON = playerJSON.getJSONObject("position");
//            player.setSpace(board.getSpace(positionJSON.getInt("x"), positionJSON.getInt("y")));
//            player.setHeading(Heading.valueOf(positionJSON.getString("heading")));
//
//            JSONArray program = playerJSON.getJSONArray("program");
//            for (int j = 0; j < program.length(); j++) {
//                JSONObject programJSON = program.getJSONObject(j);
//                CommandCard commandCard = new CommandCard(Command.valueOf(programJSON.getString("command")));
//                CommandCardField field = player.getProgramField(j);
//                field.setCard(commandCard);
//                field.setVisible(programJSON.getBoolean("visible"));
//            }
//
//            JSONArray cards = playerJSON.getJSONArray("cards");
//            for (int j = 0; j < cards.length(); j++) {
//                JSONObject card = cards.getJSONObject(j);
//                CommandCard commandCard = new CommandCard(Command.valueOf(card.getString("command")));
//                CommandCardField field = player.getCardField(j);
//                field.setCard(commandCard);
//                field.setVisible(card.getBoolean("visible"));
//            }
//
//            board.addPlayer(player);
//        }
//
//        board.setCurrentPlayer(board.getPlayer(gameStateJSON.getInt("currentPlayer")));
//    }
//
//    /**
//     * Saves the current gamestate to a file.
//     *
//     * @author Marcus Sand, mwasa@dtu.dk (s215827)
//     * @param board the board.
//     */
//    public static void saveGameState(Board board) {
//        JSONObject gameStateJSON = new JSONObject();
//
//        gameStateJSON.put("map", board.getGame().getMapID());
//        gameStateJSON.put("phase", board.getGame().getGameState().getPhase().name());
//        gameStateJSON.put("step", board.getGame().getGameState().getStep());
//        gameStateJSON.put("currentPlayer", board.getPlayerNumber(board.getCurrentPlayer()));
//
//        JSONArray players = new JSONArray();
//        for (int i = 0; i < board.getPlayersNumber(); i++) {
//            Player player = board.getPlayer(i);
//            JSONObject playerJSON = new JSONObject();
//
//            playerJSON.put("name", player.getName());
//            playerJSON.put("color", player.getColor());
//            playerJSON.put("power", player.getPower());
//            playerJSON.put("energy", player.getEnergy());
//            playerJSON.put("currentCheckpoint", player.getCurrentCheckpoint());
//
//            JSONObject position = new JSONObject();
//            position.put("x", player.getSpace().x);
//            position.put("y", player.getSpace().y);
//            position.put("heading", player.getHeading().name());
//            playerJSON.put("position", position);
//
//            JSONArray program = new JSONArray();
//            for (int j = 0; j < Player.NO_REGISTERS; j++) {
//                JSONObject programCard = new JSONObject();
//                CommandCardField field = player.getProgramField(j);
//                if (field != null && field.getCard() != null) {
//                    programCard.put("command", field.getCard().command.name());
//                    programCard.put("visible", field.isVisible());
//                    program.put(programCard);
//                }
//            }
//            playerJSON.put("program", program);
//
//            JSONArray cards = new JSONArray();
//            for (int j = 0; j < Player.NO_COMMAND_CARDS; j++) {
//                JSONObject cardsJSON = new JSONObject();
//                CommandCardField field = player.getCardField(j);
//                if (field != null && field.getCard() != null) {
//                    cardsJSON.put("command", field.getCard().command.name());
//                    cardsJSON.put("visible", field.isVisible());
//                    cards.put(cardsJSON);
//                }
//            }
//            playerJSON.put("cards", cards);
//
//            players.put(playerJSON);
//        }
//        gameStateJSON.put("players", players);
//
//        String name = board.getBoardName() + " (" + new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date()) + ")";
//        String appdataFolder = appDirs.getUserDataDir("RoboRally", "prod", "DTU");
//        String filePath = appdataFolder + "/" + GAMESTATEFOLDER + "/" + name + ".json";
//        File file = new File(filePath);
//        file.getParentFile().mkdirs();
//        try (FileWriter writer = new FileWriter(file)) {
//            System.out.println("Saving game to " + filePath);
//            //We can write any JSONArray or JSONObject instance to the file
//            writer.write(gameStateJSON.toString(2));
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
