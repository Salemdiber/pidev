package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.model.User;

import org.example.model.SessionManager;

public class UpdateController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    @FXML
    private PasswordField mdpField;

    @FXML
    private Button submitButton;

    @FXML
    private Label errorLabel;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getCurrentUser();
        System.out.println("Currqqqqent user ID: " + currentUser.getIdUser());

        if (currentUser != null) {

            setUserDetails(currentUser);
            // displayUserImage(currentUser);

        }
    }

    public void setUserDetails(User user) {
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleField.setText(user.getRole());
        // Avoid setting the password field for security reasons
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        // Récupérer les données des champs
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String role = roleField.getText();

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }
        currentUser = SessionManager.getCurrentUser();
        System.out.println("Current user ID: " + currentUser.getIdUser());

        // Créer un utilisateur mis à jour sans toucher au mot de passe
        User updatedUser = new User(nom, prenom, email, role, currentUser.getMdp()); // Utiliser le mot de passe actuel
        updatedUser.setIdUser(currentUser.getIdUser()); // S'assurer que l'ID est conservé pour la mise à jour
        System.out.println("Current user ID: " + currentUser.getIdUser());

        UserDAO userDAO = new UserDAO();
        if (userDAO.updateUser(updatedUser)) {
            errorLabel.setText("Update successful!");
            // Optionnellement, naviguer vers une autre page ou afficher un message de succès
        } else {
            errorLabel.setText("Update failed. Please try again.");
        }
    }


    @FXML
    private void handleEditProfile(ActionEvent event) {
        // Handle edit profile action, e.g., reload the current scene or navigate to another
    }

    @FXML
    private void handleAdminDashboard(ActionEvent event) {
        // Handle admin dashboard action, e.g., navigate to the admin dashboard
    }
}
