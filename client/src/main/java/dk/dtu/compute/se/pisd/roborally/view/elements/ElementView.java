package dk.dtu.compute.se.pisd.roborally.view.elements;

import dk.dtu.compute.se.pisd.roborally.model.elements.FieldElement;
import dk.dtu.compute.se.pisd.roborally.view.SpaceView;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * An ElementView is the visual representation of a {@link FieldElement FieldElement}.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
abstract public class ElementView extends BorderPane {

    // TODO: the sizes does not change when the space resizes
    /**
     * the height of the wall view
     */
    public int ELEMENT_HEIGHT = SpaceView.SPACE_HEIGHT;
    /**
     * the width of the wall view
     */
    public int ELEMENT_WIDTH = SpaceView.SPACE_WIDTH;
    /**
     * imageview object
     */
    private ImageView imageView;

    /**
     * Creates a new view for a {@link FieldElement FieldElement}.
     *
     * @param image     field teaxture
     * @param alignment gives direction for the teaxture
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ElementView(Image image, String alignment) {
        if (image != null) {
            this.imageView = new ImageView(image);
        } else {
            this.imageView = new ImageView();
        }

        switch (alignment.toLowerCase()) {
            case "top":
                this.setTop(imageView);
                break;
            case "right":
                this.setRight(imageView);
                break;
            case "bottom":
                this.setBottom(imageView);
                break;
            case "left":
                this.setLeft(imageView);
                break;
            default:
                this.setCenter(imageView);
                break;
        }

        setAlignment(imageView, Pos.CENTER);

        this.imageView.fitWidthProperty().bind(this.widthProperty());
        this.imageView.fitHeightProperty().bind(this.heightProperty());

        updateSize();
    }

    /**
     * sets the image
     *
     * @param image the image
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void setImage(Image image) {
        this.imageView.setImage(image);
    }

    /**
     * updates the size of pic
     *
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public void updateSize() {
        // XXX the following styling should better be done with styles
        this.setPrefWidth(ELEMENT_WIDTH);
        this.setMinWidth((double) ELEMENT_WIDTH / 2);
        this.setMaxWidth(ELEMENT_WIDTH);

        this.setPrefHeight(ELEMENT_HEIGHT);
        this.setMinHeight((double) ELEMENT_HEIGHT / 2);
        this.setMaxHeight(ELEMENT_HEIGHT);
    }

    /**
     * gets the imageview
     *
     * @return imageview
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public ImageView getImageView() {
        return imageView;
    }
}
