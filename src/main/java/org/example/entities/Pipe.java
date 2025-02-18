package org.example.entities;

import java.awt.*;

public class Pipe{
    int x=360;
    int y=0;
    int width=64;
    int height=512;
    Image image;
    char type;
    boolean passed=false;
    public Pipe(Image image,char type) {
        this.image=image;
        this.type=type;
    }

}
