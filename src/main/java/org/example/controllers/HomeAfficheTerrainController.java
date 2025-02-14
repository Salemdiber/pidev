package org.example.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
    private TableView<Terrain> tableViewTerrains;
    @FXML
    private TableColumn<Terrain, Integer> colId;
    @FXML
    private TableColumn<Terrain, String> colNom, colLieu, colDesc, colImg;
    @FXML
    private Button btnsupprimer;
    @FXML
    private Button btnmodifier;

    @FXML
    private Button btnreserver;
    @FXML
    private Button btnreserver1;




    private static final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @FXML
    public void initialize() {
        if (tableViewTerrains == null) {
            System.out.println("❌ ERREUR : tableViewTerrains est NULL dans initialize()");
            return;
        }

        // Lier les colonnes avec les propriétés de Terrain
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId_terrain()));
        colNom.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNom()));
        colLieu.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLieu()));
        colDesc.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDes()));
        colImg.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getImg()));

        // Charger les données
        chargerTerrains();

        // Ajouter des actions aux boutons
        btnmodifier.setOnAction(event -> ouvrirFenetreModification());
        btnsupprimer.setOnAction(event -> supprimerTerrain());
        btnreserver.setOnAction(event -> reserverTerrain());
        btnreserver1.setOnAction(event -> handleAfficherReservations());

    }

    private void chargerTerrains() {
        try {
            List<Terrain> terrains = serviceTerrain.afficher_t();
            ObservableList<Terrain> data = FXCollections.observableArrayList(terrains);
            tableViewTerrains.setItems(data);
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

            // Ajouter un écouteur pour détecter la fermeture de la fenêtre et rafraîchir la TableView
            stage.setOnHidden((WindowEvent e) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AddTerrain.fxml");
        }
    }
    public  void rafraichirAffichage() {
        try {
            List<Terrain> terrains = serviceTerrain.afficher_t();
            tableViewTerrains.setItems(FXCollections.observableArrayList(terrains));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }
    public void supprimerTerrain() {
        Terrain terrainSelectionne = tableViewTerrains.getSelectionModel().getSelectedItem();
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
            } catch (SQLException e){
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le terrain : " + e.getMessage());
            }
        }
    }

    public void ouvrirFenetreModification() {
        if (tableViewTerrains == null) { // Vérifie si l'élément est bien chargé
            System.out.println("❌ ERREUR : tableTerrain n'est pas initialisé !");
            return;
        }
        Terrain terrain = tableViewTerrains.getSelectionModel().getSelectedItem();
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void reserverTerrain() {
        Terrain terrainSelectionne = tableViewTerrains.getSelectionModel().getSelectedItem();

        if (terrainSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un terrain à réserver.");
            return;
        }

        int idTerrain = terrainSelectionne.getId_terrain();
        int idUser = getCurrentUserId();
        String dateReservation = java.time.LocalDate.now().toString();

        try {
            serviceTerrain.reserverTerrain(idTerrain, idUser, dateReservation);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain réservé avec succès !");
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de réserver le terrain : " + e.getMessage());
        }
    }
    private int getCurrentUserId() {

        return 1;
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
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre des réservations : " + e.getMessage());
        }
    }



}
