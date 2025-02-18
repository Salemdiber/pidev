package org.example.entities;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    int id_res,id_user,id_terrain;
    String nomTerrain;
    Timestamp date_res;



    public Reservation(int id_res, Timestamp date_res, int id_user, int id_terrain) {
        this.id_res = id_res;
        this.date_res = date_res;
        this.id_user = id_user;
        this.id_terrain = id_terrain;

    }
    public Reservation(Timestamp date_res, int id_user, int id_terrain, String nom_terrain) {
        this.date_res = date_res;
        this.id_user = id_user;
        this.id_terrain = id_terrain;


    }

    public int getId_res() {
        return id_res;
    }

    public void setId_res(int id_res) {
        this.id_res = id_res;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_terrain() {
        return id_terrain;
    }

    public void setId_terrain(int id_terrain) {
        this.id_terrain = id_terrain;
    }

    public Timestamp getDate_res() {
        return date_res;
    }

    public void setDate_res(Timestamp date_res) {
        this.date_res = date_res;
    }

    public String getNomTerrain() {
        return nomTerrain;
    }

    public void setNomTerrain(String nomTerrain) {
        this.nomTerrain = nomTerrain;
    }

}

