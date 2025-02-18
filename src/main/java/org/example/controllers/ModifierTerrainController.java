package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.io.File;
import java.sql.SQLException;

public class ModifierTerrainController {

    @FXML
    private TextField nametxtfield ;
    @FXML
    private TextField lieutxtfield ;
    @FXML
    private TextField desctxtfield ;
    @FXML
    private TextField txtImgTerrain ;
    @FXML
    private ImageView terrainImage;

    @FXML
    private Button btnChoisirImage;
    @FXML
    private Button btnModifierTerrain;
    @FXML
    private Button annulerbtn;



    private Terrain terrainSelectionne;
    private final ServiceTerrain serviceTerrain = new ServiceTerrain();

    public void setTerrain(Terrain terrain) {
        this.terrainSelectionne = terrain;
        if (terrain != null) {
            nametxtfield.setText(terrain.getNom());
            lieutxtfield.setText(terrain.getLieu());
            desctxtfield.setText(terrain.getDes());
            txtImgTerrain.setText(terrain.getImg());
        }
    }

    @FXML
    public void initialize() {
        btnChoisirImage.setOnAction(event -> choisirImage());
        btnModifierTerrain.setOnAction(event -> modifierTerrain());
        annulerbtn.setOnAction(event -> annulerModification());

    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Choisir une image pour le terrain");

        File selectedFile = fileChooser.showOpenDialog(txtImgTerrain.getScene().getWindow());

        if (selectedFile != null) {
            // ✅ Met à jour le champ texte avec le chemin absolu
            txtImgTerrain.setText(selectedFile.getAbsolutePath());

            // ✅ Met à jour l'ImageView avec l'image sélectionnée
            Image image = new Image(selectedFile.toURI().toString());
            terrainImage.setImage(image);
        } else {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune sélection", "Aucune image n'a été sélectionnée !");
        }
    }


    private void modifierTerrain() {
        if (terrainSelectionne == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun terrain sélectionné !");
            return;
        }

        String nom = nametxtfield.getText();
        String lieu = lieutxtfield.getText();
        String description = desctxtfield.getText();
        String img = txtImgTerrain.getText();

        // Vérifier si tous les champs sont remplis
        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || img.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Vérification que le lieu commence par une majuscule
        if (!Character.isUpperCase(lieu.charAt(0))) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le lieu doit commencer par une majuscule !");
            return;
        }

        // Vérification de la longueur du nom et de la description
        if (nom.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le nom doit comporter au moins 3 caractères.");
            return;
        }

        if (description.length() < 10) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 10 caractères.");
            return;
        }

        terrainSelectionne.setNom(nom);
        terrainSelectionne.setLieu(lieu);
        terrainSelectionne.setDes(description);
        terrainSelectionne.setImg(img);

        try {
            serviceTerrain.modifier_t(terrainSelectionne);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain modifié avec succès !");
            fermerFenetre(); // Fermer la fenêtre de modification
            if (homeAfficheTerrainController != null) {
                homeAfficheTerrainController.rafraichirAffichage();
            }
            // Rafraîchir l'affichage
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier le terrain : " + e.getMessage());
        }
    }


    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void fermerFenetre() {
        Stage stage = (Stage) nametxtfield.getScene().getWindow();
        stage.close();
        if (homeAfficheTerrainController != null) {
            homeAfficheTerrainController.rafraichirAffichage();
        }
    }
    private HomeAfficheTerrainController homeAfficheTerrainController;
    public void setHomeAfficheTerrainController(HomeAfficheTerrainController controller) {
        this.homeAfficheTerrainController = controller;
    }
    @FXML
    private void annulerModification() {
        Stage stage = (Stage) annulerbtn.getScene().getWindow();
        stage.close();
    }



}
