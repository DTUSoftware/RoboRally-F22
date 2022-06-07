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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.cards.*;
import dk.dtu.compute.se.pisd.roborally.server.GameService;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The view that each separate {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} has.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayerView extends Tab implements ViewObserver {

    private Player player;

    private VBox top;
    private ScrollPane playerViewScrollPane;

    private Label programLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private GridPane cardsPane;
    private Label upgradeCardsLabel;
    private GridPane upgradeCardsPane;

    private CardFieldView[] programCardViews;
    private CardFieldView[] cardViews;
    private ArrayList<CardFieldView> upgradeCardViews;

    private VBox buttonPanel;

    private Button finishButton;

    private VBox playerInteractionPanel;

    private GameController gameController;

    /**
     * The PlayerView constructor.
     *
     * @param gameController the {@link dk.dtu.compute.se.pisd.roborally.controller.GameController GameController}
     *                       that controls this player view.
     * @param player         the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} that is represented
     *                       on this view.
     */
    public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
        super(player.getName());
        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        playerViewScrollPane = new ScrollPane();
        playerViewScrollPane.setContent(top);
        playerViewScrollPane.setFitToWidth(true);
        this.setContent(playerViewScrollPane);

        this.gameController = gameController;
        this.player = player;

        programLabel = new Label("Program");

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX  the following buttons should actually not be on the tabs of the individual
        //      players, but on the PlayersView (view for all players). This should be
        //      refactored.

        finishButton = new Button("Ready");
        finishButton.setOnAction( e -> {
            GameService.markPlayerReady(gameController.getGameID(), player.getID());
//            finishButton.setDisable(true);
        });

        buttonPanel = new VBox(finishButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        upgradeCardsLabel = new Label("Installed Upgrades");
        upgradeCardsPane = new GridPane();
        upgradeCardsPane.setVgap(2.0);
        upgradeCardsPane.setHgap(2.0);
        upgradeCardViews = new ArrayList<>();
        for (int i = 0; i < player.getUpgradesNum(); i++) {
            CardField cardField = player.getUpgradeField(i);
            if (cardField == null) {
                cardField = new CardField(player);
            }
            upgradeCardViews.add(i, new CardFieldView(gameController, cardField));
            upgradeCardsPane.add(upgradeCardViews.get(i), i, 0);
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);
        top.getChildren().add(upgradeCardsLabel);
        top.getChildren().add(upgradeCardsPane);

        player.attach(this);
        if (player.board != null) {
            player.board.attach(this);
            update(player.board);
        }
    }

    /**
     * Updates the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player}'s view, depending on actions taken by
     * the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} on the {@link dk.dtu.compute.se.pisd.roborally.model.Board Board}.
     *
     * @param subject the board which changed
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == player.board || subject == player) {
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null) {
                    if (player.board.getPhase() == Phase.PROGRAMMING ) {
                        cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                    } else {
                        if (i < player.board.getStep()) {
                            cardFieldView.setBackground(CardFieldView.BG_DONE);
                        } else if (i == player.board.getStep()) {
                            if (player.board.getCurrentPlayer() == player) {
                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
                            } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board.getPlayerNumber(player)) {
                                cardFieldView.setBackground(CardFieldView.BG_DONE);
                            } else {
                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                            }
                        } else {
                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                        }
                    }
                }
            }

            if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                switch (player.board.getPhase()) {
                    case WAITING:
                        finishButton.setDisable(false);
                        break;
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        // XXX just to make sure that there is a way for the player to get
                        //     from the initialization phase to the programming phase somehow!
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        break;

                    default:
                        finishButton.setDisable(true);
                }

//                if (player.isReady()) {
//                    finishButton.setDisable(true);
//                }


            } else {
                if (!programPane.getChildren().contains(playerInteractionPanel)) {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();

                if (player.board.getCurrentPlayer() == player) {

                    int step = player.board.getStep();
                    Card currentCard = null;

                    while (true) {
                        if (step < 0) {
                            System.out.println("This should not happen! No interactive cards on deck?!?");
                            return;
                        }

                        currentCard = player.board.getCurrentPlayer().getProgramField(step).getCard();

                        if (
                                (currentCard instanceof ProgramCard && ((ProgramCard) currentCard).getProgram().isInteractive())
                                ||
                                (currentCard instanceof DamageCard && ((DamageCard) currentCard).getDamage().isInteractive())
                        ) {
                            break;
                        }
                        step--;
                    }

                    if (currentCard instanceof ProgramCard) {
                        List<Program> cardOptions = ((ProgramCard) currentCard).getProgram().getOptions();

                        for (Program cardOption : cardOptions) {
                            Button optionButton = new Button(cardOption.displayName);
                            optionButton.setOnAction(e -> GameService.chooseOption(gameController.getGameID(), player.getID(), cardOption.name()));
                            optionButton.setDisable(false);
                            playerInteractionPanel.getChildren().add(optionButton);
                        }
                    }
                    else if (currentCard instanceof DamageCard) {
                        List<Damage> cardOptions = ((DamageCard) currentCard).getDamage().getOptions();

                        for (Damage cardOption : cardOptions) {
                            Button optionButton = new Button(cardOption.displayName);
                            optionButton.setOnAction(e -> GameService.chooseOption(gameController.getGameID(), player.getID(), cardOption.name()));
                            optionButton.setDisable(false);
                            playerInteractionPanel.getChildren().add(optionButton);
                        }
                    }
                }
            }
        }
    }

}
