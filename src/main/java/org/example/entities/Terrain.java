package org.example.entities;
import javafx.beans.property.*;
import javafx.beans.binding.BooleanExpression;

public class Terrain {
    String nom,lieu,des,img;
    int id_terrain;
public Terrain() {}
    public Terrain(int id_terrain,String nom, String lieu, String des, String img) {
        this.id_terrain = id_terrain;
        this.nom = nom;
        this.lieu = lieu;
        this.des = des;
        this.img = img;

    }
    public Terrain(String nom, String lieu, String des, String img) {

        this.nom = nom;
        this.lieu = lieu;
        this.des = des;
        this.img = img;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId_terrain() {
        return id_terrain;
    }
    @Override
    public String toString() {
        return "Terrain{" +
                "id_terrain=" + id_terrain +
                ", nom='" + nom + '\'' +
                ", lieu='" + lieu + '\'' +
                ", description='" + des + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

}

