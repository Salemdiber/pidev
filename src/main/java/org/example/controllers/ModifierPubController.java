package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entities.Publication;
import org.example.services.ServicePublication;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierPubController {

    @FXML
    private TextField titrePub;
    @FXML
    private TextField descPub;
    @FXML
    private Button btnModifierPub;

    private Publication publicationSelectionnee;
    private final ServicePublication servicePublication = new ServicePublication();
    private DetailsPubController detailsPubController;

    public void setPublication(Publication publication) {
        this.publicationSelectionnee = publication;
        if (publication != null) {
            titrePub.setText(publication.getTitre());
            descPub.setText(publication.getDescrib());
        }
    }

    public void setDetailsPubController(DetailsPubController controller) {
        this.detailsPubController = controller;
    }

    @FXML
    public void initialize() {
        btnModifierPub.setOnAction(event -> modifierPublication());
    }

    private void modifierPublication() {
        if (publicationSelectionnee == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune publication sélectionnée !");
            return;
        }

        String titre = titrePub.getText().trim();
        String description = descPub.getText().trim();

        if (titre.isEmpty() || description.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        if (titre.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le titre doit comporter au moins 3 caractères.");
            return;
        }

        if (description.length() < 10) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 10 caractères.");
            return;
        }

        publicationSelectionnee.setTitre(titre);
        publicationSelectionnee.setDescrib(description);

        try {
            servicePublication.modifier_t(publicationSelectionnee);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Publication modifiée avec succès !");


            if (detailsPubController != null) {
                detailsPubController.setPublication(publicationSelectionnee);
                detailsPubController.chargerCommentaires();
            }
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier la publication : " + e.getMessage());
        }
    }


    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML

    private void handleReturnButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsPub.fxml"));
            Parent homePage = loader.load();

            DetailsPubController detailsController = loader.getController();
            if (detailsPubController != null && publicationSelectionnee != null) {
                detailsController.setPublication(publicationSelectionnee);
                detailsController.chargerCommentaires();
            }

            Scene homeScene = new Scene(homePage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
