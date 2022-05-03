package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.PriorityAntenna;
import dk.dtu.compute.se.pisd.roborally.model.elements.RebootToken;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A RebootTokenView is the visual representation of a {@link RebootToken RebootToken}.
 */
public class RebootTokenView extends ElementView {

    /** the Checkpoint that is linked to the view */
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
     */
    public RebootTokenView(@NotNull RebootToken rebootToken) {
        super(image, "center");
        this.rebootToken = rebootToken;

        super.setImage(this.image);

        switch (rebootToken.getDirection()) {
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
