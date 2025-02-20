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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class AjouterTerrainController {


    @FXML
    private TextField nametxtfield ;
    @FXML
    private TextField lieutxtfield ;
    @FXML
    private TextField desctxtfield ;
    @FXML
    private ImageView terrainImage;

    @FXML
    private Button btnChoisirImage;
    @FXML
    private Button ajouterbtn;
    @FXML
    private Button annulerbtn;


    private final ServiceTerrain serviceTerrain = new ServiceTerrain();
    private File imageFile;

    @FXML
    public void initialize() {
        btnChoisirImage.setOnAction(event -> choisirImage());
        ajouterbtn.setOnAction(event -> ajouterTerrain());
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
        String nom = nametxtfield.getText();
        String lieu = lieutxtfield.getText();
        String description = desctxtfield.getText();


        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || imageFile == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }


        if (nom.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le nom doit comporter au moins 3 caractères.");
            return;
        }
        if (!Character.isUpperCase(lieu.charAt(0))) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le lieu doit commencer par une majuscule !");
            return;
        }

        if (description.length() < 10) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 10 caractères.");
            return;
        }


        String fileExtension = getFileExtension(imageFile);
        if (!fileExtension.equals("png") && !fileExtension.equals("jpg") && !fileExtension.equals("jpeg")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'image doit être au format PNG, JPG ou JPEG.");
            return;
        }


        File destinationDir = new File("img/");
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        String newFileName = System.currentTimeMillis() + "_" + imageFile.getName();
        File destinationFile = new File(destinationDir, newFileName);

        try {

            Files.copy(imageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


            String imgPath = "img/" + newFileName;

            Terrain terrain = new Terrain(nom, lieu, description, imgPath);


            serviceTerrain.ajouter_t(terrain);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain ajouté avec succès !");
            clearFields();
            Stage stage = (Stage) ajouterbtn.getScene().getWindow();
           ;
            System.out.println("✅ Image copiée et sauvegardée avec succès : " + imgPath);
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image : " + e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le terrain : " + e.getMessage());
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1).toLowerCase();
    }

    private void clearFields() {
        nametxtfield.clear();
        lieutxtfield.clear();
        desctxtfield.clear();
        terrainImage.setImage(null);
        imageFile = null;
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
