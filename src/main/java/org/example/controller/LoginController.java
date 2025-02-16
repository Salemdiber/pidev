package org.example.controller;

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
import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.utils.DataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.example.model.SessionManager;


public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField mdpField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        // Retrieve data from input fields
        String email = emailField.getText();
        String mdp = mdpField.getText();

        // Perform validation
        if (email.isEmpty() || mdp.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Validate login credentials
        User user = validateLogin(email, mdp);
        if (user != null) {
            System.out.println("User found in DB: " + user.getEmail());
            System.out.println("User ID from DB: " + user.getIdUser());

            SessionManager.setCurrentUser(user);
            System.out.println("User ID in Session: " + SessionManager.getCurrentUser().getIdUser());
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

        try (Connection connection = DataBase.getConnection();
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
                    user.setIdUser(resultSet.getInt("id_user")); // 🔥 Ajout de l'ID ici
                    return user;

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openProfilPage( ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/profil.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);





       /*

        try {
            // Load the profil.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profil.fxml"));
            Parent root = loader.load();

            // Get the controller and set user details
            ProfilController profilController = loader.getController();
            //profilController.setUserDetails(user);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load profil.fxml");
        }*/
    }

    private void openAdminPage(User user, ActionEvent event) {
        try {
            // Load the admin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
            Parent root = loader.load();

            // Get the controller and set user details
            AdminController adminController = loader.getController();
            adminController.setUserDetails(user.getNom(), user.getPrenom(), user.getEmail(), user.getRole());

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load admin.fxml");
        }
    }
}
