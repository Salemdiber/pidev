package org.example.controllers;

import org.example.entities.Commentaire;
import org.example.entities.Publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.services.ServiceCommentaire;
import org.example.services.ServicePublication;

import java.sql.SQLException;

public class AjoutComController {

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button ajoutcombtn;

    @FXML
    private TextField descCom;

    private final ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private final ServicePublication servicePublication = new ServicePublication(); // Added to fetch user name
    private DetailsPubController detailsPubController;
    private int idPub;
    private int idUser = 1; // Default user ID (Change this based on logged-in user)

    public void setIdPub(int idPub) {
        this.idPub = idPub;
    }

    // Set publication ID for the comment
    public void setPublication(Publication publication) {
        if (publication != null) {
            this.idPub = publication.getIdPub();
        }
    }

    // Refresh comments after adding a comment
    public void setDetailsPubController(DetailsPubController detailsPubController) {
        this.detailsPubController = detailsPubController;
    }

    // Add a new comment
    @FXML
    public void ajouterCommentaire(ActionEvent actionEvent) {
        String description = descCom.getText().trim();

        // Validate input
        if (description.isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Champ vide", "Veuillez saisir un commentaire.");
            return;
        }

        // Check if idPub is set
        if (idPub == 0) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune publication sélectionnée !");
            return;
        }

        // Fetch the user name from the publication
        String userName = servicePublication.getUserNameById(idUser); // Retrieve user name by ID

        // Creating the comment with user_id and user_name
        Commentaire commentaire = new Commentaire(description, idUser, idPub, userName);

        try {
            serviceCommentaire.ajouter_t(commentaire);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Commentaire ajouté avec succès !");

            // Refresh comments in DetailsPubController after adding
            if (detailsPubController != null) {
                detailsPubController.chargerCommentaires();
            }

            // Close the window after adding
            fermerFenetre();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le commentaire : " + e.getMessage());
        }
    }

    /**
     * Handle cancel action.
     */
    @FXML
    public void annulerCommentaire(ActionEvent actionEvent) {
        fermerFenetre();
    }

    /**
     * Close the current window.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
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