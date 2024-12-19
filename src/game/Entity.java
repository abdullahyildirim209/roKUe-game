package game;

import java.awt.Image;
import java.awt.Rectangle;

abstract class Entity {
    
    int xPosition;
    int yPosition;
    Hall hall;
    
    int collisionX;
    int collisionY;
    int collisionWidth;
    int collisionHeight;
    Rectangle collisionArea = new Rectangle();

    Entity() {
        collisionX = 0;
        collisionY = 0;
        collisionWidth = Hall.pixelsPerTile;
        collisionHeight = Hall.pixelsPerTile;
    }

    void place(int xPosition, int yPosition, Hall hall) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        

        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
        updateCollisionArea();

        this.hall = hall;
        hall.grid[xPosition][yPosition] = this;
        hall.entities.add(this);
    }

    void randomlyPlace(Hall hall) {
        int[] position = hall.getRandomEmptyTilePosition();
        this.place(position[0], position[1], hall);
    }

    void moveTo(int newXPosition, int newYPosition) {
        hall.grid[xPosition][yPosition] = null;
        hall.grid[newXPosition][newYPosition] = this;
        this.xPosition = newXPosition;
        this.yPosition = newYPosition;
        updateCollisionArea();
    }

    abstract void update();

    abstract Image getSprite();

    void updateCollisionArea(int x, int y) {
        collisionArea.x = x + collisionX;
        collisionArea.y = y + collisionY;
    }

    void updateCollisionArea() {
        collisionArea.x = getXPixelPosition() + collisionX;
        collisionArea.y = getYPixelPosition() + collisionY;
    }

    int getXPosition() {
        return xPosition;
    }
    
    int getYPosition() {
        return yPosition;
    }

    int getXPixelPosition() {
        return xPosition * Hall.pixelsPerTile;
    }
    
    int getYPixelPosition() {
        return yPosition * Hall.pixelsPerTile;
    }

    int getWidth() {
        return 1;
    }

    int getHeight() {
        return 1;
    }
}
