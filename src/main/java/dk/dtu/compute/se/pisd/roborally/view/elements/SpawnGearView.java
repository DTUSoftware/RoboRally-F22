package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.RebootToken;
import dk.dtu.compute.se.pisd.roborally.model.elements.SpawnGear;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A RebootTokenView is the visual representation of a {@link SpawnGear SpawnGear}.
 */
public class SpawnGearView extends ElementView {

    /** the SpawnGear that is linked to the view */
    public final SpawnGear spawnGear;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/spawn_gear.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link SpawnGear spawnGear}.
     *
     * @param spawnGear the {@link SpawnGear spawnGear}.
     */
    public SpawnGearView(@NotNull SpawnGear spawnGear) {
        super(image, "center");
        this.spawnGear = spawnGear;
    }
}
