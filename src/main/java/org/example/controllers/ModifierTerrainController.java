package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            txtImgTerrain.setText(selectedFile.toURI().toString()); // Stocke le chemin de l'image
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

        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || img.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
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



}
