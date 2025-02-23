package org.example.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.entities.Reservation;
import org.example.entities.Terrain;
import org.example.services.ServiceReservation;
import org.example.services.ServiceTerrain;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerReservationController  {
    @FXML
    public TableView<Reservation> tableViewReservations;
    @FXML
    private TableColumn<Reservation, String> colDateRes;

    @FXML
    private TableColumn<Reservation, Integer> colIduser;

    @FXML
    private TableColumn<Reservation, Integer> colIdterrain;
    @FXML
    private Button btnsupprimerreservation;

    private final ServiceReservation serviceReservation = new ServiceReservation();

    @FXML
    public void initialize() {
        colDateRes.setCellValueFactory(new PropertyValueFactory<>("date reservation"));
        colIduser.setCellValueFactory(new PropertyValueFactory<>("id user"));
        colIdterrain.setCellValueFactory(new PropertyValueFactory<>("id terrain"));


        rafraichirAffichage();


        btnsupprimerreservation.setOnAction(event -> supprimerReservation());
    }

    private void supprimerReservation() {
        Reservation reservationSelectionne = tableViewReservations.getSelectionModel().getSelectedItem();
        if (reservationSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un reservation à supprimer.");
            return;
        }

        try {
            serviceReservation.supprimer_t(reservationSelectionne.getId_res());
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Reservation supprimé avec succès !");
            rafraichirAffichage(); // Mise à jour de la liste après suppression
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de supprimer le reservation : " + e.getMessage());
        }
    }

    private void rafraichirAffichage() {
        try {
            tableViewReservations.getItems().clear();
            tableViewReservations.getItems().addAll(serviceReservation.afficher_t());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les terrains : " + e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
