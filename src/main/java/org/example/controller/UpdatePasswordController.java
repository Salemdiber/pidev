package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.model.User;
//a faire session 
public class UpdatePasswordController {

    @FXML
    private PasswordField currentMdpField;

    @FXML
    private PasswordField newMdpField;

    @FXML
    private PasswordField confirmMdpField;

    @FXML
    private Label errorLabel;

    private User currentUser;

    public void setUserDetails(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        // Retrieve data from input fields
        String currentMdp = currentMdpField.getText();
        String newMdp = newMdpField.getText();
        String confirmMdp = confirmMdpField.getText();

        // Perform validation
        if (currentMdp.isEmpty() || newMdp.isEmpty() || confirmMdp.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!newMdp.equals(confirmMdp)) {
            errorLabel.setText("New passwords do not match.");
            return;
        }

        if (!currentUser.getMdp().equals(currentMdp)) {
            errorLabel.setText("Current password is incorrect.");
            return;
        }

        // Update password in the database
        currentUser.setMdp(newMdp);
        UserDAO userDAO = new UserDAO();
        if (userDAO.updateUser(currentUser)) {
            errorLabel.setText("Password update successful!");
            // Optionally, navigate to another page or show a success message
        } else {
            errorLabel.setText("Password update failed. Please try again.");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        // Handle cancel action, e.g., close the window or navigate back
    }
}
