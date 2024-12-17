package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Crate extends GameObject {
    private static final Image sprite = new ImageIcon(Crate.class.getResource("/sprites/objects/crate.png")).getImage();

    public Crate() {
        super();
        canCaryRune = true;
    }

    @Override
    public void update() { return; }

    @Override
    public Image getSprite() {
        return sprite;
    }

}
