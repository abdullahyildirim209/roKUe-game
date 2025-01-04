package com.rokue.game.entities;

import java.awt.Image;
import java.awt.Rectangle;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public abstract class Entity {
    int xPosition;
    int yPosition;
    Hall hall;
    
    int collisionX;
    int collisionY;
    int collisionWidth;
    int collisionHeight;
    Rectangle collisionArea = new Rectangle();

    boolean shadow = true;

    public Entity() {
        collisionX = 0;
        collisionY = 0;
        collisionWidth = Hall.getPixelsPerTile();
        collisionHeight = Hall.getPixelsPerTile();
    }

    public boolean getShadow() {
        return shadow;
    }

    public Rectangle getCollisionArea() {
        return collisionArea;
    }

    // used for placing props in builder mode
    public void place(int xPosition, int yPosition, Hall hall) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
        updateCollisionArea();

        this.hall = hall;
        hall.getGrid()[xPosition][yPosition] = this;
    }

    // used for placing entities, the hero and enchantments
    public void randomlyPlace(Hall hall) {
        int[] position = hall.getRandomEmptyTilePosition();
        this.place(position[0], position[1], hall);
    }

    // used for moving the hero hero enemies (does not check for collision)
    public void moveTo(int newXPosition, int newYPosition) {
        hall.getGrid()[xPosition][yPosition] = null;
        hall.getGrid()[newXPosition][newYPosition] = this;
        this.xPosition = newXPosition;
        this.yPosition = newYPosition;
        updateCollisionArea();
    }

    // called every game tick, used in characters
    public abstract void update();

    // used for placing the collision rectangle where the currently entity is
    public void updateCollisionArea(int x, int y) {
        collisionArea.x = x + collisionX;
        collisionArea.y = y + collisionY;
    }

    public void updateCollisionArea() {
        collisionArea.x = getXPixelPosition() + collisionX;
        collisionArea.y = getYPixelPosition() + collisionY;
    }

    public abstract Image getSprite(SpriteLoader spriteLoader);

    public int getXPosition() {
        return xPosition;
    }
    
    public int getYPosition() {
        return yPosition;
    }

    public int getXPixelPosition() {
        return xPosition * Hall.getPixelsPerTile();
    }
    
    public int getYPixelPosition() {
        return yPosition * Hall.getPixelsPerTile();
    }

    public int getWidth() {
        return 1;
    }

    public int getHeight() {
        return 1;
    }

    public boolean isProp() {
        return false;
    }

    public boolean isEnchantment() {
        return false;
    }

    public abstract int getShadowX();

    public abstract int getShadowY();

    public abstract int getShadowHeight();

    public abstract int getShadowWidth();
}
