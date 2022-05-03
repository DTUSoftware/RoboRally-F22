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
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.elements.*;
import dk.dtu.compute.se.pisd.roborally.view.elements.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A SpaceView is the visual representation of a {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    /** the height of the space view */
    final public static int SPACE_HEIGHT = 60; // 75;
    /** the width of the space view */
    final public static int SPACE_WIDTH = 60;  // 75;

    /** the Space that is linked to the view */
    public final Space space;

    private static Image image;
    private final ImageView imageView;

    static {
        try {
            image = new Image(Resources.getResource("objects/field.jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
     *
     * @param space the {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;
        this.imageView = new ImageView(image);
        this.imageView.fitWidthProperty().bind(this.widthProperty());
        this.imageView.fitHeightProperty().bind(this.heightProperty());

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth((double) SPACE_WIDTH/2);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight((double) SPACE_HEIGHT/2);
        this.setMaxHeight(SPACE_HEIGHT);


        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * Updates the view with the objects.
     */
    private void updateObjects() {
        this.getChildren().add(imageView);
        setAlignment(imageView, Pos.CENTER);

        ArrayList<CheckpointView> checkpoints = new ArrayList<>();
        ArrayList<ConveyorBeltView> conveyorBelts = new ArrayList<>();
        ArrayList<EnergySpaceView> energySpaces = new ArrayList<>();
        ArrayList<GearView> gears = new ArrayList<>();
        ArrayList<LaserView> lasers = new ArrayList<>();
        ArrayList<PitView> pits = new ArrayList<>();
        ArrayList<PriorityAntennaView> priorityAntennas = new ArrayList<>();
        ArrayList<PushPanelView> pushPanels = new ArrayList<>();
        ArrayList<RebootTokenView> rebootTokens = new ArrayList<>();
        ArrayList<SpawnGearView> spawnGears = new ArrayList<>();
        ArrayList<WallView> walls = new ArrayList<>();

        // we do this because we need to be able to control which elements are added to the spaceview first (overlap control in JavaFX)
        for (FieldElement fieldElement : space.getFieldObjects()) {
            if (fieldElement instanceof Checkpoint) {
                checkpoints.add(new CheckpointView((Checkpoint) fieldElement));
            }
            else if (fieldElement instanceof ConveyorBelt) {
                conveyorBelts.add(new ConveyorBeltView((ConveyorBelt) fieldElement));
            }
            else if (fieldElement instanceof EnergySpace) {
                energySpaces.add(new EnergySpaceView((EnergySpace) fieldElement));
            }
            else if (fieldElement instanceof Gear) {
                gears.add(new GearView((Gear) fieldElement));
            }
            else if (fieldElement instanceof Laser) {
                lasers.add(new LaserView((Laser) fieldElement));
            }
            else if (fieldElement instanceof Pit) {
                pits.add(new PitView((Pit) fieldElement));
            }
            else if (fieldElement instanceof PriorityAntenna) {
                priorityAntennas.add(new PriorityAntennaView((PriorityAntenna) fieldElement));
            }
            else if (fieldElement instanceof PushPanel) {
                pushPanels.add(new PushPanelView((PushPanel) fieldElement));
            }
            else if (fieldElement instanceof RebootToken) {
                rebootTokens.add(new RebootTokenView((RebootToken) fieldElement));
            }
            else if (fieldElement instanceof SpawnGear) {
                spawnGears.add(new SpawnGearView((SpawnGear) fieldElement));
            }
            else if (fieldElement instanceof Wall) {
                walls.add(new WallView((Wall) fieldElement));
            }
        }

        for (ConveyorBeltView conveyorBelt : conveyorBelts) {
            this.getChildren().add(conveyorBelt);
        }
        for (RebootTokenView rebootToken : rebootTokens) {
            this.getChildren().add(rebootToken);
        }
        for (CheckpointView checkpoint : checkpoints) {
            this.getChildren().add(checkpoint);
        }
        for (EnergySpaceView energySpace : energySpaces) {
            this.getChildren().add(energySpace);
        }
        for (PriorityAntennaView priorityAntenna : priorityAntennas) {
            this.getChildren().add(priorityAntenna);
        }
        for (SpawnGearView spawnGear : spawnGears) {
            this.getChildren().add(spawnGear);
        }
        for (GearView gear : gears) {
            this.getChildren().add(gear);
        }
        for (LaserView laser : lasers) {
            this.getChildren().add(laser);
        }
        for (PushPanelView pushPanel : pushPanels) {
            this.getChildren().add(pushPanel);
        }
        for (WallView wall : walls) {
            this.getChildren().add(wall);
        }
        for (PitView pit : pits) {
            this.getChildren().add(pit);
        }
    }

    /**
     * Updates the view with headings and other effects on the {@link dk.dtu.compute.se.pisd.roborally.model.Player Player}
     * when the player interacts with the {@link dk.dtu.compute.se.pisd.roborally.model.Space Space}.
     */
    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    /**
     * Updates when the {@link dk.dtu.compute.se.pisd.roborally.model.Space Space} gets changed.
     *
     * @param subject the subject which changed
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            updateObjects();
            updatePlayer();
        }
    }

}
