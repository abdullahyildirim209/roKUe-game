package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

import com.rokue.game.map.Hall;



public class LuringGem extends Entity {
    private Hall hall;
    private int direction;
    private Image sprite;

    public LuringGem() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/objects/LuringGem.png")).getImage();
    }

    public void place(int x, int y, Hall hall) {
        this.xPosition = x;
        this.yPosition = y;
        this.hall = hall;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public void update() {

    }

    @Override
    public Image getSprite() {
        return sprite;
    }
}