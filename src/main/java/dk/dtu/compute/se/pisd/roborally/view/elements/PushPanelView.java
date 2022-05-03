package dk.dtu.compute.se.pisd.roborally.view.elements;

import com.google.common.io.Resources;
import dk.dtu.compute.se.pisd.roborally.model.elements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.elements.PushPanel;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
     * @param pushPanel the {@link PushPanel pushPanel}.
     */
    public PushPanelView(@NotNull PushPanel pushPanel) {
        super(image, "top");
        this.pushPanel = pushPanel;

        //TODO chance text so it fits pushPanel
        Text text = new Text(Integer.toString(this.pushPanel.getRegister1()));
        text.setLayoutY(27);
        text.setLayoutX(28);
        text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
        text.setFill(Color.YELLOW);
        super.getChildren().add(text);

        text = new Text(Integer.toString(this.pushPanel.getRegister2()));
        text.setLayoutY(27);
        text.setLayoutX(38);
        text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 13));
        text.setFill(Color.YELLOW);
        super.getChildren().add(text);

        switch (pushPanel.getDirection()) {
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
