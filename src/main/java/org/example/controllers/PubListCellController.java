package org.example.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.entities.Publication;

public class PubListCellController extends ListCell<Publication> {

    private final HBox content;
    private final Label titreLabel;
    private final Label descriptionLabel;

    public PubListCellController() {
        super();
        titreLabel = new Label();
        titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(300);

        VBox textContainer = new VBox(titreLabel, descriptionLabel);
        textContainer.setSpacing(5);

        content = new HBox(textContainer);
        setGraphic(content); // Ensure the custom cell is displayed
    }

    @Override
    protected void updateItem(Publication publication, boolean empty) {
        super.updateItem(publication, empty);
        if (empty || publication == null) {
            setGraphic(null);
        } else {
            titreLabel.setText(publication.getTitre());
            descriptionLabel.setText(publication.getDescrib());
            setGraphic(content); // Ensure the content is correctly displayed
        }
    }
}
