package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.view.elements.elements.Gear;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link Gear Gear}.
 */
public class GearView extends ElementView {

    /** the Checkpoint that is linked to the view */
    public final Gear gear;
    private Image image;

    /**
     * Creates a new view for a {@link Gear Gear}.
     *
     * @param gear the {@link Gear Gear}.
     */
    public GearView(@NotNull Gear gear) {
        super(null, "center");
        this.gear = gear;

        // right gear
        if (this.gear.getDirection()) {
            try {
                this.image = new Image(Resources.getResource("objects/gear_right.jpg").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // left gear
        else {
            try {
                this.image = new Image(Resources.getResource("objects/gear_left.jpg").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.setImage(this.image);
    }
}
