package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.entities.SessionManager;
import org.example.entities.Terrain;
import org.example.entities.User;
import org.example.services.ServiceTerrain;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;


public class TerrainDetailController {
    @FXML
    private Label nomLabelDetail;
    @FXML
    private Label lieuLabelDetail;
    @FXML
    private Label desLabelDetail;
    @FXML
    private Label userRoleLabel;
    @FXML
    private ImageView terrainImageViewDetail;
    @FXML
    private Button btnreserver;
    @FXML
    private Button btnmodifier;
    @FXML
    private Button btnsupprimer;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> heureComboBox;
    private Terrain terrain;
    private final ServiceTerrain serviceTerrain = new ServiceTerrain();
    @FXML
    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            userRoleLabel.setText("Rôle: " + currentUser.getRole());
        } else {
            userRoleLabel.setText("Utilisateur non connecté");
        }
        btnreserver.setOnAction(event -> reserverTerrain());
        btnmodifier.setOnAction(event -> modifierTerrain(event));
    }
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        if (terrain != null) {
            nomLabelDetail.setText(terrain.getNom());
            lieuLabelDetail.setText(terrain.getLieu());
            desLabelDetail.setText(terrain.getDes());


            if (terrain.getImg() != null && !terrain.getImg().isEmpty()) {
                Image image = new Image("file:" + terrain.getImg());
                terrainImageViewDetail.setImage(image);
            }
        }
    }
    @FXML

    private void reserverTerrain() {
        if (terrain == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Aucun terrain sélectionné.");
            return;
        }

        LocalDate dateReservation = datePicker.getValue();
        String heureReservation = heureComboBox.getValue();

        if (dateReservation == null || heureReservation == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une date et une heure.");
            return;
        }


        LocalDate today = LocalDate.now();
        if (dateReservation.isBefore(today)) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Vous ne pouvez pas réserver un terrain pour une date passée.");
            return;
        }

        int idTerrain = terrain.getId_terrain();
        int idUser = getCurrentUserId();
        String dateHeureReservation = dateReservation.toString() + " " + heureReservation;

        try {

            if (!serviceTerrain.isTerrainAvailable(idTerrain, dateHeureReservation)) {
                afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Le terrain est déjà réservé à cette date et heure.");
                return;
            }

            serviceTerrain.reserverTerrain(idTerrain, idUser, dateHeureReservation);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain réservé avec succès !");
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de réserver le terrain : " + e.getMessage());
        }
    }
    @FXML
    private void supprimerTerrain(ActionEvent event) {
        if (terrain == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Aucun terrain sélectionné.");
            return;
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce terrain ?");
        alert.setContentText("Cette action est irréversible.");


        if (alert.showAndWait().get() == ButtonType.OK) {
            try {

                serviceTerrain.supprimer_t(terrain.getId_terrain());


                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain supprimé avec succès !");


                handleReturnButtonClick(event);

            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de supprimer le terrain : " + e.getMessage());
            }
        }
    }
    private int getCurrentUserId() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getIdUser();
        } else {

            return 0;
        }
    }
    @FXML
    private void modifierTerrain(ActionEvent event) {
        if (terrain == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Aucun terrain sélectionné.");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateTerrain.fxml"));
            Parent root = loader.load();


            ModifierTerrainController updateController = loader.getController();
            updateController.setTerrain(terrain);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
        }
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
