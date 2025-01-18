package com.rokue.game.entities;

import com.rokue.game.map.Hall;

public class Arrow {
    private int x, y;
    private int targetX, targetY;
    private boolean active = true;
    private final Hall hall;
    private int direction; // Direction of the arrow (0-7)

    public Arrow(int startX, int startY, int targetX, int targetY, Hall hall) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.hall = hall;

        calculateDirection(); // Determine the direction of the arrow
    }

    private void calculateDirection() {
        int dx = targetX - x;
        int dy = targetY - y;

        if (dx > 0 && dy == 0) direction = 1; // Right
        else if (dx > 0 && dy > 0) direction = 2; // Bottom-right
        else if (dx > 0 && dy < 0) direction = 3; // Upper-right
        else if (dx < 0 && dy == 0) direction = 4; // Left
        else if (dx < 0 && dy > 0) direction = 5; // Bottom-left
        else if (dx < 0 && dy < 0) direction = 6; // Upper-left
        else if (dx == 0 && dy > 0) direction = 0; // Down
        else if (dx == 0 && dy < 0) direction = 7; // Up
    }

    public void update() {
        if (!active) return;

        int deltaX = targetX - x;
        int deltaY = targetY - y;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            x += (deltaX > 0) ? 1 : -1;
        } else {
            y += (deltaY > 0) ? 1 : -1;
        }

        if (x == targetX && y == targetY) {
            if (hall.getHero().getXPosition() == targetX && hall.getHero().getYPosition() == targetY) {
                hall.getHero().decreaseHealth();
            }
            active = false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isActive() {
        return active;
    }
}
