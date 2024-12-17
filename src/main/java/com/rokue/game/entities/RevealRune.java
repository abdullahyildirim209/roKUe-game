package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.rokue.game.map.Hall;

public class RevealRune extends Enchantment {
    private Hall hall;
    private Image sprite;

    public RevealRune() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/objects/RevealRune.png")).getImage();

    }

    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }

}