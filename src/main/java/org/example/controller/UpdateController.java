package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.model.User;

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
        // Initialize the controller, e.g., set up event listeners
    }

    public void setUserDetails(User user) {
        this.currentUser = user;
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleField.setText(user.getRole());
        // Avoid setting the password field for security reasons
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        // Retrieve data from input fields
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String role = roleField.getText();
        String mdp = mdpField.getText();

        // Perform validation
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role.isEmpty() || mdp.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        // Update user details in the database
        User updatedUser = new User(nom, prenom, email, role, mdp);
        updatedUser.setIdUser(currentUser.getIdUser()); // Ensure the ID is retained for update

        UserDAO userDAO = new UserDAO();
        if (userDAO.updateUser(updatedUser)) {
            errorLabel.setText("Update successful!");
            // Optionally, navigate to another page or show a success message
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
