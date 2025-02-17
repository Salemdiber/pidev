package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import tn.esprit.entities.Equipe;
import tn.esprit.entities.Partie;
import tn.esprit.entities.TypeMatch;
import tn.esprit.services.ServiceMatch;
import tn.esprit.utils.Mydatabase;

import java.sql.*;
import java.util.List;

public class AddMatchController {
    @javafx.fxml.FXML
    private Pane AddMatchpane;
    @javafx.fxml.FXML
    private Button quitmatchbtn;
    @javafx.fxml.FXML
    private DatePicker datedatepicker;
    @javafx.fxml.FXML
    private Button addmatchbtn;
    @javafx.fxml.FXML
    private ListView<Equipe> equipeListView;
    private ObservableList<Equipe> equipesList = FXCollections.observableArrayList();
    @javafx.fxml.FXML
    private TextField resultxtfield;
    @javafx.fxml.FXML
    private ComboBox<String> placecombobox; // Spécifiez le type pour le ComboBox
    @javafx.fxml.FXML
    private ComboBox<TypeMatch> typecombobox;

    private final ServiceMatch serviceMatch = new ServiceMatch();

    @javafx.fxml.FXML
    public void initialize() {
        typecombobox.setItems(FXCollections.observableArrayList(TypeMatch.values()));
        addmatchbtn.setOnAction(event -> ajouterMatch());
        //chargerEquipes();
    }

//    private void chargerEquipes() {
//        equipesList.clear();
//        String req = "SELECT * FROM equipe"; // Remplace par ta requête correcte
//
//        try (Connection cnx = Mydatabase.getInstance().getConnection();
//             Statement st = cnx.createStatement();
//             ResultSet rs = st.executeQuery(req)) {
//            while (rs.next()) {
//                Equipe equipe = new Equipe(
//                        rs.getInt("id_equipe"),
//                        rs.getString("nom_equipe")
//                );
//                equipesList.add(equipe);
//            }
//            equipeListView.setItems(equipesList);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private void ajouterMatch() {
        // Récupération des valeurs des champs
        TypeMatch type = typecombobox.getValue();  // Utiliser getValue() pour ComboBox
        String resultat = resultxtfield.getText();  // Récupérer le texte du TextField
        Date date_match = java.sql.Date.valueOf(datedatepicker.getValue());  // Utilisation de getValue() pour DatePicker
        String place = placecombobox.getValue();  // Utiliser getValue() pour ComboBox
        /*List<Equipe> listequipe = equipeListView.getSelectionModel().getSelectedItems();*/  // Utiliser getSelectedItems() pour ListView

        // Vérification si un des champs est vide
        if (type == null || resultat.isEmpty() || date_match == null || place == null /*|| listequipe.isEmpty()*/) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        try {
            // Créer un nouvel objet Match
            Partie match = new Partie(type, resultat, date_match, place);
            // 📤 Enregistrement en base de données
            serviceMatch.ajouter(match);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");
            clearFields();

        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le match : " + e.getMessage());
        }
    }

    private void clearFields() {
        typecombobox.setValue(null); // Réinitialiser le ComboBox
        resultxtfield.clear(); // Effacer le texte du TextField
        datedatepicker.setValue(null); // Réinitialiser le DatePicker
        placecombobox.setValue(null); // Réinitialiser un autre ComboBox
        equipeListView.getSelectionModel().clearSelection(); // Vider la sélection de la ListView
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @javafx.fxml.FXML
    public void quitter(Event event) {
        // Logique pour quitter ou fermer la fenêtre
        // Par exemple, vous pouvez fermer la fenêtre actuelle

    }
}