package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ExtraTime extends Enchantment {
    private Image sprite;

    public ExtraTime() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/objects/ExtraTime.png")).getImage();
    }

    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }
    
}
