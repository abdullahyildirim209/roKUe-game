package com.rokue.game.entities;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

import com.rokue.game.map.Hall;

public class Fighter extends Monster {
    private Hall hall;
    private Image sprite;
    private long lastMoveTime = 0;
    private final long moveDelay = 500;
    private Random random = new Random();
    private boolean followingLuringGem = false;

    private int targetX = -1;
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

    public void reset(){
        targetX = -1;
        targetY = -1;
    }

    @Override
    public void update() {
        if (hall == null || hall.getHero() == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime < moveDelay) {
            return;
        }

        if (!followingLuringGem) {
            LuringGem nearestGem = findNearestLuringGem();
            if (nearestGem != null) {
                followLuringGem(nearestGem.getXPosition(), nearestGem.getYPosition());
            }
        }

        if (followingLuringGem && targetX != -1 && targetY != -1) {
            moveTowards(targetX, targetY);


        } else {
            int heroX = hall.getHero().getXPosition();
            int heroY = hall.getHero().getYPosition();

            int dx = Math.abs(heroX - xPosition);
            int dy = Math.abs(heroY - yPosition);

            if (dx + dy == 1) {
                hall.getHero().decreaseHealth();
                System.out.println("Fighter hit the Hero! Hero's health: " + hall.getHero().getHealth());
            }
            else if (dx + dy <= 3) {
                moveTowards(heroX, heroY);
            }

            else {
                randomMove();
            }
        }

        lastMoveTime = currentTime;
    }

    private LuringGem findNearestLuringGem() {
        LuringGem nearestGem = null;
        int minDistance = Integer.MAX_VALUE;

        for (Enchantment enchantment : hall.getEnchantments()) {
            if (enchantment instanceof LuringGem) {
                LuringGem gem = (LuringGem) enchantment;
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

        if (hall.isPositionEmpty(newX, newY)) {
            xPosition = newX;
            yPosition = newY;
        }
        else randomMove();
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

        if (newX >= 1 && newX <= 16 && newY >= 1 && newY <= 16 && hall.isPositionEmpty(newX, newY)) {
            xPosition = newX;
            yPosition = newY;
        }
    }

    public void followLuringGem(int gemX, int gemY) {
        this.followingLuringGem = true;
        this.targetX = gemX;
        this.targetY = gemY;
    }

    @Override
    public Image getSprite() {
        return sprite;
    }
}
