package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

import com.rokue.game.map.Hall;

public class Archer extends Monster {
    private Hall hall;
    private final Image sprite;
    private final long attackInterval = 1000;
    private long lastAttackTime = 0;

    public Archer() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/monsters/archer.png")).getImage();
    }

    public void place(int x, int y, Hall hall) {
        this.xPosition = x;
        this.yPosition = y;
        this.hall = hall;
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (hall.getHero() != null) {
            int heroX = hall.getHero().getXPosition();
            int heroY = hall.getHero().getYPosition();
            int distance = Math.abs(heroX - xPosition) + Math.abs(heroY - yPosition);

            if (distance < 4 && currentTime - lastAttackTime > attackInterval) {
                hall.getHero().decreaseHealth();
                lastAttackTime = currentTime;
            }
        }
    }

    private void attack(int targetX, int targetY) {
        System.out.println("Archer attacks at " + targetX + ", " + targetY);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }
}
