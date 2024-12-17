package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Rune extends Entity {
    private static final Image sprite = new ImageIcon(Rune.class.getResource("/sprites/objects/Rune.png")).getImage();
    private boolean isPosSet;

    public Rune() {
        super();
        isPosSet = false;
    }

    @Override
    public void setPosition(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
        isPosSet  = true;
    }

    public boolean isPositionSet(){
        return isPosSet;
    }

    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }
    
}
