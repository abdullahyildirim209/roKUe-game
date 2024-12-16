package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Crate extends Entity {

    private static final Image sprite = new ImageIcon(Crate.class.getResource("/sprites/objects/crate.png")).getImage();

    @Override
    public void update() {
    }

    @Override
    public Image getSprite() {
        return sprite;
    }
}
