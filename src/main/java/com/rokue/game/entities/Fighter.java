package com.rokue.game.entities;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

import com.rokue.game.map.Hall;

public class Fighter extends Entity {
    private Hall hall;
    private Image sprite;
    private long lastMoveTime = 0; // Son hareket zamanı
    private final long moveDelay = 500;
    private Random random = new Random(); // Rastgele hareket için
    private boolean followingLuringGem = false;

    private int targetX = -1; // Hedef koordinatlar (Luring Gem)
    private int targetY = -1;

    public Fighter() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/monsters/fighter.png")).getImage();
    }


    public void place(int x, int y, Hall hall) {
        this.xPosition = x;
        this.yPosition = y;
        this.hall = hall;
    }

    @Override
    public void update() {
        if (hall == null || hall.getHero() == null) {
            return; // Ensure hall and hero are not null
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime < moveDelay) {
            return; // Enforce move delay
        }

        // Luring Gem Behavior
        if (!followingLuringGem) {
            LuringGem nearestGem = findNearestLuringGem();
            if (nearestGem != null) {
                followLuringGem(nearestGem.getXPosition(), nearestGem.getYPosition());
            }
        }

        // If following a Luring Gem, move towards it
        if (followingLuringGem && targetX != -1 && targetY != -1) {
            moveTowards(targetX, targetY);

            // Check if the Fighter reached the Luring Gem
            if (xPosition == targetX && yPosition == targetY) {
                Entity entity = hall.getEntityAt(targetX, targetY);
                if (entity instanceof LuringGem) {
                    hall.removeEntity(entity); // Remove the Luring Gem from the map
                }
                followingLuringGem = false; // Stop following the Luring Gem
                targetX = -1;
                targetY = -1;
            }
        } else {
            // Behavior to approach the Hero
            int heroX = hall.getHero().getXPosition();
            int heroY = hall.getHero().getYPosition();

            int dx = Math.abs(heroX - xPosition);
            int dy = Math.abs(heroY - yPosition);

            // If distance to Hero is 1, attack
            if (dx + dy == 1) {
                hall.getHero().decreaseHealth();
                System.out.println("Fighter hit the Hero! Hero's health: " + hall.getHero().getHealth());
            }
            // If the Hero is within 3 tiles, move towards the Hero
            else if (dx + dy <= 3) {
                moveTowards(heroX, heroY);
            }
            // Otherwise, move randomly
            else {
                randomMove();
            }
        }

        lastMoveTime = currentTime; // Update last move time
    }

    private LuringGem findNearestLuringGem() {
        LuringGem nearestGem = null;
        int minDistance = Integer.MAX_VALUE;

        for (Entity entity : hall.getEntities()) {
            if (entity instanceof LuringGem) {
                LuringGem gem = (LuringGem) entity;
                int distance = Math.abs(gem.getXPosition() - xPosition) + Math.abs(gem.getYPosition() - yPosition);

                if (distance < minDistance) {
                    minDistance = distance;
                    nearestGem = gem;
                }
            }
        }

        return nearestGem;
    }

    private void moveTowards(int targetX, int targetY) {
        int newX = xPosition;
        int newY = yPosition;

        if (xPosition < targetX) {
            newX++;
        } else if (xPosition > targetX) {
            newX--;
        }

        if (yPosition < targetY) {
            newY++;
        } else if (yPosition > targetY) {
            newY--;
        }

        // Eğer yeni pozisyon boşsa hareket et
        if (hall.isPositionEmpty(newX, newY)) {
            xPosition = newX;
            yPosition = newY;
        }
    }

    private void randomMove() {
        int direction = random.nextInt(4);
        int newX = xPosition;
        int newY = yPosition;

        switch (direction) {
            case 0 -> newX--; // Sol
            case 1 -> newX++; // Sağ
            case 2 -> newY--; // Yukarı
            case 3 -> newY++; // Aşağı
        }

        // Eğer pozisyon boşsa hareket et
        if (newX >= 1 && newX <= 16 && newY >= 1 && newY <= 16 && hall.isPositionEmpty(newX, newY)) {
            xPosition = newX;
            yPosition = newY;
        }
    }

    public void followLuringGem(int gemX, int gemY) {
        // Luring Gem'i takip etmeye başla
        this.followingLuringGem = true;
        this.targetX = gemX;
        this.targetY = gemY;
    }

    @Override
    public Image getSprite() {
        return sprite;
    }
}
