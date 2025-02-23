package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.io.IOException;
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

            txtImgTerrain.setText(selectedFile.getAbsolutePath());


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


        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || img.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }


        if (!Character.isUpperCase(lieu.charAt(0))) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le lieu doit commencer par une majuscule !");
            return;
        }


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

            if (homeAfficheTerrainController != null) {
                homeAfficheTerrainController.rafraichirAffichage();
            }

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
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {

            Parent homePage = FXMLLoader.load(getClass().getResource("/view/homeAffiche.fxml"));
            Scene homeScene = new Scene(homePage);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
