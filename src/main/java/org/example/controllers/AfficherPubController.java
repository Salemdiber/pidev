package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.entities.Publication;
import org.example.services.ServicePublication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherPubController {

    @FXML
    public HBox notifCount;
    @FXML
    public ScrollPane coursesScrollPane;
    @FXML
    public Circle profilImgContainer;

    private static final ServicePublication servicePublication = new ServicePublication();
    @FXML
    private Button logoutBtn;
    @FXML
    private Label courseTitle;
    @FXML
    private Button TeamsBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private TextField searchInput;
    @FXML
    private Pane mainPageContainer;
    @FXML
    private Label welcomeMsg;
    @FXML
    private Button btnajouter;
    @FXML
    private ListView<Publication> listviewPub;
    @FXML
    private Label courseWBJTitle;
    @FXML
    private ImageView eventsBtnImg;
    @FXML
    private Button forumBtn;
    @FXML
    private Circle demandeJoinWBImg;
    @FXML
    private Button collectsBtn;
    @FXML
    private AnchorPane demandeJoinWBContainer;
    @FXML
    private ImageView forumBtnImg;
    @FXML
    private ImageView productsBtnImg;
    @FXML
    private ImageView homeBtnImg;
    @FXML
    private Button btnsupprimer;
    @FXML
    private ImageView collectsBtnImg;
    @FXML
    private Button eventsBtn;
    @FXML
    private ImageView logoutBtnImg;
    @FXML
    private Button btnmodifier;
    @FXML
    private ImageView coursesBtnImg;
    @FXML
    private ImageView btnsupprimer1;
    @FXML
    private Button productsBtn;
    @FXML
    private ImageView btnmodifier1;

    @FXML
    public void initialize() {
        if (listviewPub == null) {
            System.out.println("❌ ERREUR : listViewTerrains est NULL dans initialize()");
            return;
        }
        listviewPub.setCellFactory(param -> new PubListCellController());

        // Charger les données
        chargerTerrains();

        // Ajouter des actions aux boutons
        //btnmodifier.setOnAction(event -> ouvrirFenetreModification());
        btnsupprimer.setOnAction(event -> supprimerTerrain());

        listviewPub.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifiez si c'est un double-clic
                Publication selectedPub = listviewPub.getSelectionModel().getSelectedItem();
                if (selectedPub != null) {
                    openTerrainDetail(selectedPub);
                }
            }
        });
    }

    private void openTerrainDetail(Publication selectedPub) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsPub.fxml"));
            Parent root = loader.load();

           DetailsPubController controller = loader.getController();
            controller.setPublication(selectedPub);
            controller.setAfficherPubController(this); // Pass reference for refreshing

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du publication");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    private void chargerTerrains() {
        try {
            List<Publication> pubs = servicePublication.afficher_t();
            ObservableList<Publication> data = FXCollections.observableArrayList(pubs);
            listviewPub.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les pubs : " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjoutPub.fxml"));
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
            List<Publication> terrains = servicePublication.afficher_t();
            listviewPub.setItems(FXCollections.observableArrayList(terrains));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    @FXML
    public void supprimerTerrain() {
        Publication pubSelectionne = listviewPub.getSelectionModel().getSelectedItem();
        if (pubSelectionne == null) {
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
                servicePublication.supprimer_t(pubSelectionne.getIdPub());
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Terrain supprimé avec succès !");
                rafraichirAffichage();
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le terrain : " + e.getMessage());
            }
        }
    }

    private int getCurrentUserId() {
        return 1; // Remplacez ceci par la logique pour obtenir l'ID de l'utilisateur actuel
    }

    @Deprecated
    public void handleAfficherReservations(ActionEvent actionEvent) {
    }

    @FXML
    public void ouvrirFenetreModification(ActionEvent actionEvent) {
    }






}