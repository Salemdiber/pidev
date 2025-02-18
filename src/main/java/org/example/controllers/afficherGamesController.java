package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.entities.MiniGame;
import org.example.services.ServiceMiniGame;

import java.sql.SQLException;
import java.util.List;

public class afficherGamesController {

    @FXML
    private ListView<MiniGame> gameList;
    @FXML
    private Button back;
    private final ServiceMiniGame serviceMiniGame = new ServiceMiniGame();

    @FXML
    public void initialize() {
        loadGameHistory();
        gameList.setCellFactory(param -> new ListCell<MiniGame>() {
            @Override
            protected void updateItem(MiniGame game, boolean empty) {
                super.updateItem(game, empty);

                if (empty || game == null) {
                    setGraphic(null);
                } else {
                    // Create Labels for each field
                    Label typeLabel = new Label("Type: " + game.getType());
                    typeLabel.setFont(new Font("Arial", 14));
                    typeLabel.setTextFill(Color.BLACK);

                    Label scoreLabel = new Label("Score: " + game.getScore());
                    scoreLabel.setFont(new Font("Arial", 14));
                    scoreLabel.setTextFill(Color.BLACK);

                    Label resultLabel = new Label("Result: " + game.getResult());
                    resultLabel.setFont(new Font("Arial", 14));
                    resultLabel.setTextFill(Color.BLACK);

                    VBox details = new VBox(3, typeLabel, scoreLabel, resultLabel);
                    setGraphic(details);
                }
            }
        });
    }

    private void loadGameHistory() {
        try {
            List<MiniGame> games = serviceMiniGame.afficher(1);
            if (games.isEmpty()) {
                System.out.println("no");
                return;
            }
            ObservableList<MiniGame> gameDataList = FXCollections.observableArrayList(games);
            gameList.setItems(gameDataList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void backPage(ActionEvent actionEvent) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/gameHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1450, 720);
            Stage primaryStage = (Stage) back.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

