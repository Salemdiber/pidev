package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.io.File;
import java.sql.SQLException;

public class AjouterTerrainController {
    public Button btnAnnuler;
    @FXML
    private TextField nomTerrain;

    @FXML
    private TextField lieuTerrain;

    @FXML
    private TextArea descTerrain;

    @FXML
    private ImageView terrainImage;

    @FXML
    private Button btnChoisirImage;

    @FXML
    private Button btnAjouter;

    private final ServiceTerrain serviceTerrain = new ServiceTerrain();
    private File imageFile; // Stocke le fichier image sélectionné

    @FXML
    public void initialize() {

        btnChoisirImage.setOnAction(event -> choisirImage());


        btnAjouter.setOnAction(event -> ajouterTerrain());
    }
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imageFile = selectedFile;
            terrainImage.setImage(new Image(imageFile.toURI().toString()));
        }
    }
    private void ajouterTerrain() {
        String nom = nomTerrain.getText();
        String lieu = lieuTerrain.getText();
        String description = descTerrain.getText();
        String imgPath = (imageFile != null) ? imageFile.getAbsolutePath() : "";


        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || imgPath.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }
        Terrain terrain = new Terrain(nom, lieu, description, imgPath);

        try {
            serviceTerrain.ajouter_t(terrain);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain ajouté avec succès !");
            clearFields();
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le terrain : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomTerrain.clear();
        lieuTerrain.clear();
        descTerrain.clear();
        terrainImage.setImage(null); // Réinitialise l'image
        imageFile = null; // Réinitialise le fichier image
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
