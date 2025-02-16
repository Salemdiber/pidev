package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.sql.SQLException;
import java.time.LocalDate;


public class TerrainDetailController {
    @FXML
    private Label nomLabelDetail;
    @FXML
    private Label lieuLabelDetail;
    @FXML
    private Label desLabelDetail;
    @FXML
    private ImageView terrainImageViewDetail;
    @FXML
    private Button btnreserver;
    @FXML
    private DatePicker datePicker; // Ajout du DatePicker
    @FXML
    private ComboBox<String> heureComboBox;
    private Terrain terrain;
    private final ServiceTerrain serviceTerrain = new ServiceTerrain();
    @FXML
    public void initialize() {
        btnreserver.setOnAction(event -> reserverTerrain());
    }
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        if (terrain != null) {
            nomLabelDetail.setText(terrain.getNom());
            lieuLabelDetail.setText(terrain.getLieu());
            desLabelDetail.setText(terrain.getDes());

            // Charger l'image
            if (terrain.getImg() != null && !terrain.getImg().isEmpty()) {
                Image image = new Image("file:" + terrain.getImg());
                terrainImageViewDetail.setImage(image);
            }
        }
    }
    @FXML

    private void reserverTerrain() {
        if (terrain == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Aucun terrain sélectionné.");
            return;
        }

        LocalDate dateReservation = datePicker.getValue();
        String heureReservation = heureComboBox.getValue();

        if (dateReservation == null || heureReservation == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une date et une heure.");
            return;
        }

        // Vérifier si la date est dans le passé
        LocalDate today = LocalDate.now();
        if (dateReservation.isBefore(today)) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Vous ne pouvez pas réserver un terrain pour une date passée.");
            return;
        }

        int idTerrain = terrain.getId_terrain();
        int idUser = getCurrentUserId();
        String dateHeureReservation = dateReservation.toString() + " " + heureReservation;

        try {
            // Vérifiez la disponibilité avant de réserver
            if (!serviceTerrain.isTerrainAvailable(idTerrain, dateHeureReservation)) {
                afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Le terrain est déjà réservé à cette date et heure.");
                return;
            }

            serviceTerrain.reserverTerrain(idTerrain, idUser, dateHeureReservation);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain réservé avec succès !");
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de réserver le terrain : " + e.getMessage());
        }
    }

    private int getCurrentUserId() {
        return 1;
    }
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
