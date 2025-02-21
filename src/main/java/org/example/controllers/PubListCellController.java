package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Publication;

import java.io.IOException;

public class PubListCellController extends ListCell<Publication> {
    private final HBox content;
    private final Label userLabel;
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

        VBox textContainer = new VBox(userLabel, titreLabel, descriptionLabel);
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
            // Assurez-vous que 'publication.getNom()' renvoie bien le nom de l'utilisateur
            if (publication.getNom() != null) {
                userLabel.setText("Posté par : " + publication.getNom());
            } else {
                userLabel.setText("Utilisateur inconnu");
            }
            titreLabel.setText(publication.getTitre());
            descriptionLabel.setText(publication.getDescrib());
            setGraphic(content);
        }
    }
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {

            Parent homePage = FXMLLoader.load(getClass().getResource("/view/homeAffiche.fxml"));
            Scene homeScene = new Scene(homePage);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
