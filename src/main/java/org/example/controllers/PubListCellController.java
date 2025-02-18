package org.example.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.entities.Publication;

public class PubListCellController extends ListCell<Publication> {
    private final HBox content;
    private final Label userLabel; // New Label for user's name
    private final Label titreLabel;
    private final Label descriptionLabel;

    public PubListCellController() {
        super();
        userLabel = new Label();
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: blue;");

        titreLabel = new Label();
        titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(300);

        VBox textContainer = new VBox(userLabel, titreLabel, descriptionLabel); // Show nom above titre
        textContainer.setSpacing(5);

        content = new HBox(textContainer);
        setGraphic(content);
    }

    @Override
    protected void updateItem(Publication publication, boolean empty) {
        super.updateItem(publication, empty);
        if (empty || publication == null) {
            setGraphic(null);
        } else {
            userLabel.setText("Posté par : " + publication.getNom()); // Display user's name
            titreLabel.setText(publication.getTitre());
            descriptionLabel.setText(publication.getDescrib());
            setGraphic(content);
        }
    }
}
