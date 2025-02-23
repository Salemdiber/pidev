package org.example.controllers;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.Equipe;
import org.example.entities.SessionManager;
import org.example.services.ServiceEquipe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import static org.example.controllers.TeamhomePController.afficherAlerte;

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
    private String imagePath = "";
    private ServiceEquipe serviceEquipe = new ServiceEquipe();
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
        addteambtn.setOnAction(event -> handleAddTeam());
        quitAteambtn.setOnAction(event -> handleQuit());
        chooseimagebtn.setOnAction(event -> choisirImage());
        String role = SessionManager.getInstance().getUserRole();
        if (!"Admin".equals(role)) {
            addteambtn.setDisable(true);
        }


    }


    @FXML
    public void handleAddTeam() {
        // Réinitialisation des messages d'erreur
        errorNom.setText("");
        errorClassement.setText("");
        errorDescription.setText("");
        errorPoints.setText("");
        errorImage.setText("");


        String nom = nametxtfield.getText().trim();
        String classementStr = classementtxtfield.getText().trim();
        String description = desctxtfield.getText().trim();
        String pointsStr = ptstxtfield.getText().trim();

        boolean isValid = true;


        if (nom.isEmpty()) {
            errorNom.setText("Le nom est requis !");
            isValid = false;
        } else if (nom.matches("\\d+")) {
            errorNom.setText("Le nom ne peut pas être un nombre !");
            isValid = false;
        }


        if (description.isEmpty()) {
            errorDescription.setText("La description est requise !");
            isValid = false;
        } else if (description.matches("\\d+")) {
            errorDescription.setText("La description ne peut pas être un nombre !");
            isValid = false;
        }

        int classement = 0, points = 0;


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


        if (!isValid) return;


        File destinationDir = new File("img/");
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        String newFileName = System.currentTimeMillis() + "_" + imageFile.getName();
        File destinationFile = new File(destinationDir, newFileName);

        try {
            Files.copy(imageFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String imgPath = "img/" + newFileName;


            Equipe equipe = new Equipe(nom, classement, imgPath, description, points);


            serviceEquipe.ajouter_t(equipe);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Équipe ajoutée avec succès !");
            clearFields();


            System.out.println("✅ Image copiée et sauvegardée avec succès : " + imgPath);
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image : " + e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter l'équipe : " + e.getMessage());
        }
    }




    @FXML
    public void handleQuit() {
        Stage stage = (Stage) quitAteambtn.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void handleChooseImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            teamImage.setImage(new javafx.scene.image.Image("file:///" + imagePath));
        }
    }
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {

            imageFile = selectedFile;
            teamImage.setImage(new Image(imageFile.toURI().toString()));


        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void clearFields() {
        nametxtfield.clear();
        classementtxtfield.clear();
        desctxtfield.clear();
        ptstxtfield.clear();
        teamImage.setImage(null);
    }
}
