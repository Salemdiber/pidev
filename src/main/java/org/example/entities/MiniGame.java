package org.example.entities;
import java.sql.Blob;
import java.sql.Date;
import org.example.utils.MyDataBase;

import javax.swing.*;

public class MiniGame extends JPanel {
    protected int id, type,score,user;
    protected String result;
    protected String data;

    public MiniGame( int type, int score, int user, String result, String data) {
        this.type = type;
        this.score = score;
        this.user = user;
        this.result = result;
        this.data = data;
    }

    public MiniGame() {
    }
    public int getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

    public String getResult() {
        return result;
    }

    public String getData() {
        return data;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MiniGame{" +"id=" + id +", score=" + score +", user=" + user +", result='" + result + '\'' +", data=" + data;
    }
}
