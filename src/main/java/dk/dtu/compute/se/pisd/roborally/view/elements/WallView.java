package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Wall;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link dk.dtu.compute.se.pisd.roborally.model.elements.Wall Wall}.
 */
public class WallView extends ElementView {

    /** the Wall that is linked to the view */
    public final Wall wall;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/wall.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link dk.dtu.compute.se.pisd.roborally.model.elements.Wall Wall}.
     *
     * @param wall the {@link dk.dtu.compute.se.pisd.roborally.model.elements.Wall Wall}.
     */
    public WallView(@NotNull Wall wall) {
        super(image, "top");
        this.wall = wall;
        super.getImageView().fitHeightProperty().unbind();

        switch (wall.getDirection()) {
            case NORTH:
                super.getImageView().fitWidthProperty().bind(this.widthProperty());
                break;
            case SOUTH:
                super.getImageView().fitWidthProperty().bind(this.widthProperty());
                this.setRotate(180);
                break;
            case EAST:
                super.getImageView().fitWidthProperty().bind(this.heightProperty());
                this.setRotate(90);
                break;
            case WEST:
                super.getImageView().fitWidthProperty().bind(this.heightProperty());
                this.setRotate(-90);
                break;
        }
    }
}
