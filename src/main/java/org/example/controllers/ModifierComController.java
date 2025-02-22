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
import org.example.entities.Commentaire;
import org.example.services.ServiceCommentaire;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierComController {

    @FXML
    private TextField descComField;
    @FXML
    private Button btnModifier;

    private Commentaire commentaireSelectionne;
    private final ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private DetailsComController detailsComController;

    public void setCommentaire(Commentaire commentaire) {
        this.commentaireSelectionne = commentaire;
        if (commentaire != null) {
            descComField.setText(commentaire.getDesc());
        }
    }

    public void setDetailsComController(DetailsComController detailsComController) {
        this.detailsComController = detailsComController;
    }


    @FXML
    public void initialize() {
        btnModifier.setOnAction(event -> modifierCommentaire());

    }

    @FXML
    private void modifierCommentaire() {
        if (commentaireSelectionne == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun commentaire sélectionné !");
            return;
        }

        String nouvelleDescription = descComField.getText().trim();

        if (nouvelleDescription.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le champ de description ne peut pas être vide !");
            return;
        }

        if (nouvelleDescription.length() < 5) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 5 caractères.");
            return;
        }

        commentaireSelectionne.setDesc(nouvelleDescription);

        try {
            serviceCommentaire.modifier_t(commentaireSelectionne);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Commentaire modifié avec succès !");

            if (detailsComController != null) {
                detailsComController.setCommentaire(commentaireSelectionne);
            }



        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier le commentaire : " + e.getMessage());
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
    private void fermerFenetre() {
        Stage stage = (Stage) descComField.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsCom.fxml"));
            Parent homePage = loader.load();


            DetailsComController detailsComController = loader.getController();


            if (detailsComController != null && commentaireSelectionne != null) {
                detailsComController.setCommentaire(commentaireSelectionne);
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
