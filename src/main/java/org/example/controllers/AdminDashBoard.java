package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import org.example.entities.User;
import org.example.services.ServiceUser;

import java.sql.SQLException;
import java.util.List;

public class AdminDashBoard {

    @FXML
    private ListView<User> listUser;
    private final ServiceUser serviceUser = new ServiceUser();

    @FXML
    public void initialize() {
        loadUserList();


        listUser.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setGraphic(null);
                } else {

                    VBox vBox = new VBox(10);
                    vBox.setPadding(new Insets(10));
                    vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    vBox.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 2;");


                    Label nameLabel = new Label(user.getNom() + " " + user.getPrenom());
                    nameLabel.setFont(new Font("Arial", 16));
                    nameLabel.setTextFill(Color.DARKBLUE);

                    Label emailLabel = new Label(user.getEmail());
                    emailLabel.setFont(new Font("Arial", 14));
                    emailLabel.setTextFill(Color.BLACK);

                    Label roleLabel = new Label("Role: " + user.getRole());
                    roleLabel.setFont(new Font("Arial", 14));
                    roleLabel.setTextFill(Color.DARKGREEN);


                    vBox.getChildren().addAll(nameLabel, emailLabel, roleLabel);


                    setGraphic(vBox);
                }
            }
        });
    }

    private void loadUserList() {
        try {

            List<User> users = serviceUser.afficher_t();
            if (users == null || users.isEmpty()) {
                System.out.println("No users found");
                return;
            }


            ObservableList<User> userList = FXCollections.observableArrayList(users);


            listUser.setItems(userList);

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to load users");
            alert.setContentText("There was an error connecting to the database. Please try again later.");
            alert.showAndWait();
        }
    }

}
