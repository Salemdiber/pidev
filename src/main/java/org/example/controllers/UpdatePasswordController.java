package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import org.example.entities.User;
import org.example.services.ServiceUser;

import java.sql.SQLException;


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
    private void handleSubmitButtonAction(ActionEvent event) throws SQLException {

        String currentMdp = currentMdpField.getText();
        String newMdp = newMdpField.getText();
        String confirmMdp = confirmMdpField.getText();


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


        currentUser.setMdp(newMdp);
        ServiceUser serviceuser = new ServiceUser();
        if (serviceuser.modifieru(currentUser)) {
            errorLabel.setText("Password update successful!");
        } else {
            errorLabel.setText("Password update failed. Please try again.");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {

    }
}
