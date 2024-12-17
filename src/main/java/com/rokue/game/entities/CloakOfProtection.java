package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class CloakOfProtection extends Enchantment{
    private Image sprite;

    public CloakOfProtection() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/objects/cloakOfProtection.png")).getImage();
    }

    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }
    
}
