package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Checkpoint;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A WallView is the visual representation of a {@link Checkpoint Checkpoint}.
 */
public class CheckpointView extends ElementView {

    /** the Checkpoint that is linked to the view */
    public final Checkpoint checkpoint;
    private static Image image;

    static {
        try {
            image = new Image(Resources.getResource("objects/checkpoint.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new view for a {@link Checkpoint Checkpoint}.
     *
     * @param checkpoint the {@link Checkpoint Checkpoint}.
     */
    public CheckpointView(@NotNull Checkpoint checkpoint) {
        super(image, "center");
        this.checkpoint = checkpoint;

        Text text = new Text(Integer.toString(this.checkpoint.getNumber()));
        text.setLayoutY(27);
        text.setLayoutX(28);
        text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
        text.setFill(Color.YELLOW);
        super.getChildren().add(text);

        super.ELEMENT_HEIGHT = super.ELEMENT_HEIGHT /5 * 4;
        super.ELEMENT_WIDTH = super.ELEMENT_WIDTH / 5 * 4;
        super.updateSize();
    }
}
