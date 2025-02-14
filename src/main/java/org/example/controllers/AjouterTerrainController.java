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
    private File imageFile; // Stocke le fichier image sélectionné

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
            // Sauvegarde uniquement le nom du fichier au lieu du chemin absolu
            imageFile = selectedFile;
            terrainImage.setImage(new Image(imageFile.toURI().toString()));

            // Copier l'image sélectionnée vers le dossier resources/img/ (À FAIRE)
        }
    }
    private void ajouterTerrain() {
        String nom = nametxtfield.getText();
        String lieu = lieutxtfield.getText();
        String description = desctxtfield.getText();

        // Vérifier si une image a été sélectionnée
        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || imageFile == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // 📂 Définir le dossier de destination des images
        File destinationDir = new File("img/");
        if (!destinationDir.exists()) {
            destinationDir.mkdirs(); // Crée le dossier s'il n'existe pas
        }

        // 🎯 Nom de fichier unique pour éviter les conflits
        String newFileName = System.currentTimeMillis() + "_" + imageFile.getName();
        File destinationFile = new File(destinationDir, newFileName);

        try {
            // 📥 Copier l'image sélectionnée vers le dossier des uploads
            Files.copy(imageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 📌 Enregistrer uniquement le nom du fichier dans la base de données
            String imgPath = "img/" + newFileName;

            Terrain terrain = new Terrain(nom, lieu, description, imgPath);

            // 📤 Enregistrement en base de données
            serviceTerrain.ajouter_t(terrain);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain ajouté avec succès !");
            clearFields();

            System.out.println("✅ Image copiée et sauvegardée avec succès : " + imgPath);
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image : " + e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le terrain : " + e.getMessage());
        }
    }
    private void clearFields() {
        nametxtfield.clear();
        lieutxtfield.clear();
        desctxtfield.clear();
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
