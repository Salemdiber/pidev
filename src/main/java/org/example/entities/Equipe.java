package org.example.entities;
import java.util.ArrayList;
import java.util.List;

public class Equipe {
    private int idEquipe;
    private String nom;
    private int classement;
    private String image;
    private String description;
    private int points;
    private int idEntraineur = 1;

    private List<Partie> matchs = new ArrayList<>(); // Liste des matchs joués par l'équipe


    public Equipe(int idEquipe, String nom, int classement, String image, String description, int points) {
        this.idEquipe = idEquipe;
        this.nom = nom;
        this.classement = classement;
        this.image = image;
        this.description = description;
        this.points = points;
    }

    public Equipe(String nom, int classement, String image, String description, int points, int idEntraineur) {
        this.nom = nom;
        this.classement = classement;
        this.image = image;
        this.description = description;
        this.points = points;
        this.idEntraineur = idEntraineur;
    }

    public Equipe(String nom, int classement, String imgPath, String description, int points) {
        this.nom = nom;
        this.classement = classement;
        this.image = imgPath;
        this.description = description;
        this.points = points;
    }

    public Equipe(int idEquipe, String nomEquipe) {
        this.idEquipe = idEquipe;
        this.nom = nomEquipe;
    }

    public Equipe() {

    }

    // Getters
    public int getIdEquipe() {
        return idEquipe;
    }

    public String getNom() {
        return nom;
    }

    public int getClassement() {
        return classement;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public int getIdEntraineur() {
        return idEntraineur;
    }


    public List<Partie> getMatchs() {
        return matchs;
    }

    // Setters
    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    public void setMatchs(List<Partie> matchs) {
        this.matchs = matchs;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setClassement(int classement) {
        this.classement = classement;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setIdEntraineur(int idEntraineur) {
        this.idEntraineur = idEntraineur;
    }

    // Ajouter un match à la liste
    public void ajouterMatch(Partie match) {
        matchs.add(match);
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "idEquipe=" + idEquipe +
                ", nom='" + nom + '\'' +
                ", classement=" + classement +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", points=" + points +
                ", idEntraineur=" + idEntraineur +
                '}';
    }
}