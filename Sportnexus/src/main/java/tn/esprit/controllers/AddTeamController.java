package tn.esprit.controllers;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import tn.esprit.entities.Equipe;
import tn.esprit.services.ServiceEquipe;
import javafx.fxml.FXML;
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
    private Button chooseimagebtn;
    @FXML
    private ImageView teamImage;
    private File imageFile;
    private String imagePath = "";  // to store the image path for the team
    private ServiceEquipe serviceEquipe = new ServiceEquipe(); // create an instance of ServiceEquipe
    @FXML
    private Pane addteampane;
    private TeamListController teamListController;
    @FXML
    private Button quitAteambtn;
    @FXML
    private Label errorNom, errorClassement, errorDescription, errorPoints, errorImage;

    public void setTeamListController(TeamListController teamListController) {
        this.teamListController = teamListController;
    }

    @FXML
    public void initialize() {
        // Set default action for the "Add Team" button
        addteambtn.setOnAction(event -> handleAddTeam());

        // Set default action for the "Quit" button
        quitAteambtn.setOnAction(event -> handleQuit());

        // Set default action for the "Choose Image" button
        chooseimagebtn.setOnAction(event -> choisirImage());

        // You can initialize other components here if needed
        // For example, if you have a ComboBox or other controls to set
        // typecombobox.setItems(FXCollections.observableArrayList(TypeMatch.values()));
    }
    // Action when the "Add Team" button is clicked


    @FXML
    public void handleAddTeam() {
        // Réinitialisation des messages d'erreur
        errorNom.setText("");
        errorClassement.setText("");
        errorDescription.setText("");
        errorPoints.setText("");
        errorImage.setText("");

        // Récupération des données
        String nom = nametxtfield.getText().trim();
        String classementStr = classementtxtfield.getText().trim();
        String description = desctxtfield.getText().trim();
        String pointsStr = ptstxtfield.getText().trim();

        boolean isValid = true;

        // Vérification du nom (ne doit pas être vide ou un nombre)
        if (nom.isEmpty()) {
            errorNom.setText("Le nom est requis !");
            isValid = false;
        } else if (nom.matches("\\d+")) {
            errorNom.setText("Le nom ne peut pas être un nombre !");
            isValid = false;
        }

        // Vérification de la description (ne doit pas être vide ou un nombre)
        if (description.isEmpty()) {
            errorDescription.setText("La description est requise !");
            isValid = false;
        } else if (description.matches("\\d+")) {
            errorDescription.setText("La description ne peut pas être un nombre !");
            isValid = false;
        }

        int classement = 0, points = 0;

        // Vérification du classement (doit être un entier positif)
        if (classementStr.isEmpty()) {
            errorClassement.setText("Classement requis !");
            isValid = false;
        } else {
            try {
                classement = Integer.parseInt(classementStr);
                if (classement < 0) {
                    errorClassement.setText("Doit être un nombre positif !");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                errorClassement.setText("Valeur invalide !");
                isValid = false;
            }
        }

        // Vérification des points (doit être un entier positif)
        if (pointsStr.isEmpty()) {
            errorPoints.setText("Points requis !");
            isValid = false;
        } else {
            try {
                points = Integer.parseInt(pointsStr);
                if (points < 0) {
                    errorPoints.setText("Doit être un nombre positif !");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                errorPoints.setText("Valeur invalide !");
                isValid = false;
            }
        }

        // Vérification de l’image
        if (imageFile == null) {
            errorImage.setText("Veuillez sélectionner une image !");
            isValid = false;
        } else {
            String fileName = imageFile.getName().toLowerCase();
            if (!(fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg"))) {
                errorImage.setText("Format d'image invalide !");
                isValid = false;
            }
        }

        // Si une erreur est détectée, on arrête l'exécution
        if (!isValid) return;

        // Gestion du fichier image
        File destinationDir = new File("img/");
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        String newFileName = System.currentTimeMillis() + "_" + imageFile.getName();
        File destinationFile = new File(destinationDir, newFileName);

        try {
            Files.copy(imageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String imgPath = "img/" + newFileName;

            // Création de l'objet Equipe
            Equipe equipe = new Equipe(nom, classement, imgPath, description, points);

            // Enregistrement en base de données
            serviceEquipe.ajouter(equipe);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Équipe ajoutée avec succès !");
            clearFields();

            System.out.println("✅ Image copiée et sauvegardée avec succès : " + imgPath);
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image : " + e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter l'équipe : " + e.getMessage());
        }
    }



    // Action when the "Quit" button is clicked
    @FXML
    public void handleQuit() {
        Stage stage = (Stage) quitAteambtn.getScene().getWindow();
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
