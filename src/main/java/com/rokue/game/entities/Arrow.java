package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Arrow extends Entity {
    private int directionX;
    private int directionY;

    public Arrow(int startX, int startY, int targetX, int targetY) {
        super();
        this.xPosition = startX;
        this.yPosition = startY;

        this.directionX = Integer.compare(targetX, startX);
        this.directionY = Integer.compare(targetY, startY);
    }

    @Override
    public void update() {
        xPosition += directionX;
        yPosition += directionY;

    }

    @Override
    public Image getSprite() {
        return new ImageIcon(getClass().getResource("/sprites/arrow.png")).getImage();
    }
}
