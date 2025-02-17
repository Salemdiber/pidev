package tn.esprit.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import tn.esprit.entities.Equipe;
import tn.esprit.services.ServiceEquipe;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import static tn.esprit.controllers.TeamhomePController.afficherAlerte;

public class AddTeamController {

    @FXML
    private TextField nametxtfield;
    @FXML
    private TextField classementtxtfield;
    @FXML
    private TextField desctxtfield;
    @FXML
    private TextField ptstxtfield;
    @FXML
    private Button addteambtn;
    @FXML
    private Button quitteambtn;
    @FXML
    private Button chooseimagebtn;
    @FXML
    private ImageView teamImage;
    private File imageFile;
    private String imagePath = "";  // to store the image path for the team
    private ServiceEquipe serviceEquipe = new ServiceEquipe(); // create an instance of ServiceEquipe
    @FXML
    private Pane addteampane;

    @FXML
    public void initialize() {
        // Set default action for the "Add Team" button
        addteambtn.setOnAction(event -> handleAddTeam());

        // Set default action for the "Quit" button
        quitteambtn.setOnAction(event -> handleQuit());

        // Set default action for the "Choose Image" button
        chooseimagebtn.setOnAction(event -> choisirImage());

        // You can initialize other components here if needed
        // For example, if you have a ComboBox or other controls to set
        // typecombobox.setItems(FXCollections.observableArrayList(TypeMatch.values()));
    }
    // Action when the "Add Team" button is clicked
    @FXML
    public void handleAddTeam() {
        // Get input data from the user
        String nom = nametxtfield.getText();
        int classement = Integer.parseInt(classementtxtfield.getText());
        String description = desctxtfield.getText();
        int points = Integer.parseInt(ptstxtfield.getText());

        // Validate input
        if (nom.isEmpty() ||  description.isEmpty() ||imageFile==null) {
            showAlert("Error", "Please fill in all fields and select an image.");
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

            Equipe equipe = new Equipe(nom,classement,imgPath,description,points);

            // 📤 Enregistrement en base de données
            serviceEquipe.ajouter(equipe);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Equipe ajouté avec succès !");
            clearFields();

            System.out.println("✅ Image copiée et sauvegardée avec succès : " + imgPath);
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image : " + e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter l'equipe : " + e.getMessage());
        }
    }

    // Action when the "Quit" button is clicked
    @FXML
    public void handleQuit() {
        Stage stage = (Stage) quitteambtn.getScene().getWindow();
        stage.close();
    }

    // Action when the "Choose Image" button is clicked
    @FXML
    public void handleChooseImage() {
        // Open a file chooser to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();  // Get the file path of the selected image
            teamImage.setImage(new javafx.scene.image.Image("file:///" + imagePath)); // Display the selected image
        }
    }
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Sauvegarde uniquement le nom du fichier au lieu du chemin absolu
            imageFile = selectedFile;
            teamImage.setImage(new Image(imageFile.toURI().toString()));

            // Copier l'image sélectionnée vers le dossier resources/img/ (À FAIRE)
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method to clear all input fields
    private void clearFields() {
        nametxtfield.clear();
        classementtxtfield.clear();
        desctxtfield.clear();
        ptstxtfield.clear();
        teamImage.setImage(null);
    }
}
