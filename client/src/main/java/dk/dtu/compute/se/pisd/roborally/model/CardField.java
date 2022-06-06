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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.cards.Card;

/**
 * A field to put {@link Card Card}s on.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CardField extends Subject {
    /** The player that has the card field */
    final public Player player;

    private Card card;

    private boolean visible;

    /**
     * The CommandCardField constructor.
     *
     * @param player the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player} that has the card field.
     */
    public CardField(Player player) {
        this.player = player;
        this.card = null;
        this.visible = true;
    }

    /**
     * Gets the {@link Card Card} that's on the field.
     *
     * @return the {@link Card Card}, if any.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the {@link Card Card} that is on the field.
     * Cannot set the card to the same card.
     *
     * @param card The {@link Card Card} to put on the field.
     */
    public void setCard(Card card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * Whether the field is visible.
     *
     * @return <code>true</code> if the field is visible, else <code>false</code>.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Control whether the field is visible or not.
     *
     * @param visible <code>true</code> if the field should be visible, else <code>false</code>.
     */
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }
}
