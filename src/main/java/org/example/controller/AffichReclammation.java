package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.geometry.Insets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javafx.stage.Stage;
import org.example.entities.Reclammation;
import org.example.entities.SessionManager;
import org.example.entities.User;
import org.example.services.ServiceReclammation;

public class AffichReclammation {

    @FXML
    private ListView<Reclammation> listRec;  // The ListView to display reclamations
    private final ServiceReclammation serviceReclammation = new ServiceReclammation(); // Service to fetch reclamation data
    @FXML
    private Button logoutBtn;
    @FXML
    private Button btnAjouterRec;
    @FXML
    private Label courseWBJTitle;
    @FXML
    private ImageView eventsBtnImg;
    @FXML
    private Button forumBtn;
    @FXML
    private Circle demandeJoinWBImg;
    @FXML
    private Button TeamsBtn;
    @FXML
    private Button collectsBtn;
    @FXML
    private AnchorPane demandeJoinWBContainer;
    @FXML
    private HBox notifCount;
    @FXML
    private Button homeBtn;
    @FXML
    private Button btnSupprimerRec;
    @FXML
    private ImageView forumBtnImg;
    @FXML
    private TextField searchInput;
    @FXML
    private ImageView productsBtnImg;
    @FXML
    private ImageView homeBtnImg;
    @FXML
    private ImageView collectsBtnImg;
    @FXML
    private Pane mainPageContainer;
    @FXML
    private Button eventsBtn;
    @FXML
    private Circle profilImgContainer;
    @FXML
    private ImageView logoutBtnImg;
    @FXML
    private ImageView coursesBtnImg;
    @FXML
    private Button btnModifierRec;
    @FXML
    private Button productsBtn;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        System.out.println(currentUser.getIdUser());
        loadReclammationList();



        // Customize the ListView cell rendering
        listRec.setCellFactory(param -> new ListCell<Reclammation>() {
            @Override
            protected void updateItem(Reclammation reclammation, boolean empty) {
                super.updateItem(reclammation, empty);

                if (empty || reclammation == null) {
                    setGraphic(null);
                } else {
                    // Create a VBox for each reclamation to simulate a card
                    VBox vBox = new VBox(10);
                    vBox.setPadding(new Insets(10));
                    vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    vBox.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 2;");

                    // Add Labels to display reclamation details
                    Label sujetLabel = new Label("Sujet: " + reclammation.getSujet());
                    sujetLabel.setFont(new Font("Arial", 16));
                    sujetLabel.setTextFill(Color.DARKBLUE);

                    Label statutLabel = new Label("Statut: " + reclammation.getStatut());
                    statutLabel.setFont(new Font("Arial", 14));
                    statutLabel.setTextFill(Color.DARKGREEN);

                    Label dateCreationLabel = new Label("Date: " + reclammation.getDate_creation().toString()); // Corrected getter
                    dateCreationLabel.setFont(new Font("Arial", 12));
                    dateCreationLabel.setTextFill(Color.BLACK);

                    // Add the Labels to the VBox (the "card")
                    vBox.getChildren().addAll(sujetLabel, statutLabel, dateCreationLabel);

                    // Set this VBox as the graphic of the ListCell (which will be the visual display of each item)
                    setGraphic(vBox);
                }
            }
        });
    }

    void loadReclammationList() {
        User currentUser = SessionManager.getCurrentUser();
        try {
            List<Reclammation> reclammations = serviceReclammation.afficher(currentUser.getIdUser());
            if (reclammations == null || reclammations.isEmpty()) {
                System.out.println("Aucune réclamation trouvée.");
                listRec.setItems(FXCollections.observableArrayList()); // Vide la liste proprement
                return;
            }

            ObservableList<Reclammation> reclamationList = FXCollections.observableArrayList(reclammations);
            listRec.setItems(reclamationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @FXML
   public void versAjouterR(ActionEvent event) throws IOException {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ajoutR.fxml"));
           Parent root = loader.load();
           // Obtenir le contrôleur de la fenêtre AjoutRController
           AjoutRController ajoutRController = loader.getController();

           // Associer le contrôleur principal
           ajoutRController.setAffichReclamation(this);
           // Créer une nouvelle fenêtre pour Ajouter Team
           Stage stage = new Stage();
           stage.setTitle("Ajouter Reclamation");
           stage.setScene(new Scene(root));
           stage.show();
       } catch (IOException e) {
           e.printStackTrace();
           System.err.println("Erreur lors du chargement de ajoutR.fxml");
       }
    }


    @FXML
    public void versEditR(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/editR.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }



    public void delR(ActionEvent actionEvent) {
        // Récupérer l'élément sélectionné
        Reclammation selectedReclamation = listRec.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            try {
                // Supprimer la réclamation via le service
                boolean deleted = serviceReclammation.supprimer(selectedReclamation.getId_reclammation());

                if (deleted) {
                    System.out.println("Réclamation supprimée avec succès !");

                    // Supprimer localement de l'ObservableList
                    listRec.getItems().remove(selectedReclamation);

                    // Effacer la sélection pour éviter les problèmes
                    listRec.getSelectionModel().clearSelection();

                    // Rafraîchir l'affichage de la ListView
                    listRec.refresh();

                } else {
                    System.out.println("Échec de la suppression.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner une réclamation à supprimer.");
        }
    }



}
