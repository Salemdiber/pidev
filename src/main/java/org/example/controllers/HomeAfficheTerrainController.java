package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HomeAfficheTerrainController {

    public AnchorPane demandeJoinWBContainer;
    public HBox notifCount;
    public ScrollPane coursesScrollPane;
    public Circle profilImgContainer;
    public Circle demandeJoinWBImg;
    public Label courseWBJTitle;

    // Déclaration des éléments FXML
    @FXML
    private ListView<Terrain> listViewTerrains; // Remplace TableView par ListView

    @FXML
    private Button btnsupprimer;
    @FXML
    private Button btnmodifier;

    @FXML
    private Button btnreserver1;

    private static final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @FXML
    public void initialize() {
        if (listViewTerrains == null) {
            System.out.println("❌ ERREUR : listViewTerrains est NULL dans initialize()");
            return;
        }
        listViewTerrains.setCellFactory(param -> new TerrainListCellController());

        // Charger les données
        chargerTerrains();

        // Ajouter des actions aux boutons
        btnmodifier.setOnAction(event -> ouvrirFenetreModification());
        btnsupprimer.setOnAction(event -> supprimerTerrain());

        btnreserver1.setOnAction(event -> handleAfficherReservations());
        listViewTerrains.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifiez si c'est un double-clic
                Terrain selectedTerrain = listViewTerrains.getSelectionModel().getSelectedItem();
                if (selectedTerrain != null) {
                    openTerrainDetail(selectedTerrain);
                }
            }
        });
    }

    private void openTerrainDetail(Terrain selectedTerrain) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TerrainDetail.fxml"));
            Parent root = loader.load();

            TerrainDetailController controller = loader.getController();
            controller.setTerrain(selectedTerrain);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du Terrain");

            stage.setMaximized(true); // Plein écran fenêtré
            stage.setResizable(false); // Empêche le redimensionnement

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    private void chargerTerrains() {
        try {
            List<Terrain> terrains = serviceTerrain.afficher_t();
            ObservableList<Terrain> data = FXCollections.observableArrayList(terrains);
            listViewTerrains.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les terrains : " + e.getMessage());
        }
    }

    private static void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAjouterTerrain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTerrain.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre pour AjouterTerrain
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Terrain");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);
            // Ajouter un écouteur pour détecter la fermeture de la fenêtre et rafraîchir la ListView
            stage.setOnHidden((WindowEvent e) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("Erreur lors du chargement de AddTerrain.fxml");
        }
    }

    public void rafraichirAffichage() {
        try {
            List<Terrain> terrains = serviceTerrain.afficher_t();
            listViewTerrains.setItems(FXCollections.observableArrayList(terrains));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    public void supprimerTerrain() {
        Terrain terrainSelectionne = listViewTerrains.getSelectionModel().getSelectedItem();
        if (terrainSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un terrain à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce terrain ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceTerrain.supprimer_t(terrainSelectionne.getId_terrain());
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain supprimé avec succès !");
                rafraichirAffichage();
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le terrain : " + e.getMessage());
            }
        }
    }

    public void ouvrirFenetreModification() {
        if (listViewTerrains == null) { // Vérifie si l'élément est bien chargé
            System.out.println("❌ ERREUR : listViewTerrains n'est pas initialisé !");
            return;
        }
        Terrain terrain = listViewTerrains.getSelectionModel().getSelectedItem();
        if (terrain == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un terrain à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateTerrain.fxml"));
            Parent root = loader.load();

            ModifierTerrainController controller = loader.getController();
            controller.setTerrain(terrain);  // Passer le terrain sélectionné
            controller.setHomeAfficheTerrainController(this);
            Stage stage = new Stage();
            stage.setTitle("Modifier un terrain");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleAfficherReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Listereservations.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Réservations");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setMaximized(true);
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre des réservations : " + e.getMessage());
        }
    }
}