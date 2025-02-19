package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.entities.Reclammation;
import org.example.services.ServiceReclammation;

import java.sql.SQLException;


public class AjoutRController {

    @FXML
    private TextField sujetField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button submitButton;

    @FXML
    private Button quit;

    @FXML
    private Text statusMessage;

    private AffichReclammation affichReclamation;


    public void setAffichReclamation(AffichReclammation affichReclamation) {
        this.affichReclamation = affichReclamation;
    }

    private final ServiceReclammation serviceReclammation = new ServiceReclammation();

    @FXML
    private void initialize() {
        submitButton.setOnAction(event -> ajouterReclammation());
        quit.setOnAction(event -> quitRec(new ActionEvent())); // Simule un ActionEvent
    }

    @FXML
    public void ajouterReclammation() {
        String sujet = sujetField.getText().trim();
        String description = descriptionField.getText().trim();
        int idUser = 95; // Remplacez ceci par l'ID de l'utilisateur connecté

        if (sujet.isEmpty() || description.isEmpty()) {
            statusMessage.setText("Veuillez remplir tous les champs.");
            return;
        }


        Reclammation reclammation = new Reclammation(idUser,sujet,description);

        try {
            if (serviceReclammation.ajouter(reclammation)) {
                statusMessage.setText("Réclamation ajoutée avec succès !");
                sujetField.clear();
                descriptionField.clear();
            } else {
                statusMessage.setText("Erreur lors de l'ajout de la réclamation.");
            }
        } catch (SQLException e) {
            statusMessage.setText("Erreur de connexion à la base de données.");
            e.printStackTrace();
        }
    }

    @FXML
    public void quitRec(ActionEvent actionEvent) {
        Stage stage = (Stage) quit.getScene().getWindow();

        if (this.affichReclamation != null) {
            this.affichReclamation.loadReclammationList();
        }

        stage.close();
    }

}
