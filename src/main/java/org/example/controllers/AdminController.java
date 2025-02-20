package org.example.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.example.utils.MyDataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;


public class AdminController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField roleField;

    public void setUserDetails(String nom, String prenom, String email, String role) {
        nomField.setText(nom);
        prenomField.setText(prenom);
        emailField.setText(email);
        roleField.setText(role);
    }

    public void versDash(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/adminDashboard.fxml")));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public void versGestions(ActionEvent event) {
        try {

            Connection connection = MyDataBase.getInstance().getConnection();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePage.fxml")));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
