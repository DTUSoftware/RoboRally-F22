package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Pit;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A PitView is the visual representation of a {@link Pit Pit}.
 */
public class PitView extends ElementView {

    /** the Pit that is linked to the view */
    public final Pit pit;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/pit.jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link Pit Pit}.
     *
     * @param pit the {@link Pit Pit}.
     */
    public PitView(@NotNull Pit pit) {
        super(image, "center");
        this.pit = pit;
    }
}
