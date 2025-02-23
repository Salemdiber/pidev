package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.entities.SessionManager;
import org.example.entities.Terrain;
import org.example.services.ServiceTerrain;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HomeAfficheTerrainController {




    @FXML
    private ListView<Terrain> listViewTerrains;

    @FXML
    private Button btnsupprimer;
    @FXML
    private Button btnmodifier;
    @FXML
    private TextField searchbar;

    @FXML
    private Button eventbtn;
    @FXML
    private Button btnajouter;
    @FXML
    private Button btnreserver1;
    private ObservableList<Terrain> terrainList;
    private FilteredList<Terrain> filteredList;
    private static final ServiceTerrain serviceTerrain = new ServiceTerrain();

    @FXML
    public void initialize() {
        if (listViewTerrains == null) {
            System.out.println("❌ ERREUR : listViewTerrains est NULL dans initialize()");
            return;
        }
        listViewTerrains.setCellFactory(param -> new TerrainListCellController());


        chargerTerrains();
        filteredList = new FilteredList<>(terrainList, p -> true);
        listViewTerrains.setItems(filteredList);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(terrain -> {
                // If the search bar is empty, display all terrains
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                String lowerCaseFilter = newValue.toLowerCase();
                return terrain.getNom().toLowerCase().contains(lowerCaseFilter); // Assuming Terrain has a getName() method
            });
        });


        listViewTerrains.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Terrain selectedTerrain = listViewTerrains.getSelectionModel().getSelectedItem();
                if (selectedTerrain != null) {
                    openTerrainDetail(selectedTerrain, new ActionEvent(event.getSource(), null));
                }
            }
        });



        String role = SessionManager.getInstance().getUserRole();
        System.out.println("User Role: " + role);
        if ("Admin".equals(role)) {
            btnajouter.setDisable(false);
            btnreserver1.setDisable(false);
        } else {
            btnajouter.setDisable(true);
            btnreserver1.setDisable(true);
        }


    }





    private void openTerrainDetail(Terrain selectedTerrain, ActionEvent event) {
        if (selectedTerrain == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun terrain sélectionné.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TerrainDetail.fxml"));
            Parent root = loader.load();

            TerrainDetailController controller = loader.getController();
            controller.setTerrain(selectedTerrain);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les détails du terrain : " + e.getMessage());
        }
    }

    private void chargerTerrains() {
        try {
            List<Terrain> terrains = serviceTerrain.afficher_t();
            terrainList = FXCollections.observableArrayList(terrains); // Store the original list
            filteredList = new FilteredList<>(terrainList, p -> true); // Initialize the filtered list
            listViewTerrains.setItems(filteredList); // Set the filtered list to the ListView
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
    private void handleAjouterTerrain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTerrain.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
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
@FXML
private void handleAfficherReservations(ActionEvent event) {
    try {
        Parent reservationsPage = FXMLLoader.load(getClass().getResource("/view/Listereservations.fxml"));
        Scene scene = new Scene(reservationsPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre des réservations : " + e.getMessage());
    }
}


    @FXML
    private void handleOuvrirForum(ActionEvent event) throws IOException {
        Parent trajetPage = FXMLLoader.load(getClass().getResource("/view/AfficherPub.fxml"));
        Scene scene = new Scene(trajetPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleOuvrirGame(ActionEvent event) throws IOException {
        Parent trajetPage = FXMLLoader.load(getClass().getResource("/view/gameHome.fxml"));
        Scene scene = new Scene(trajetPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleOuvrirTeam(ActionEvent event) throws IOException {
        Parent trajetPage = FXMLLoader.load(getClass().getResource("/view/team_home1.fxml"));
        Scene scene = new Scene(trajetPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleOuvrirEvent(ActionEvent event) throws IOException {
        Parent trajetPage = FXMLLoader.load(getClass().getResource("/view/AfficherEvent.fxml"));
        Scene scene = new Scene(trajetPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion : " + e.getMessage());
        }
    }
    @FXML
    private void handleHome(ActionEvent event) throws IOException {
        Parent trajetPage = FXMLLoader.load(getClass().getResource("/view/HomePage.fxml"));
        Scene scene = new Scene(trajetPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}