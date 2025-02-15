package org.example.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.entities.Terrain;

import java.io.File;

public class TerrainListCellController extends ListCell<Terrain> {

    private final HBox content; // Conteneur principal pour la cellule
    private final ImageView imageView; // Pour afficher l'image du terrain
    private final Label nomLabel; // Pour afficher le nom du terrain
    private final Label lieuLabel; // Pour afficher le lieu du terrain
    private final Label descriptionLabel; // Pour afficher la description du terrain

    public TerrainListCellController() {
        super();

        // 🔹 Initialiser les composants
        imageView = new ImageView();
        imageView.setFitHeight(100); // Taille de l'image
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        nomLabel = new Label();
        nomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        lieuLabel = new Label();

        descriptionLabel = new Label();
        descriptionLabel.setWrapText(true); // Permettre le retour à la ligne automatique
        descriptionLabel.setMaxWidth(300); // Largeur max pour éviter trop de texte

        // 🔹 Organiser les composants dans un VBox
        VBox textContainer = new VBox(nomLabel, lieuLabel, descriptionLabel);
        textContainer.setSpacing(5); // Espacement entre les éléments

        // 🔹 HBox contenant l'image et les textes
        content = new HBox(imageView, textContainer);
        content.setSpacing(10); // Espacement entre l'image et les textes
    }

    @Override
    protected void updateItem(Terrain terrain, boolean empty) {
        super.updateItem(terrain, empty);
        if (empty || terrain == null) {
            setGraphic(null);
        } else {
            //  Mettre à jour le texte des labels
            nomLabel.setText("Nom : " + terrain.getNom());
            lieuLabel.setText("Lieu : " + terrain.getLieu());
            descriptionLabel.setText("Description : " + terrain.getDes());

            // 📷 Vérifier et charger l'image
            String imagePath = terrain.getImg();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    System.err.println("❌ Image introuvable : " + imagePath);
                    imageView.setImage(null);
                }
            }

            setGraphic(content);
        }
    }
}
