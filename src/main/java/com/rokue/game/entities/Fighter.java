package com.rokue.game.entities;

import java.awt.Image;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class Fighter extends Character{
    
    private boolean sideOfHero = false;
    private boolean followLuringGem = false;
    private final long attackDelay = 500;
    private long lastAttack = 0;
    private int targetX = -1;
    private int targetY = -1;
    private boolean moved = false;
    private final long randomMoveTime = 500;
    private long lastRandomMove = 0;
    private int randomMoveDirection = 0;

    public Fighter() {
        super();
        collisionX = 1;
        collisionY = 5;
        collisionWidth = Hall.getPixelsPerTile() - 2;
        collisionHeight = Hall.getPixelsPerTile() + 2;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
    }

    @Override
    public void update() {
        if (xPosition >= hall.getHero().xPosition) sideOfHero = false;
        else sideOfHero = true;

        if (followLuringGem && targetX == -1 && targetY == -1) {
            targetX = findNearestLuringGem()[0];
            targetY = findNearestLuringGem()[1];
        }

        if (followLuringGem && targetX != -1 && targetY != -1) {
            moveTowards(targetX, targetY, false);
        }

        else {
            int heroX = hall.getHero().getXPosition();
            int heroY = hall.getHero().getYPosition();

            int dx = Math.abs(heroX - xPosition);
            int dy = Math.abs(heroY - yPosition);

            // If distance to Hero is 1, attack
            if (dx + dy == 1 && System.currentTimeMillis() - lastAttack > attackDelay) {
                hall.getHero().decreaseHealth();
                lastAttack = System.currentTimeMillis();
                System.out.println("Fighter hit the Hero! Hero's health: " + hall.getHero().getHealth());
            }
            // If the Hero is within 3 tiles, move towards the Hero
            else if (dx + dy <= 3) {
                moveTowards(heroX, heroY, false);
            }
            // Otherwise, move randomly
            else {
                randomMove();
            }

        }


    }

    public void reset() {
        targetX = -1;
        targetY = -1;
    }

    public void moveTowards(int x, int y, boolean calledByRandomMove) {
        if (moved) {
            if (xPosition > x && !checkLeftCollision()) xPixelPosition--;
            else if (xPosition < x && !checkRightCollision()) xPixelPosition++;

            if (yPosition > y && !checkUpCollision()) yPixelPosition--;
            else if (yPosition < y && !checkDownCollision()) yPixelPosition++;

            if (!calledByRandomMove && ((xPosition > x && checkLeftCollision()) || (xPosition < x && checkRightCollision()) || xPosition == x) && ((yPosition > y && checkUpCollision()) || (yPosition < y && checkDownCollision()) || yPosition == y))
                randomMove();

            moveTo((xPixelPosition + 8) / 16, (yPixelPosition + 15) / 16);
            moved = false;
        }
        else moved = true;
    }

    public void randomMove() {
        if (System.currentTimeMillis() - lastRandomMove > randomMoveTime) {
            lastRandomMove = System.currentTimeMillis();
            randomMoveDirection = hall.getRNG().nextInt(4);
        }

        if (randomMoveDirection == 0)
            moveTowards(xPosition + 1, yPosition, true);
        else if (randomMoveDirection == 1)
            moveTowards(xPosition - 1, yPosition, true);
        else if (randomMoveDirection == 2)
            moveTowards(xPosition, yPosition + 1, true);
        else if (randomMoveDirection == 3)
            moveTowards(xPosition, yPosition - 1, true);

    }

    public int[] findNearestLuringGem() {
        int minDistance = Integer.MAX_VALUE;
        int[] pos = {-1, -1};
        for (Enchantment e : hall.getEnchantments()) {
            if (e instanceof LuringGem) {
                int distance = Math.abs(e.getXPosition() - xPosition) + Math.abs(e.getYPosition() - yPosition);

                if (distance < minDistance) {
                    minDistance =  distance;
                    pos[0] = e.getXPosition();
                    pos[1] = e.getYPosition();
                }
            }
        }
        return pos;
    }

    public void followLuringGem() {
        followLuringGem = true;
    }

    public void stopFollowingLuringGem() {
        followLuringGem = false;
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        if (followLuringGem && targetX != -1 && targetY != -1)
            return spriteLoader.getMonsterSprites()[targetX > xPosition ? 3 : 4];
        else 
            return spriteLoader.getMonsterSprites()[sideOfHero ? 3 : 4];

    }


    // might clean this up later, checks for collision in 3 tiles in bottom/top/right/left, function is chosen depending on which direction the hero is trying to move
    public boolean checkDownCollision() {
        updateCollisionArea(xPixelPosition, yPixelPosition + 1);
        return hall.checkCollision(this, xPosition, yPosition + 1) || hall.checkCollision(this, xPosition + 1, yPosition + 1) || hall.checkCollision(this, xPosition - 1, yPosition + 1);
    }

    public boolean checkUpCollision() {
        updateCollisionArea(xPixelPosition, yPixelPosition - 1);
        return hall.checkCollision(this, xPosition, yPosition - 1) || hall.checkCollision(this, xPosition + 1, yPosition - 1) || hall.checkCollision(this, xPosition - 1, yPosition - 1);
    }

    public boolean checkRightCollision() {
        updateCollisionArea(xPixelPosition + 1, yPixelPosition);
        return hall.checkCollision(this, xPosition + 1, yPosition) || hall.checkCollision(this, xPosition + 1, yPosition + 1) || hall.checkCollision(this, xPosition + 1, yPosition - 1);
    }

    public boolean checkLeftCollision() {
        updateCollisionArea(xPixelPosition - 1, yPixelPosition);
        return hall.checkCollision(this, xPosition - 1, yPosition) || hall.checkCollision(this, xPosition - 1, yPosition + 1) || hall.checkCollision(this, xPosition - 1, yPosition - 1);
    }
    
}
