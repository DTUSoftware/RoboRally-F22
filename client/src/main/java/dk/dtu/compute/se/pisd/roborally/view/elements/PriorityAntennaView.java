package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.EnergySpace;
import dk.dtu.compute.se.pisd.roborally.model.elements.PriorityAntenna;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link PriorityAntenna PriorityAntenna}.
 */
public class PriorityAntennaView extends ElementView {

    /** the Checkpoint that is linked to the view */
    public final PriorityAntenna priorityAntenna;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/priority_antenna.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link PriorityAntenna PriorityAntenna}.
     *
     * @param priorityAntenna the {@link PriorityAntenna PriorityAntenna}.
     */
    public PriorityAntennaView(@NotNull PriorityAntenna priorityAntenna) {
        super(image, "center");
        this.priorityAntenna = priorityAntenna;
    }
}
