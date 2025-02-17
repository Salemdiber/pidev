package controllers;

import entities.Commentaire;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.ServiceCommentaire;
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


    /**
     * Set the selected comment to be displayed in the window.
     */
    public void setCommentaire(Commentaire commentaire) {
        this.commentaire = commentaire;
        if (commentaire != null) {
            descCommentLabel.setText(commentaire.getDesc());
        }
    }

    /**
     * Set reference to `DetailsPubController` for refreshing comments after modifications.
     */
    public void setDetailsPubController(DetailsPubController detailsPubController) {
        this.detailsPubController = detailsPubController;
    }


    /**
     * Handle deleting the selected comment.
     */
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
                serviceCommentaire.supprimer(commentaire.getId()); // Use correct ID
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Commentaire supprimé avec succès !");

                // Refresh the comment list in DetailsPubController after deletion
                if (detailsPubController != null) {
                    detailsPubController.chargerCommentaires();
                }

                fermerFenetre();
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le commentaire : " + e.getMessage());
            }
        }
    }

    /**
     * Handle modifying the selected comment.
     */
    @FXML
    private void modifierCommentaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifierCom.fxml"));
            Parent root = loader.load();

            ModifierComController controller = loader.getController();
            controller.setCommentaire(commentaire);
            controller.setDetailsComController(this); // Ensure this is properly set

            Stage stage = new Stage();
            stage.setTitle("Modifier le Commentaire");
            stage.setScene(new Scene(root));

            // Refresh comments in DetailsPubController when modification window is closed
            stage.setOnHidden(event -> {
                if (detailsPubController != null) {
                    detailsPubController.chargerCommentaires();
                }
            });

            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de modification !");
            e.printStackTrace();
        }
    }


    /**
     * Close the current window.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnSupprimer.getScene().getWindow();
        stage.close();
    }

    /**
     * Display alert messages.
     */
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
