package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.example.entities.User;
import org.example.utils.MyDataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.example.entities.SessionManager;


public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField mdpField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {

        String email = emailField.getText();
        String mdp = mdpField.getText();


        if (email.isEmpty() || mdp.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }


        User user = validateLogin(email, mdp);
        if (user != null) {
            System.out.println("User found in DB: " + user.getEmail());
            System.out.println("User ID from DB: " + user.getIdUser());


            SessionManager.setCurrentUser(user);


            System.out.println("User role after login: " + SessionManager.getCurrentUser().getRole());

            if ("Admin".equals(user.getRole())) {
                openAdminPage(user, event);
            } else {
                openProfilPage(event);
            }
        } else {
            System.out.println("Login failed. Please check your email and password.");
        }
    }


    private User validateLogin(String email, String mdp) {
        String query = "SELECT * FROM user WHERE email = ? AND mdp = ?";

        try (Connection connection = MyDataBase.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, mdp);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    User user = new User(
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("email"),
                            resultSet.getString("role"),
                            resultSet.getString("mdp")
                    );
                    user.setIdUser(resultSet.getInt("id_user"));
                    return user;

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openProfilPage(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/profil.fxml")));


        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);


        Stage stage = (Stage) scene.getWindow();
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();


        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());


        stage.show();
    }


    private void openAdminPage(User user, ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
            Parent root = loader.load();


            AdminController adminController = loader.getController();
            adminController.setUserDetails(user.getNom(), user.getPrenom(), user.getEmail(), user.getRole());


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());


            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load admin.fxml");
        }
    }

}
