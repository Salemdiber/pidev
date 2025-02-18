package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entities.Commentaire;
import org.example.services.ServiceCommentaire;

import java.sql.SQLException;

public class ModifierComController {

    @FXML
    private TextField descComField;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnAnnuler;

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
        btnAnnuler.setOnAction(event -> fermerFenetre());
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
                detailsComController.setCommentaire(commentaireSelectionne); // Correct method call on an instance
            }


            fermerFenetre();
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
}
