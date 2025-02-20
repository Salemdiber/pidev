package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.entities.Reservation;
import org.example.services.ServiceReservation;
import org.example.services.ServiceTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;



public class ModifierReservationController implements Initializable {

    @FXML
    private ComboBox<String> nameComboBox;

    @FXML
    private DatePicker datePickerRes;

    @FXML
    private ComboBox<String> heureComboBoxRes;

    @FXML
    private Button btnModifierRes;

    private Reservation reservationToModify;
    private final ServiceReservation serviceReservation = new ServiceReservation();
    private static final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        heureComboBoxRes.getItems().addAll(
                "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
        );
        try {
            List<String> nomsTerrains = serviceTerrain.getAllTerrainNames();
            nameComboBox.getItems().addAll(nomsTerrains);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de charger les noms des terrains : " + e.getMessage());
        }

        btnModifierRes.setOnAction(event -> modifierReservation());
    }


    public void setReservation(Reservation reservation) {
        this.reservationToModify = reservation;


        nameComboBox.setValue(reservation.getNomTerrain());
        LocalDateTime dateTime = reservation.getDate_res().toLocalDateTime();

        datePickerRes.setValue(dateTime.toLocalDate());


        String heureExistante = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        heureComboBoxRes.setValue(heureExistante);
    }


    private void modifierReservation() {
        if (reservationToModify == null) {
            afficherAlerte("Erreur", "Aucune réservation sélectionnée.");
            return;
        }


        String nouveauNomTerrain = nameComboBox.getValue();
        LocalDate nouvelleDate = datePickerRes.getValue();
        String nouvelleHeure = heureComboBoxRes.getValue();

        if (nouveauNomTerrain == null || nouvelleDate == null || nouvelleHeure == null) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        LocalDate aujourdHui = LocalDate.now();
        if (nouvelleDate.isBefore(aujourdHui)) {
            afficherAlerte("Erreur", "La date sélectionnée ne peut pas être dans le passé.");
            return;
        }

        LocalTime heure = LocalTime.parse(nouvelleHeure, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dateTime = LocalDateTime.of(nouvelleDate, heure);


        reservationToModify.setNomTerrain(nouveauNomTerrain);
        reservationToModify.setDate_res(Timestamp.valueOf(dateTime));


        try {
            int idTerrain = serviceTerrain.getTerrainIdByName(nouveauNomTerrain);
            reservationToModify.setId_terrain(idTerrain);
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Impossible de récupérer l'ID du terrain : " + e.getMessage());
            return;
        }

        try {

            serviceReservation.modifier_t(reservationToModify);


            afficherAlerte("Succès", "La réservation a été modifiée avec succès.");


            Stage stage = (Stage) btnModifierRes.getScene().getWindow();

        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de modifier la réservation : " + e.getMessage());
        }
    }


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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