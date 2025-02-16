package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.sql.SQLException;


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

        int idTerrain = terrain.getId_terrain();
        int idUser = getCurrentUserId(); // Remplace par la logique d'authentification
        String dateReservation = java.time.LocalDate.now().toString();

        try {
            serviceTerrain.reserverTerrain(idTerrain, idUser, dateReservation);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain réservé avec succès !");
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de réserver le terrain : " + e.getMessage());
        }
    }
    private int getCurrentUserId() {
        return 1; // Remplace cette valeur par la vraie logique de récupération de l'ID utilisateur
    }
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
