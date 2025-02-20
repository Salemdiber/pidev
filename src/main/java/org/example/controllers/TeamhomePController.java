package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.entities.Equipe;
import org.example.entities.Partie;
import org.example.entities.SessionManager;
import org.example.services.ServiceEquipe;
import org.example.services.ServiceMatch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.example.entities.TypeMatch.AMICAL;

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
        String role = SessionManager.getInstance().getUserRole();
        if (!"Admin".equals(role)) {
            addteam.setDisable(true);
            addmatch.setDisable(true);
        }

        allteamsBtnA(null);

    }

    private void chargerTeams() {
        try {
            List<Equipe> equipes = serviceEquipe.afficher_t();
            ObservableList<Equipe> data = FXCollections.observableArrayList(equipes);
            listViewTeams.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les équipes : " + e.getMessage());
        }
    }

    private void chargerMatchs() {
        try {
            List<Partie> matchs = serviceMatch.afficher_t();
            ObservableList<Partie> data = FXCollections.observableArrayList(matchs);
            listViewMatchs.setItems(data);
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les matchs : " + e.getMessage());
        }
    }

    public void rafraichirAffichage() {
        try {
            List<Equipe> equipes = serviceEquipe.afficher_t();
            listViewTeams.setItems(FXCollections.observableArrayList(equipes));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    public void rafraichirAffichageM() {
        try {
            ServiceMatch serviceMatch = new ServiceMatch();
            List<Partie> matchs = serviceMatch.afficher_t();
            listViewMatchs.getItems().clear();
            listViewMatchs.getItems().addAll(matchs);
            listViewMatchs.refresh();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTeam.fxml"));
            Parent root = loader.load();

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
    private void handleAjouterMatch(ActionEvent event) {
        try {
            Parent AddMatch = FXMLLoader.load(getClass().getResource("/view/AddMatch.fxml"));
            Scene scene = new Scene(AddMatch);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
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
        listViewMatchs.setVisible(true);
        matchScrollPane.setVisible(true);
        listViewTeams.setVisible(false);
        teamsScrollPane.setVisible(false);
        chargerMatchs();
    }

    @FXML
    public void allteamsBtnA(ActionEvent actionEvent) {
        listViewTeams.setVisible(true);
        teamsScrollPane.setVisible(true);
        listViewMatchs.setVisible(false);
        matchScrollPane.setVisible(false);
        chargerTeams();
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
    private void handleOuvrirTerrain(ActionEvent event) throws IOException {
        Parent trajetPage = FXMLLoader.load(getClass().getResource("/view/HomeAffiche.fxml"));
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