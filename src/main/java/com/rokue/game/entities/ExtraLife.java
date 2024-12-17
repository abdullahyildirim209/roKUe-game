package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ExtraLife extends Enchantment{
    private Image sprite;

    public ExtraLife() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/objects/heartChestOpen.png")).getImage();
    }

    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }

}
