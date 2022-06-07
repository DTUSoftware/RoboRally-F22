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

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.CardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.cards.Card;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.server.GameService.updatePlayerDeck;

/**
 * The CardFieldView is the visual representation of a
 * {@link CardField CommandCardField}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class CardFieldView extends GridPane implements ViewObserver {

    /**
     * This data format helps avoiding transfers of e.g. Strings from other
     * programs which can copy/paste Strings.
     */
    final public static DataFormat ROBO_RALLY_CARD = new DataFormat("games/roborally/cards");

    /**
     * The width of a card field
     */
    final public static int CARDFIELD_WIDTH = 65;
    /**
     * The height of a card field
     */
    final public static int CARDFIELD_HEIGHT = 100;

    /**
     * The border design of a card field
     */
    final public static Border BORDER = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));

    /**
     * The background design of a card field
     */
    final public static Background BG_DEFAULT = new Background(new BackgroundFill(Color.WHITE, null, null));
    /**
     * The background design of a card when dragged
     */
    final public static Background BG_DRAG = new Background(new BackgroundFill(Color.GRAY, null, null));
    /**
     * The background design of a card when dropped
     */
    final public static Background BG_DROP = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));

    /**
     * The background design of a card when active
     */
    final public static Background BG_ACTIVE = new Background(new BackgroundFill(Color.YELLOW, null, null));
    /**
     * The background design of a card when done
     */
    final public static Background BG_DONE = new Background(new BackgroundFill(Color.GREENYELLOW, null, null));

    /**
     * imageview object
     */
    private ImageView imageView;
    /**
     * the color adjusting of image
     */
    private ColorAdjust imageColor = new ColorAdjust();

    private CardField field;

    private Label label;

    private GameController gameController;

    /**
     * The CardFieldView constructor.
     *
     * @param gameController the {@link dk.dtu.compute.se.pisd.roborally.controller.GameController GameController}
     *                       that controls this card field view.
     * @param field          the {@link CardField CommandCardField} that
     *                       is represented in the view.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public CardFieldView(@NotNull GameController gameController, @NotNull CardField field) {
        this.gameController = gameController;
        this.field = field;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(5, 5, 5, 5));

        this.setBorder(BORDER);
        this.setBackground(BG_DEFAULT);

        this.setPrefWidth(CARDFIELD_WIDTH);
        this.setMinWidth(CARDFIELD_WIDTH);
//        this.setMaxWidth(CARDFIELD_WIDTH);
        this.setPrefHeight(CARDFIELD_HEIGHT);
        this.setMinHeight(CARDFIELD_HEIGHT);
//        this.setMaxHeight(CARDFIELD_HEIGHT);

        label = new Label("This is a slightly longer text");
        label.setWrapText(true);
        label.setMouseTransparent(true);
        this.add(label, 0, 0);

        this.imageView = new ImageView();
        this.imageView.fitWidthProperty().bind(this.widthProperty());
        this.imageView.fitHeightProperty().bind(this.heightProperty());
        this.imageView.setMouseTransparent(true);
        this.imageView.setEffect(imageColor);
        this.add(imageView, 0, 0);

        this.setOnDragDetected(new OnDragDetectedHandler());
        this.setOnDragOver(new OnDragOverHandler());
        this.setOnDragEntered(new OnDragEnteredHandler());
        this.setOnDragExited(new OnDragExitedHandler());
        this.setOnDragDropped(new OnDragDroppedHandler());
        this.setOnDragDone(new OnDragDoneHandler());

        field.attach(this);
        update(field);
    }

    /**
     * Gets the representation of the provided cardfield.
     *
     * @param cardField the {@link CardField CommandCardField} to get the
     *                  representation of.
     * @return the representation, based on current field player, and such.
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private String cardFieldRepresentation(CardField cardField) {
        if (cardField.player != null) {

            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardField other = cardField.player.getProgramField(i);
                if (other == cardField) {
                    return "P," + i;
                }
            }

            for (int i = 0; i < Player.NO_CARDS; i++) {
                CardField other = cardField.player.getCardField(i);
                if (other == cardField) {
                    return "C," + i;
                }
            }
        }
        return null;

    }

    /**
     * Gets a CommandCardField based on the provided representation.
     *
     * @param rep the representation.
     * @return the {@link CardField CommandCardField}, based on the representation.
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    private CardField cardFieldFromRepresentation(String rep) {
        if (rep != null && field.player != null) {
            String[] strings = rep.split(",");
            if (strings.length == 2) {
                int i = Integer.parseInt(strings[1]);
                if ("P".equals(strings[0])) {
                    if (i < Player.NO_REGISTERS) {
                        return field.player.getProgramField(i);
                    }
                } else if ("C".equals(strings[0])) {
                    if (i < Player.NO_CARDS) {
                        return field.player.getCardField(i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Updates the card field, when changes are made to the {@link CardField CommandCardField}.
     *
     * @param subject the subject which changed
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == field && subject != null) {
            Card card = field.getCard();
            if (card != null && field.isVisible()) {
                label.setText(card.getName());
                try {
                    imageView.setImage(new Image(Resources.getResource("objects/cards/" + card.getName() + ".jpg").openStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                label.setText("");
                imageView.setImage(null);
            }
        }
    }

    /**
     * Detects mouse drag events of cards.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private class OnDragDetectedHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView source = (CardFieldView) t;
                CardField cardField = source.field;
                if (cardField != null &&
                        cardField.getCard() != null &&
                        cardField.player != null &&
                        cardField.player.board != null &&
                        cardField.player.board.getPhase().equals(Phase.PROGRAMMING)) {
                    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                    Image image = source.snapshot(null, null);
                    db.setDragView(image);

                    ClipboardContent content = new ClipboardContent();
                    content.put(ROBO_RALLY_CARD, cardFieldRepresentation(cardField));

                    db.setContent(content);
                    imageColor.setSaturation(-1);
                    source.setBackground(BG_DRAG);
                }
            }
            event.consume();
        }

    }

    /**
     * Handles mouse drag events of cards.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private class OnDragOverHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target = (CardFieldView) t;
                CardField cardField = target.field;
                if (cardField != null &&
                        (cardField.getCard() == null || event.getGestureSource() == target) &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * Handles mouse drag events of cards.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private class OnDragEnteredHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target = (CardFieldView) t;
                CardField cardField = target.field;
                if (cardField != null &&
                        cardField.getCard() == null &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        target.setBackground(BG_DROP);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * Handles mouse drag events of cards.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private class OnDragExitedHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target = (CardFieldView) t;
                CardField cardField = target.field;
                if (cardField != null &&
                        cardField.getCard() == null &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        imageColor.setSaturation(0);
                        target.setBackground(BG_DEFAULT);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * Handles mouse drag events of cards.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private class OnDragDroppedHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target = (CardFieldView) t;
                CardField cardField = target.field;

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (cardField != null &&
                        cardField.getCard() == null &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getGestureSource() != target &&
                            db.hasContent(ROBO_RALLY_CARD)) {
                        Object object = db.getContent(ROBO_RALLY_CARD);
                        if (object instanceof String) {
                            CardField source = cardFieldFromRepresentation((String) object);
                            if (source != null && gameController.moveCards(source, cardField)) {
                                if (updatePlayerDeck(gameController.getGameID(), cardField.player)) {
                                    // CommandCard card = source.getCard();
                                    // if (card != null) {
                                    // if (gameController.moveCards(source, cardField)) {
                                    // cardField.setCard(card);
                                    success = true;
                                    // }
                                } else {
                                    gameController.moveCards(cardField, source);
                                }
                            }
                        }
                    }
                }
                event.setDropCompleted(success);
                target.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

    /**
     * Handles mouse drag events of cards.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    private class OnDragDoneHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView source = (CardFieldView) t;
                imageColor.setSaturation(0);
                source.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

}




