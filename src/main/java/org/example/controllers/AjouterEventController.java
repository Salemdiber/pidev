
package org.example.controllers;

import org.example.entities.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.services.ServiceEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class AjouterEventController {


    @FXML
    private TextField nametxtfield ;
    @FXML
    private TextField lieutxtfield ;
    @FXML
    private TextField desctxtfield ;
    @FXML
    private DatePicker datetf;
    @FXML
    private ComboBox<Integer> hourComboBox;
    @FXML
    private ComboBox<Integer> minuteComboBox;

    @FXML
    private Button btnChoisirImage;
    @FXML
    private Button annulerbtn;


    private final ServiceEvent serviceEvent = new ServiceEvent();
    private File imageFile; // Stocke le fichier image sélectionné
    @FXML
    private ImageView imageview;
    @FXML
    private Button ajouterbtn;
    @FXML
    private Button btnreturn;

    @FXML
    public void initialize() {
        hourComboBox.getItems().addAll(IntStream.range(0, 24).boxed().toList());
        minuteComboBox.getItems().addAll(IntStream.range(0, 60).boxed().toList());

        hourComboBox.setValue(12);
        minuteComboBox.setValue(0);

        btnChoisirImage.setOnAction(event -> choisirImage());


        ajouterbtn.setOnAction(event -> ajouterEvent());
    }
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Sauvegarde uniquement le nom du fichier au lieu du chemin absolu
            imageFile = selectedFile;
            imageview.setImage(new Image(imageFile.toURI().toString()));
        }
    }
    private void ajouterEvent() {
        String nom = nametxtfield.getText();
        String lieu = lieutxtfield.getText();
        String description = desctxtfield.getText();
        LocalDate date = datetf.getValue();
        Integer hour = hourComboBox.getValue();
        Integer minute = minuteComboBox.getValue();

        // Vérifier si une image a été sélectionnée
        if (nom.isEmpty() || lieu.isEmpty() || description.isEmpty() || imageFile == null || date == null || hour == null || minute == null) {
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

        hour = Integer.parseInt(hourComboBox.getValue().toString());
        minute = Integer.parseInt(minuteComboBox.getValue().toString());
        LocalDateTime dateTime2 = datetf.getValue().atTime(hour, minute);
        LocalDateTime today = LocalDateTime.now();
        if (dateTime2.isBefore(today)) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Vous ne pouvez pas planifier un event pour une date passée.");
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

            LocalDateTime dateTime = date.atTime(hour, minute);

            Event E = new Event(nom,imgPath,description,lieu,dateTime);


            // 📤 Enregistrement en base de données
            serviceEvent.ajouter_t(E);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Evenement ajouté avec succès !");
            clearFields();

            System.out.println("✅ Image copiée et sauvegardée avec succès : " + imgPath);
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image : " + e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter l'evenement : " + e.getMessage());
        }
    }
    private void clearFields() {
        nametxtfield.clear();
        lieutxtfield.clear();
        desctxtfield.clear();
        imageview.setImage(null); // Réinitialise l'image
        imageFile = null; // Réinitialise le fichier image
        datetf.setValue(null);
        hourComboBox.setValue(12);
        minuteComboBox.setValue(0);

    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleReturnButtonClick() {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) btnreturn.getScene().getWindow();
        stage.close(); // Cela fermera la fenêtre actuelle
    }
}






