package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.entities.Reclammation;
import org.example.services.ServiceReclammation;

import java.sql.SQLException;

public class EditRController {

    @FXML
    private TextField sujetField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button submitButton;

    @FXML
    private Text statusMessage;

    private final ServiceReclammation serviceReclammation = new ServiceReclammation();
    private Reclammation reclammation;

    public void setReclammation(Reclammation reclammation) {
        this.reclammation = reclammation;
        sujetField.setText(reclammation.getSujet());
        descriptionField.setText(reclammation.getDescription());
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction(event -> modifierReclammation());
    }

    private void modifierReclammation() {
        if (reclammation == null) {
            statusMessage.setText("Aucune réclamation sélectionnée.");
            return;
        }

        String sujet = sujetField.getText().trim();
        String description = descriptionField.getText().trim();

        if (sujet.isEmpty() || description.isEmpty()) {
            statusMessage.setText("Veuillez remplir tous les champs.");
            return;
        }

        reclammation.setSujet(sujet);
        reclammation.setDescription(description);

        try {
            if (serviceReclammation.modifier(reclammation)) {
                statusMessage.setText("Réclamation modifiée avec succès !");
            } else {
                statusMessage.setText("Erreur lors de la modification de la réclamation.");
            }
        } catch (SQLException e) {
            statusMessage.setText("Erreur de connexion à la base de données.");
            e.printStackTrace();
        }
    }
}
