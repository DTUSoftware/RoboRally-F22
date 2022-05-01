package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.elements.Wall;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link Laser Laser}.
 */
public class LaserView extends ElementView {

    /** the Wall that is linked to the view */
    public final Laser laser;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/laser_1.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link Laser Laser}.
     *
     * @param laser the {@link Laser Laser}.
     */
    public LaserView(@NotNull Laser laser) {
        super(image, "top");
        this.laser = laser;

        switch (laser.getDirection()) {
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
