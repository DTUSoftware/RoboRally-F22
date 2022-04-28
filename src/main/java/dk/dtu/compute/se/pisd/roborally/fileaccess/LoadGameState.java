package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.elements.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadGameState {

    private static final String GAMESTATEFOLDER = "gamestates";

    public static void loadGameState(Board board, String filename) {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource(GAMESTATEFOLDER + "/" + filename + ".json").openStream();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return;
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject gameState = new JSONObject(tokener);

        board.setPhase(Phase.valueOf(gameState.getString("phase")));

        JSONArray players = gameState.getJSONArray("players");
        for (int i = 0; i < players.length(); i++) {
            JSONObject playerJSON = players.getJSONObject(i);
            Player player = new Player(board, playerJSON.getString("color"), playerJSON.getString("name"));

            player.setPower(playerJSON.getInt("power"));
            player.setEnergy(playerJSON.getInt("energy"));
            player.setHP(playerJSON.getInt("health"));
            player.setCurrentCheckpoint(playerJSON.getInt("currentCheckpoint"));

            JSONObject positionJSON = playerJSON.getJSONObject("position");
            player.setSpace(board.getSpace(positionJSON.getInt("x"), positionJSON.getInt("y")));
            player.setHeading(Heading.valueOf(positionJSON.getString("heading")));

            JSONArray program = playerJSON.getJSONArray("program");
            for (int j = 0; j < program.length(); j++) {
                JSONObject programJSON = program.getJSONObject(j);
                CommandCard commandCard = new CommandCard(Command.valueOf(programJSON.getString("command")));
                CommandCardField field = player.getProgramField(j);
                field.setCard(commandCard);
                field.setVisible(programJSON.getBoolean("visible"));
            }

            JSONArray cards = playerJSON.getJSONArray("cards");
            for (int j = 0; j < cards.length(); j++) {
                JSONObject card = cards.getJSONObject(j);
                CommandCard commandCard = new CommandCard(Command.valueOf(card.getString("command")));
                CommandCardField field = player.getCardField(j);
                field.setCard(commandCard);
                field.setVisible(card.getBoolean("visible"));
            }

            board.addPlayer(player);
        }
    }

    public static void saveGameState(Board board) {
        JSONObject gameState = new JSONObject();

        gameState.put("phase", board.getPhase().name());

        JSONArray players = new JSONArray();
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            JSONObject playerJSON = new JSONObject();

            playerJSON.put("name", player.getName());
            playerJSON.put("color", player.getColor());
            playerJSON.put("power", player.getPower());
            playerJSON.put("energy", player.getEnergy());
            playerJSON.put("health", player.getHP());
            playerJSON.put("currentCheckpoint", player.getCurrentCheckpoint());

            JSONObject position = new JSONObject();
            position.put("x", player.getSpace().x);
            position.put("y", player.getSpace().y);
            position.put("heading", player.getHeading().name());
            playerJSON.put("position", position);

            JSONArray program = new JSONArray();
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                JSONObject programCard = new JSONObject();
                CommandCardField field = player.getProgramField(j);
                programCard.put("command", field.getCard().command.name());
                programCard.put("visible", field.isVisible());
                program.put(programCard);
            }
            playerJSON.put("program", program);

            JSONArray cards = new JSONArray();
            for (int j = 0; j < Player.NO_CARDS; j++) {
                JSONObject cardsJSON = new JSONObject();
                CommandCardField field = player.getCardField(j);
                cardsJSON.put("command", field.getCard().command.name());
                cardsJSON.put("visible", field.isVisible());
                cards.put(cardsJSON);
            }
            playerJSON.put("cards", cards);

            players.put(playerJSON);
        }
        gameState.put("players", players);

        String name = board.getBoardName() + "_" + new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        URL fileURL = Resources.getResource(GAMESTATEFOLDER + "/" + name + ".json");
        try (FileWriter file = new FileWriter(fileURL.getPath())) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(gameState.toString(2));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
