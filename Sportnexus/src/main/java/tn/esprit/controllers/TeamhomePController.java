package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;
import tn.esprit.services.ServiceEquipe;
import tn.esprit.services.ServiceMatch;
import tn.esprit.utils.Mydatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static tn.esprit.entities.TypeMatch.AMICAL;

public class TeamhomePController
{

    @javafx.fxml.FXML
    private Button allmatchsBtn;
    @javafx.fxml.FXML
    private Button gamesBtn;
    @javafx.fxml.FXML
    private Button logoutBtn;
    @javafx.fxml.FXML
    private Button teamrankingBtn;
    @javafx.fxml.FXML
    private Button forumBtn;
    @javafx.fxml.FXML
    private Button stadiumBtn;
    @javafx.fxml.FXML
    private ImageView stadiumBtnImg;
    @javafx.fxml.FXML
    private Button TeamsBtn;
    @javafx.fxml.FXML
    private ImageView teamsBtnImg;
    @javafx.fxml.FXML
    private Button addmatch;
    @javafx.fxml.FXML
    private HBox notifCount;
    @javafx.fxml.FXML
    private ImageView gamesBtnImg;
    @javafx.fxml.FXML
    private Button homeBtn;
    @javafx.fxml.FXML
    private ScrollPane teamsScrollPane;
    @javafx.fxml.FXML
    private ImageView eventBtnImg;
    @javafx.fxml.FXML
    private ImageView forumBtnImg;
    @javafx.fxml.FXML
    private ListView<Equipe> listViewTeams;
    @javafx.fxml.FXML
    private TextField searchInput;
    @javafx.fxml.FXML
    private Button addteam;
    @javafx.fxml.FXML
    private ImageView homeBtnImg;
    @javafx.fxml.FXML
    private Label TeamsandMatchsTitle;
    @javafx.fxml.FXML
    private Pane mainPageContainer;
    @javafx.fxml.FXML
    private Button eventBtn;
    @javafx.fxml.FXML
    private Circle profilImgContainer;
    @javafx.fxml.FXML
    private ImageView logoutBtnImg;
    @javafx.fxml.FXML
    private Label welcomeMsg;
    @javafx.fxml.FXML
    private Button allteamsBtn;
    @FXML
    private ScrollPane matchScrollPane;
    @FXML
    private ListView<Partie> listViewMatchs;


    static final ServiceEquipe serviceEquipe = new ServiceEquipe();
    static final ServiceMatch serviceMatch = new ServiceMatch();



    @FXML
    public void initialize() throws SQLException {
        if (listViewTeams == null) {
            System.out.println("❌ ERREUR : listViewEquipes est NULL dans initialize()");
            return;
        }
        if (listViewMatchs == null) {
            System.out.println("❌ ERREUR : listViewMatchs est NULL dans initialize()");
            return;
        }
        listViewMatchs.setCellFactory(param -> new MatchListController());

        listViewTeams.setCellFactory(param -> new TeamListController());

        allteamsBtnA(null);

    }

    private void chargerTeams() {
        try {
            List<Equipe> equipes = serviceEquipe.afficher();
            ObservableList<Equipe> data = FXCollections.observableArrayList(equipes);
            listViewTeams.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les équipes : " + e.getMessage());
        }
    }

    private void chargerMatchs() {
        try {
            List<Partie> matchs = serviceMatch.afficher();
            ObservableList<Partie> data = FXCollections.observableArrayList(matchs);
            listViewMatchs.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les matchs : " + e.getMessage());
        }
    }

    public void rafraichirAffichage() {
        try {
            List<Equipe> equipes = serviceEquipe.afficher();
            listViewTeams.setItems(FXCollections.observableArrayList(equipes));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    public void rafraichirAffichageM() {
        try {
            ServiceMatch serviceMatch = new ServiceMatch();  // ✅ Créer une instance
            List<Partie> matchs = serviceMatch.afficher();  // ✅ Appeler la méthode correctement
            listViewMatchs.getItems().clear();  // Vider la liste
            listViewMatchs.getItems().addAll(matchs); // Ajouter les nouvelles données
            listViewMatchs.refresh(); // Rafraîchir l'affichage
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }


    static void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAjouterTeam() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddTeam.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre pour Ajouter Team
            Stage stage = new Stage();
            stage.setTitle("Ajouter Team");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AddTeam.fxml");
        }
    }

    @FXML
    private void handleAjouterMatch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/AddMatch.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre pour AjouterTerrain
            Stage stage = new Stage();
            stage.setTitle("Ajouter Match");
            stage.setScene(new Scene(root));
            // Ajouter un écouteur pour détecter la fermeture de la fenêtre et rafraîchir la ListView
            stage.setOnHidden((WindowEvent e) -> rafraichirAffichage());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AddMatch.fxml");
        }
    }

    @FXML
    private void handleRefresh() {
        rafraichirAffichage();
    }
    @FXML
    public void allmatchsBtn(ActionEvent actionEvent) {
        listViewMatchs.setVisible(true);  // Afficher la liste des matchs
        matchScrollPane.setVisible(true); // Afficher le ScrollPane des matchs
        listViewTeams.setVisible(false);  // Cacher la liste des équipes
        teamsScrollPane.setVisible(false); // Cacher le ScrollPane des équipes
        chargerMatchs();                   // Charger les matchs
    }

    @FXML
    public void allteamsBtnA(ActionEvent actionEvent) {
        listViewTeams.setVisible(true);
        teamsScrollPane.setVisible(true);
        listViewMatchs.setVisible(false); // Cacher la liste des matchs
        matchScrollPane.setVisible(false); // Cacher le ScrollPane des matchs
        chargerTeams(); // Recharger les équipes
    }


    @javafx.fxml.FXML
    public void TeamsBtnClicked(Event event) {
    }
}