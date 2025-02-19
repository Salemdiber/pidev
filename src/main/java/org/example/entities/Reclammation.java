package org.example.entities;

import java.util.Date;

public class Reclammation {
   private  int id_reclammation, id_user;
   private String sujet,description,statut;
   private Date date_creation;

    public Reclammation(int id_user, String sujet, String description, Date date_creation, String statut) {
        this.id_user = id_user;
        this.sujet = sujet;
        this.description = description;
        this.date_creation = date_creation;
        this.statut = statut;
    }

    public Reclammation(int idUser, String sujet, String description) {
        this.id_user = idUser;
        this.sujet = sujet;
        this.description = description;

    }


    public String getStatut() {
        return statut;
    }

    public void setId_reclammation(int id_reclammation) {
        this.id_reclammation = id_reclammation;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public int getId_reclammation() {
        return id_reclammation;
    }

    public int getId_user() {
        return id_user;
    }

    public String getSujet() {
        return sujet;
    }

    public String getDescription() {
        return description;
    }

}
