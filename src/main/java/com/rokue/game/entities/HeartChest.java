package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class HeartChest extends GameObject{
    private static final Image sprite = new ImageIcon(HeartChest.class.getResource("/sprites/objects/heartChest.png")).getImage();

    public HeartChest() {
        super();
        canCaryRune = false;
    }
    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }

}
