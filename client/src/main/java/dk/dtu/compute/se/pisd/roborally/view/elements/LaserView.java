package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.elements.Wall;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link Laser Laser}.
 * @author Oscar Maxwell
 * @author Nicolai Udbye
 * @author Marcus
 */
public class LaserView extends ElementView {

    /** the Wall that is linked to the view */
    public final Laser laser;
    private static Image image;
    private int lazerNumber;

    /**
     * Creates a new view for a {@link Laser Laser}.
     *
     * @param laser the {@link Laser Laser}.
     */
    public LaserView(@NotNull Laser laser) {
        super(null, "top");
        this.laser = laser;
        lazerNumber = this.laser.getLazer();

        if (lazerNumber == 1) {
            try {
                image = new Image(Resources.getResource("objects/laser_1.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (lazerNumber == 2) {
            try {
                image = new Image(Resources.getResource("objects/laser_2.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (lazerNumber == 3) {
            try {
                image = new Image(Resources.getResource("objects/laser_3.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (lazerNumber == 4) {
            try {
                image = new Image(Resources.getResource("objects/laser_1_middel.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (lazerNumber == 5) {
            try {
                image = new Image(Resources.getResource("objects/laser_2_middel.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (lazerNumber == 6) {
            try {
                image = new Image(Resources.getResource("objects/laser_3_middel.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                image = new Image(Resources.getResource("objects/laser_1.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.setImage(this.image);

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
