package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.elements.PushPanel;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A PushPanelView is the visual representation of a {@link PushPanel PushPanel}.
 */
public class PushPanelView extends ElementView {

    /** the Wall that is linked to the view */
    public final PushPanel pushPanel;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/push_panel.jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link PushPanel pushPanel}.
     *
     * @param laser the {@link PushPanel pushPanel}.
     */
    public PushPanelView(@NotNull PushPanel pushPanel) {
        super(image, "top");
        this.pushPanel = pushPanel;
        super.getImageView().fitHeightProperty().unbind();

        switch (pushPanel.getDirection()) {
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
