package controllers;

import entities.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ServiceParticipant;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterEventController {
    @FXML
    private Button btAfficher;
    @FXML
    private Button btAjouter;
    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfLieu;
    @FXML
    private TextField tfDesc;

    @FXML
    public void AfficherEvent(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherEvent.fxml"));
            tfNom.getScene().setRoot(root);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
