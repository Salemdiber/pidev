package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.example.entities.Commentaire;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.services.ServiceCommentaire;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.SQLException;

public class DetailsComController {

    @FXML
    private Button btnModifier;
    @FXML
    private Label descCommentLabel;
    @FXML
    private Button btnSupprimer;

    private final ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private Commentaire commentaire;
    private DetailsPubController detailsPubController;



    public void setCommentaire(Commentaire commentaire) {
        this.commentaire = commentaire;
        if (commentaire != null) {
            descCommentLabel.setText(commentaire.getDesc());
        }
    }


    public void setDetailsPubController(DetailsPubController detailsPubController) {
        this.detailsPubController = detailsPubController;
    }



    @FXML
    private void supprimerCommentaire() {
        if (commentaire == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun commentaire sélectionné !");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce commentaire ?");

        if (confirmation.showAndWait().orElse(null) == ButtonType.OK) {
            try {
                serviceCommentaire.supprimer_t(commentaire.getId());
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Commentaire supprimé avec succès !");


                if (detailsPubController != null) {
                    detailsPubController.chargerCommentaires();
                }


            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le commentaire : " + e.getMessage());
            }
        }
    }


    @FXML
    private void modifierCommentaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifierCom.fxml"));
            Parent root = loader.load();

            ModifierComController controller = loader.getController();
            controller.setCommentaire(commentaire);
            controller.setDetailsComController(this);


            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la scène de modification !");
            e.printStackTrace();
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AfficherPub.fxml"));
            Parent homePage = loader.load();


            Object controller = loader.getController();
            if (controller instanceof AfficherPubController) {
                AfficherPubController afficherPubController = (AfficherPubController) controller;

            } else {
                throw new ClassCastException("Controller is not an instance of AfficherPubController");
            }


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homePage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

}
