package dk.dtu.compute.se.pisd.roborally_server.fileaccess;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally_server.model.*;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.*;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadBoard.loadBoard;

/**
 * Loads the state of the game
 */
public class LoadGameState {

    /** Where we save the game state */
    public static final String GAMESTATEFOLDER = "gamestates";
    static final private AppDirs appDirs = AppDirsFactory.getInstance();

    public static PlayerDeck getPlayerDeckFromJSON(JSONObject playerGameState) {
        PlayerDeck playerDeck = new PlayerDeck();

        if (playerGameState.has("energy")) {
            playerDeck.setEnergy(playerGameState.getInt("energy"));
        }
        if (playerGameState.has("damage")) {
            playerDeck.setDamage(playerGameState.getInt("damage"));
        }

        ArrayList<CommandCard> program = new ArrayList<>(PlayerDeck.NO_REGISTERS);
        JSONArray programJSON = playerGameState.getJSONArray("program");
        for (int j = 0; j < programJSON.length(); j++) {
            JSONObject commandCardJSON = programJSON.getJSONObject(j);
            CommandCard commandCard = new CommandCard(Command.valueOf(commandCardJSON.getString("command")));
            commandCard.setVisible(commandCardJSON.getBoolean("visible"));
            program.add(commandCard);
        }
        playerDeck.setProgram(program);

        ArrayList<Card> cards = new ArrayList<>(PlayerDeck.NO_CARDS);
        JSONArray cardsJSON = playerGameState.getJSONArray("cards");
        for (int j = 0; j < cardsJSON.length(); j++) {
            JSONObject cardJSON = cardsJSON.getJSONObject(j);
            Card card;
            switch (cardJSON.getEnum(CardType.class, "type")) {
                case COMMAND:
                    card = new CommandCard(Command.valueOf(cardJSON.getString("command")));
                    break;
                case DAMAGE:
                    card = new DamageCard(Damage.valueOf(cardJSON.getString("damage")));
                    break;
                default:
                    continue;
            }
            card.setVisible(cardJSON.getBoolean("visible"));
            cards.add(card);
        }
        playerDeck.setCards(cards);

        ArrayList<UpgradeCard> upgrades = new ArrayList<>(PlayerDeck.NO_UPGRADES);
        // TODO: remove has check
        if (playerGameState.has("upgrades")) {
            JSONArray upgradesJSON = playerGameState.getJSONArray("upgrades");
            for (int j = 0; j < upgradesJSON.length(); j++) {
                JSONObject upgradeJSON = upgradesJSON.getJSONObject(j);
                UpgradeCard upgradeCard = new UpgradeCard(Upgrade.valueOf(upgradeJSON.getString("upgrade")));
                upgradeCard.setVisible(upgradeJSON.getBoolean("visible"));
                upgrades.add(upgradeCard);
            }
        }
        playerDeck.setUpgrades(upgrades);

        return playerDeck;
    }

    /**
     * Loads a map, players and board from a saved gamestate.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param game the game.
     * @param filename the filename of the saved gamestate (without .json).
     */
    public static void loadGameState(Game game, String filename) {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResource(GAMESTATEFOLDER + "/" + filename + ".json").openStream();
        }
        catch (Exception e) {
            if (!e.toString().contains("not found")) {
                e.printStackTrace();
            }
        }
        if (inputStream == null) {
            String appdataFolder = appDirs.getUserDataDir("RoboRally", "prod", "DTU");
            appdataFolder = appdataFolder + "/" + GAMESTATEFOLDER + "/" + filename + ".json";
            try {
                inputStream = new FileInputStream(appdataFolder);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (inputStream == null) {
                game.setMapID(null);
                loadBoard(game);
                return;
            }
        }

        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject gameStateJSON = new JSONObject(tokener);

        game.setMapID(gameStateJSON.getString("map"));
        Board board = loadBoard(game);
        GameState gameState = game.getGameState();
        gameState.setPhase(Phase.valueOf(gameStateJSON.getString("phase")));
        gameState.setStep(gameStateJSON.getInt("step"));

        JSONArray players = gameStateJSON.getJSONArray("players");
        game.setPlayerCount(players.length());
        game.initializePlayers();
        for (int i = 0; i < players.length(); i++) {
            JSONObject playerJSON = players.getJSONObject(i);
            Player player = game.getGameState().getPlayer(i);
            player.setColor(playerJSON.getString("color"));
            player.setName(playerJSON.getString("name"));

            player.setCurrentCheckpoint(playerJSON.getInt("currentCheckpoint"));

            JSONObject positionJSON = playerJSON.getJSONObject("position");
            player.setSpace(board.getSpace(positionJSON.getInt("x"), positionJSON.getInt("y")));
            player.setHeading(Heading.valueOf(positionJSON.getString("heading")));

            // parse and load deck
            PlayerDeck loadedPlayerDeck = getPlayerDeckFromJSON(playerJSON);
            PlayerDeck playerDeck = player.getDeck();

            playerDeck.setCards(loadedPlayerDeck.getCards());
            playerDeck.setProgram(loadedPlayerDeck.getProgram());
            playerDeck.setUpgrades(loadedPlayerDeck.getUpgrades());
        }

        game.getGameState().setCurrentPlayer(gameStateJSON.getInt("currentPlayer"));
    }

    /**
     * Saves the current gamestate to a file.
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @param game the game.
     */
    public static void saveGameState(Game game) {
        JSONObject gameStateJSON = new JSONObject();

        gameStateJSON.put("map", game.getMapID());
        gameStateJSON.put("phase", game.getGameState().getPhase().name());
        gameStateJSON.put("step", game.getGameState().getStep());
        gameStateJSON.put("currentPlayer", game.getGameState().getCurrentPlayer());

        JSONArray players = new JSONArray();
        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player player = game.getGameState().getPlayer(i);
            JSONObject playerJSON = new JSONObject();

            playerJSON.put("name", player.getName());
            playerJSON.put("color", player.getColor());
            playerJSON.put("energy", player.getDeck().getEnergy());
            playerJSON.put("damage", player.getDeck().getDamage());
            playerJSON.put("currentCheckpoint", player.getCurrentCheckpoint());

            JSONObject position = new JSONObject();
            position.put("x", player.getSpace().x);
            position.put("y", player.getSpace().y);
            position.put("heading", player.getHeading().name());
            playerJSON.put("position", position);

            JSONArray program = new JSONArray();
            for (int j = 0; j < PlayerDeck.NO_REGISTERS; j++) {
                JSONObject programCard = new JSONObject();
                CommandCard field = player.getDeck().getProgramField(j);
                if (field != null && field.getCommand() != null) {
                    programCard.put("type", "COMMAND");
                    programCard.put("command", field.getCommand().name());
                    programCard.put("visible", field.getVisible());
                    program.put(programCard);
                }
            }
            playerJSON.put("program", program);

            JSONArray cards = new JSONArray();
            for (int j = 0; j < PlayerDeck.NO_CARDS; j++) {
                JSONObject cardsJSON = new JSONObject();
                Card field = player.getDeck().getCardField(j);
                if (field != null && field.getType() != null) {
                    switch (field.getType()) {
                        case COMMAND:
                            cardsJSON.put("type", "COMMAND");
                            cardsJSON.put("command", ((CommandCard) field).getCommand().name());
                            break;
                        case DAMAGE:
                            cardsJSON.put("type", "DAMAGE");
                            cardsJSON.put("command", ((DamageCard) field).getDamage().name());
                            break;
                    }
                    cardsJSON.put("visible", field.getVisible());
                    cards.put(cardsJSON);
                }
            }
            playerJSON.put("cards", cards);

            players.put(playerJSON);
        }
        gameStateJSON.put("players", players);

        String name = game.getMapID() + " (" + new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date()) + ")";
        String appdataFolder = appDirs.getUserDataDir("RoboRally", "prod", "DTU");
        String filePath = appdataFolder + "/" + GAMESTATEFOLDER + "/" + name + ".json";
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            System.out.println("Saving game to " + filePath);
            //We can write any JSONArray or JSONObject instance to the file
            writer.write(gameStateJSON.toString(2));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
