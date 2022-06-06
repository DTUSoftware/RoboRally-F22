package dk.dtu.compute.se.pisd.roborally_server.fileaccess;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally_server.model.*;
import dk.dtu.compute.se.pisd.roborally_server.model.board.Board;
import dk.dtu.compute.se.pisd.roborally_server.model.cards.*;
import dk.dtu.compute.se.pisd.roborally_server.server.service.JSONService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static dk.dtu.compute.se.pisd.roborally_server.fileaccess.LoadBoard.loadBoard;

/**
 * Loads the state of the game
 */
public class LoadGameState {

    /** Where we save the game state */
    public static final String GAMESTATEFOLDER = "gamestates";
    public static final SimpleDateFormat gameStateTimeFormat = new SimpleDateFormat("(dd-MM-yyyy_HH-mm-ss)");

    public static PlayerDeck getPlayerDeckFromJSON(JSONObject playerGameState) {
        PlayerDeck playerDeck = new PlayerDeck();

        if (playerGameState.has("energy")) {
            playerDeck.setEnergy(playerGameState.getInt("energy"));
        }
        if (playerGameState.has("damage")) {
            playerDeck.setDamage(playerGameState.getInt("damage"));
        }

        ArrayList<ProgramCard> program = new ArrayList<>(PlayerDeck.NO_REGISTERS);
        JSONArray programJSON = playerGameState.getJSONArray("program");
        for (int j = 0; j < programJSON.length(); j++) {
            JSONObject commandCardJSON = programJSON.getJSONObject(j);
            ProgramCard programCard = new ProgramCard(Program.valueOf(commandCardJSON.getString("program")));
            programCard.setVisible(commandCardJSON.getBoolean("visible"));
            program.add(programCard);
        }
        playerDeck.setProgram(program);

        ArrayList<Card> cards = new ArrayList<>(PlayerDeck.NO_CARDS);
        JSONArray cardsJSON = playerGameState.getJSONArray("cards");
        for (int j = 0; j < cardsJSON.length(); j++) {
            JSONObject cardJSON = cardsJSON.getJSONObject(j);
            Card card;
            switch (cardJSON.getEnum(CardType.class, "type")) {
                case PROGRAM:
                    card = new ProgramCard(Program.valueOf(cardJSON.getString("program")));
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
            String appdataFolder = JSONService.getAppDataFolder();
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

            game.changePlayerID(player.getID(), UUID.fromString(playerJSON.getString("id")));

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
            playerJSON.put("id", player.getID());
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
                ProgramCard field = player.getDeck().getProgramField(j);
                if (field != null && field.getProgram() != null) {
                    programCard.put("type", "PROGRAM");
                    programCard.put("program", field.getProgram().name());
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
                        case PROGRAM:
                            cardsJSON.put("type", "PROGRAM");
                            cardsJSON.put("program", ((ProgramCard) field).getProgram().name());
                            break;
                        case DAMAGE:
                            cardsJSON.put("type", "DAMAGE");
                            cardsJSON.put("damage", ((DamageCard) field).getDamage().name());
                            break;
                    }
                    cardsJSON.put("visible", field.getVisible());
                    cards.put(cardsJSON);
                }
            }
            playerJSON.put("cards", cards);

            JSONArray upgrades = new JSONArray();
            for (int j = 0; j < PlayerDeck.NO_UPGRADES; j++) {
                JSONObject upgradesJSON = new JSONObject();
                UpgradeCard field = player.getDeck().getUpgradeField(j);
                if (field != null && field.getType() != null) {
                    upgradesJSON.put("type", "UPGRADE");
                    upgradesJSON.put("upgrade", field.getUpgrade().name());
                    upgradesJSON.put("visible", field.getVisible());
                    upgrades.put(upgradesJSON);
                }
            }
            playerJSON.put("upgrades", upgrades);

            players.put(playerJSON);
        }
        gameStateJSON.put("players", players);

        String name = game.getID() + " " + gameStateTimeFormat.format(new Date());
        String appdataFolder = JSONService.getAppDataFolder();
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
