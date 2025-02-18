package org.example.entities;

import java.awt.*;

public class Block {
    int x;
    int y;
    int width;
    int height;
    int sx;
    int sy;
    Image image;
    int type;
    char direction='U';
    int velX=0;
    int velY=0;


    Block(int x, int y, int width, int height, Image image,int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.type = type;
        sx = x;
        sy = y;

    }
    void updateDirection(char direction) {
        this.direction = direction;
        if (this.direction == 'U') {
            this.velX = 0;
            this.velY = -10;
        }
        else if (this.direction == 'D') {
            this.velX = 0;
            this.velY = 10;
        }
        else if (this.direction == 'L') {
            this.velX = -10;
            this.velY = 0;
        }
        else if (this.direction == 'R')
        {
            this.velX = 10;
            this.velY = 0;
        }


    }
    public void reset()
    {
        this.x=sx;
        this.y=sy;

    }
}