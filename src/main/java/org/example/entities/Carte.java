package org.example.entities;

import javax.swing.*;

public class Carte extends JPanel {
   int id,score,id_user;
   String rarite,image;

   public Carte(int score, int id_user, String rarite, String image) {
       this.score = score;
       this.id_user = id_user;
       this.rarite = rarite;
       this.image = image;
   }
   public Carte() {

   }
    public void setId(int id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRarite(String rarite) {
        this.rarite = rarite;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public int getId_user() {
        return id_user;
    }

    public String getImage() {
        return image;
    }

    public String getRarite() {
        return rarite;
    }
}
