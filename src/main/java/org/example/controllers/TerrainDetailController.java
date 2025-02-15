package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.entities.Terrain;

public class TerrainDetailController {
    @FXML
    private Label nomLabelDetail;
    @FXML
    private Label lieuLabelDetail;
    @FXML
    private Label desLabelDetail;
    @FXML
    private ImageView terrainImageViewDetail;

    public void setTerrain(Terrain terrain) {
        if (terrain != null) {
            nomLabelDetail.setText(terrain.getNom());
            lieuLabelDetail.setText(terrain.getLieu());
            desLabelDetail.setText(terrain.getDes());

            // Charger l'image
            String imagePath = terrain.getImg(); // Assurez-vous que cette méthode retourne le chemin de l'image
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image("file:" + imagePath); // Utilisez "file:" pour charger une image à partir d'un chemin de fichier
                terrainImageViewDetail.setImage(image);
            } else {
                // Optionnel : définir une image par défaut si le chemin est vide
                terrainImageViewDetail.setImage(null); // Ou une image par défaut
            }
        }
    }
}
