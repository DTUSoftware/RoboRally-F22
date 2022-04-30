package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.elements.ConveyorBelt;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link ConveyorBelt ConveyorBelt}.
 */
public class ConveyorBeltView extends ElementView {

    /** the Checkpoint that is linked to the view */
    public final ConveyorBelt conveyorBelt;
    private Image image;

    /**
     * Creates a new view for a {@link ConveyorBelt ConveyorBelt}.
     *
     * @param conveyorBelt the {@link ConveyorBelt ConveyorBelt}.
     */
    public ConveyorBeltView(@NotNull ConveyorBelt conveyorBelt) {
        super(null, "center");
        this.conveyorBelt = conveyorBelt;

        // blue conveyor belt
        if (this.conveyorBelt.getColor()) {
            try {
                this.image = new Image(Resources.getResource("objects/blue_conveyor_belt.jpg").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                this.image = new Image(Resources.getResource("objects/green_conveyor_belt.jpg").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.setImage(this.image);

        switch (conveyorBelt.getDirection()) {
            case NORTH:
                break;
            case SOUTH:
                this.setRotate(180);
                break;
            case EAST:
                this.setRotate(90);
                break;
            case WEST:
                this.setRotate(-90);
                break;
        }
    }
}
