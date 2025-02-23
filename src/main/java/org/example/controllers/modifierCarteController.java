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
import org.example.entities.Carte;
import org.example.services.IService;
import org.example.services.ServiceCarte;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class modifierCarteController {
    @FXML
    private Button annulerbtn;
    @FXML
    private Button btnModifierCarte;
    @FXML
    private TextField idCarte;
    @FXML
    private TextField nametxtfield;
    @FXML
    private TextField lieutxtfield;
    @FXML
    private TextField desctxtfield;
    @FXML
    private TextField cheminImage;
    @FXML
    private ImageView img;

    private final ServiceCarte serviceCarte = new ServiceCarte();

    @FXML
    public void modifierCarte(ActionEvent event) {
        try {
            String idText = idCarte.getText();
            String rarite = nametxtfield.getText();
            String scoreText = lieutxtfield.getText();
            String userIdText = desctxtfield.getText();
            String image = cheminImage.getText();

            if (idText.isEmpty() || rarite.isEmpty() || scoreText.isEmpty() || userIdText.isEmpty() || image.isEmpty() ) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis");
                return;
            } else if (idText.isBlank() || rarite.isBlank() || scoreText.isBlank() || userIdText.isBlank() || image.isBlank()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Insrer des champs");
                return;
            }else if (!rarite.equals("rare") && !rarite.equals("commune") && !rarite.equals("legandaire")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "En champs 'rarite' inserer 'rare' ou 'commune' ou 'legandaire' ");
                return;
            } else if (!estNumerique(scoreText)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "En champs score inserer valeur numerique");
                return;
            }

            int id = Integer.parseInt(idText);
            int score = Integer.parseInt(scoreText);
            int userId = Integer.parseInt(userIdText);

            Carte carte = new Carte();
            carte.setId(id);
            carte.setRarite(rarite);
            carte.setScore(score);
            carte.setImage(image);
            carte.setId_user(userId);

            int result = serviceCarte.modifier(carte, id);

            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Carte modifiée avec succès");
            } else {
                showAlert(Alert.AlertType.WARNING, "Échec", "Aucune carte");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/afficherAdminCarte.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);

            Stage primaryStage = (Stage) annulerbtn.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void choisirImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            cheminImage.setText(file.getAbsolutePath());
            img.setImage(new Image(file.toURI().toString()));
        }
        else
        {
            showAlert(Alert.AlertType.WARNING, "Échec", "Chooisir chemin validé");
        }
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean estNumerique(String scoreText) {

        return scoreText.matches("\\d+");

    }
    @FXML
    private void handleReturnButtonClick(ActionEvent event) {
        try {

            Parent homePage = FXMLLoader.load(getClass().getResource("/view/gameHome.fxml"));
            Scene homeScene = new Scene(homePage);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
