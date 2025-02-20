package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.sql.SQLException;

public class SupprimerTerrainController {

    @FXML
    public TableView<Terrain> terrainTableView;

    @FXML
    private TableColumn<Terrain, String> colNom;

    @FXML
    private TableColumn<Terrain, String> colLieu;

    @FXML
    private TableColumn<Terrain, String> colDescription;

    @FXML
    private Button btnSupprimer;

    private final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @FXML
    public void initialize() {

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        rafraichirAffichage();


        btnSupprimer.setOnAction(event -> supprimerTerrain());
    }

    private void supprimerTerrain() {
        Terrain terrainSelectionne = terrainTableView.getSelectionModel().getSelectedItem();
        if (terrainSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un terrain à supprimer.");
            return;
        }

        try {
            serviceTerrain.supprimer_t(terrainSelectionne.getId_terrain());
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain supprimé avec succès !");
            rafraichirAffichage(); // Mise à jour de la liste après suppression
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de supprimer le terrain : " + e.getMessage());
        }
    }

    private void rafraichirAffichage() {
        try {
            terrainTableView.getItems().clear();
            terrainTableView.getItems().addAll(serviceTerrain.afficher_t());
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
