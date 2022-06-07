package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.PriorityAntenna;
import dk.dtu.compute.se.pisd.roborally.model.elements.RebootToken;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A RebootTokenView is the visual representation of a {@link RebootToken RebootToken}
 *
 * @author Oscar Maxwell
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class RebootTokenView extends ElementView {

    /**
     * the Checkpoint that is linked to the view
     */
    public final RebootToken rebootToken;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/reboot_token.jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link RebootToken RebootToken}.
     *
     * @param rebootToken the {@link RebootToken RebootToken}.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     * @author Oscar Maxwell
     */
    public RebootTokenView(@NotNull RebootToken rebootToken) {
        super(image, "center");
        this.rebootToken = rebootToken;

        super.setImage(this.image);

        switch (rebootToken.getDirection()) {
            case NORTH:
                super.getImageView().fitWidthProperty().bind(this.heightProperty());
                break;
            case SOUTH:
                super.getImageView().fitWidthProperty().bind(this.heightProperty());
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
