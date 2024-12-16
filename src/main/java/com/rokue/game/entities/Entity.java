package com.rokue.game.entities;

import java.awt.Image;
import java.awt.Rectangle;

import com.rokue.game.map.Hall;

public abstract class Entity {
    protected int xPosition; // Grid pozisyonu (X)
    protected int yPosition; // Grid pozisyonu (Y)

    public void place(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    public int getXPixelPosition() {
        return xPosition * 16;
    }

    public int getYPixelPosition() {
        return yPosition * 16;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    public abstract void update();

    public abstract java.awt.Image getSprite();
}
