package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.EnergySpace;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link EnergySpace EnergySpace}.
 */
public class EnergySpaceView extends ElementView {

    /** the Checkpoint that is linked to the view */
    public final EnergySpace energySpace;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/energy_space.jpg").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link EnergySpace EnergySpace}.
     *
     * @param energySpace the {@link EnergySpace EnergySpace}.
     */
    public EnergySpaceView(@NotNull EnergySpace energySpace) {
        super(image, "center");
        this.energySpace = energySpace;
    }
}
