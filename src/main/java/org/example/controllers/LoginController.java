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
                    user.setIdUser(resultSet.getInt("id_user")); // 🔥 Ajout de l'ID ici
                    return user;

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openProfilPage(ActionEvent event) throws IOException {
        // Charger la vue du profil
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/profil.fxml")));

        // Obtenir la scène actuelle
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);

        // Récupérer la taille de l'écran
        Stage stage = (Stage) scene.getWindow();
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();

        // Ajuster la taille de la fenêtre pour qu'elle prenne toute la taille de l'écran
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        // Afficher la fenêtre
        stage.show();
    }


    private void openAdminPage(User user, ActionEvent event) {
        try {
            // Charger le fichier admin.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et définir les détails de l'utilisateur
            AdminController adminController = loader.getController();
            adminController.setUserDetails(user.getNom(), user.getPrenom(), user.getEmail(), user.getRole());

            // Obtenir le stage actuel
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Ajuster la taille de la fenêtre pour qu'elle prenne toute la taille de l'écran
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            // Mettre à jour la scène avec la nouvelle vue
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load admin.fxml");
        }
    }

}
