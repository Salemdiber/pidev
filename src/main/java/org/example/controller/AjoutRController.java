package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.entities.Reclammation;
import org.example.services.ServiceReclammation;

import java.sql.SQLException;
import java.util.Date;

public class AjoutRController {

    @FXML
    private TextField sujetField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button submitButton;

    @FXML
    private Text statusMessage;

    private final ServiceReclammation serviceReclammation = new ServiceReclammation();

    @FXML
    private void initialize() {
        submitButton.setOnAction(event -> ajouterReclammation());
    }

    private void ajouterReclammation() {
        String sujet = sujetField.getText().trim();
        String description = descriptionField.getText().trim();
        int idUser = 1; // Remplacez ceci par l'ID de l'utilisateur connecté

        if (sujet.isEmpty() || description.isEmpty()) {
            statusMessage.setText("Veuillez remplir tous les champs.");
            return;
        }


        Reclammation reclammation = new Reclammation(idUser, sujet, description, new Date(), "En attente");

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
}
