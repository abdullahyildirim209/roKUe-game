package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;

public class Fighter extends Character implements Serializable { 
    private static final long serialVersionUID = 1L;
    
    private boolean followLuringGem = false;
    private final long attackDelay = 30;
    private long lastAttack = 0;
    private int targetX = -1;
    private int targetY = -1;
    private int nextTargetDirectionX = 0;
    private int nextTargetDirectionY = 0;
    private boolean moved = false;
    private final long randomMoveTime = 60;
    private long lastRandomMove = 0;
    private int randomMoveDirection = 0;
    private boolean xDirection = false;

    private static final int[][] DIRECTIONS = {
        {-1, 0}, // up
        {1, 0},  // down
        {0, -1}, // left
        {0, 1}   // right
    };

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
        if (followLuringGem && targetX != -1 && targetY != -1) {
            findDirection();
            moveTowards(xPosition + nextTargetDirectionX, yPosition + nextTargetDirectionY, false);
        }
        else {
            int heroX = hall.getHero().getXPosition();
            int heroY = hall.getHero().getYPosition();

            int dx = Math.abs(heroX - xPosition);
            int dy = Math.abs(heroY - yPosition);

            // If distance to Hero is 1, attack
            if (dx + dy <= 1 && PlayPanel.tickTime - lastAttack > attackDelay) {
                hall.getHero().decreaseHealth();
                lastAttack = PlayPanel.tickTime;
                SoundManager.playSound("fighterAttack");
                System.out.println("Fighter hit the Hero! Hero's health: " + hall.getHero().getHealth());
            }
            // If the Hero is within 3 tiles, move towards the Hero
            else if (dx + dy <= 3) {
                targetX = heroX;
                targetY = heroY;
                findDirection();
                moveTowards(xPosition + nextTargetDirectionX, yPosition + nextTargetDirectionY, false);
            }
            // Otherwise, move randomly
            else {
                randomMove();
            }
        }
    }

    public void reset() {
        followLuringGem = false;
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
        if (PlayPanel.tickTime - lastRandomMove > randomMoveTime) {
            lastRandomMove = PlayPanel.tickTime;
            randomMoveDirection = hall.getRNG().nextInt(4);
        }

        switch (randomMoveDirection) {
            case 0 -> {
                xDirection = true;
                moveTowards(xPosition + 1, yPosition, true);
            }
            case 1 -> {
                xDirection = false;
                moveTowards(xPosition - 1, yPosition, true);
            }
            case 2 -> moveTowards(xPosition, yPosition + 1, true);
            case 3 -> moveTowards(xPosition, yPosition - 1, true);
            default -> {
            }
        }

    }

    public void findDirection() {   // BFS
        int oldXpos = xPosition;
        int oldYpos = yPosition;

        Entity[][] grid = hall.getGrid();
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[] visited = new boolean[rows * cols];
        HashMap<Integer, Integer> parentMap = new HashMap<>();

        Queue<Integer> queue = new LinkedList<>();
        queue.add(oldXpos*rows + oldYpos);
        visited[oldXpos*rows + oldYpos] = true;
        int current = 0;

        while (!queue.isEmpty()) {
            current = queue.poll();
            int curX = current / rows;
            int curY = current % rows;
            if (curX == targetX && curY == targetY) {
                break;
            }

            for (int[] direction: DIRECTIONS) {
                int newX = curX + direction[0];
                int newY = curY + direction[1];

                if ((newX == targetX && newY == targetY) || (newX >= 0 && newX < rows && newY >= 0 && newY < cols && !visited[newX*rows + newY] && grid[newX][newY] == null)) {
                    queue.add(newX*rows + newY);
                    visited[newX*rows + newY] = true;
                    parentMap.put(newX*rows + newY, current);
                }
            }
        }

        List<int[]> path = new ArrayList<>();

        while (current / rows != xPosition || current % rows != yPosition) {
            int parent = parentMap.get(current);
    
            for (int[] direction: DIRECTIONS) {
                if (parent / rows + direction[0] == current / rows && parent % rows + direction[1] == current % rows) {
                    path.add(direction);
                    break;
                }
            }

            current = parent;
        }

        Collections.reverse(path);

        int[] direction = path.get(0);
        boolean collision = false;

        switch (direction[0]) {
            case 1 -> {
                collision = checkUpCollision();
            }
            case -1 -> {
                collision = checkDownCollision();
            }
            default -> {
                if (direction[1] == 1) {
                    collision = checkRightCollision();
                }
                else if (direction[1] == -1) {
                    collision = checkLeftCollision();
                }
            }
        }

        if (!collision) {
            nextTargetDirectionX = direction[0];
            nextTargetDirectionY = direction[1];
        }
    }


    public void followLuringGem() {
        LuringGem l = hall.getActiveLuringGem();
        targetX = l.getXPosition();
        targetY = l.getYPosition();
        followLuringGem = true;
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        if (targetX != -1 && targetY != -1)
            return spriteLoader.getMonsterSprites()[targetX > xPosition ? 5 : 6];
        else 
            return spriteLoader.getMonsterSprites()[xDirection ? 5 : 6];

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
