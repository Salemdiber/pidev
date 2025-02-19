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
import org.example.entities.Event;
import org.example.services.ServiceEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class AfficherEventController {

    @FXML
    public AnchorPane demandeJoinWBContainer;
    @FXML
    public HBox notifCount;
    @FXML
    public ScrollPane coursesScrollPane;
    @FXML
    public Circle profilImgContainer;
    @FXML
    public Circle demandeJoinWBImg;
    @FXML
    public Label courseWBJTitle;

    // Déclaration des éléments FXML
    @FXML
    private ListView<Event> listViewEvent;

    private static final ServiceEvent serviceevent = new ServiceEvent();
    @FXML
    private Button logoutBtn;
    @FXML
    private Button btnajouter;
    @FXML
    private ImageView eventsBtnImg;
    @FXML
    private Button forumBtn;
    @FXML
    private Label courseTitle;
    @FXML
    private Button TeamsBtn;
    @FXML
    private Button collectsBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private ImageView forumBtnImg;
    @FXML
    private TextField searchInput;
    @FXML
    private ImageView productsBtnImg;
    @FXML
    private ImageView btnajouter1;
    @FXML
    private ImageView homeBtnImg;
    @FXML
    private ImageView collectsBtnImg;
    @FXML
    private Pane mainPageContainer;
    @FXML
    private Button eventsBtn;
    @FXML
    private ImageView logoutBtnImg;
    @FXML
    private Label welcomeMsg;
    @FXML
    private ImageView coursesBtnImg;
    @FXML
    private Button productsBtn;

    @FXML
    public void initialize() {
        if (listViewEvent == null) {
            System.out.println("❌ ERREUR : listView est NULL dans initialize()");
            return;
        }
        listViewEvent.setCellFactory(param -> {
            EventListCellController cellController = new EventListCellController();
            cellController.setAfficherController(this);  // 🔹 Passer le contrôleur principal
            return cellController;
        });

        // Charger les données
        chargerEvent();
        listViewEvent.setOnMouseClicked(event -> {
            Event selectedEvent = listViewEvent.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                openEventDetail(selectedEvent);
            }
        });

    }

    private void openEventDetail(Event selectedEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetailsEvent.fxml"));
            Parent root = loader.load();

            DetailsEventController controller = loader.getController();
            controller.setEvent(selectedEvent);
            controller.setAfficherEventController(this); // ✅ Passe le contrôleur principal

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'Événement");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }










    private void chargerEvent() {
        try {
            List<Event> events = serviceevent.afficher_t();
            ObservableList<Event> data = FXCollections.observableArrayList(events);
            listViewEvent.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les evenements : " + e.getMessage());
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
    private void handleAjouterEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AjouterEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre pour AjouterTerrain
            Stage stage = new Stage();
            stage.setTitle("Ajouter un event");
            stage.setScene(new Scene(root));
            //stage.setMaximized(true);
            stage.setResizable(true);
            // Ajouter un écouteur pour détecter la fermeture de la fenêtre et rafraîchir la ListView
            stage.setOnHidden((WindowEvent e) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterEvent.fxml");
        }
    }

    public void rafraichirAffichage() {
        try {
            List<Event> events = serviceevent.afficher_t();
            listViewEvent.setItems(FXCollections.observableArrayList(events));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }



}