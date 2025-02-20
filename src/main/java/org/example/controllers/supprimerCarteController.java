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
import javafx.stage.Stage;
import org.example.services.IService;
import org.example.services.ServiceCarte;

import java.io.IOException;
import java.sql.SQLException;

public class supprimerCarteController {

    @FXML
    private Button annulerbtn;
    @FXML
    private Button btnSupprimerCarte;
    @FXML
    private TextField idCarte;

    private final ServiceCarte serviceCarte = new ServiceCarte();

    @FXML
    public void supprimerCarte(ActionEvent event) {
        try {
            String idText = idCarte.getText();
            if (idText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un ID de carte");
                return;
            }
            int id = Integer.parseInt(idText);
            serviceCarte.supprimer_t(id);

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

    private void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
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
