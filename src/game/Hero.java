package game;

import java.awt.Image;

import javax.swing.ImageIcon;

class Hero extends Entity {
    int xPixelPosition;
    int yPixelPosition;

    Keyboard keyboard;

    int looking = 0; // 0 = down, 2 = left, 4 = up, 6 = right
    boolean running = false;
    int runStage = 0; // 0 = default, 1 = legs up
    int runTimer = 0;
    Image[] sprites = new Image[8];

    int collisionX;
    int collisionY;
    int collisionWidth;
    int collisionHeight;

    Hero(Keyboard keyboard) {
        super();

        this.keyboard = keyboard;

        for (int imageNumber = 0; imageNumber <= 7; imageNumber++) {
            sprites[imageNumber] = new ImageIcon(Hero.class.getResource(String.format("sprites/hero/%d.png", imageNumber))).getImage();
        }
    }

    @Override
    void place(int xPosition, int yPosition, Hall hall) {
        this.xPixelPosition = xPosition * Hall.pixelsPerTile;
        this.yPixelPosition = yPosition * Hall.pixelsPerTile - 7;
        super.place(xPosition, yPosition, hall);
    }

    void move() {
        running = false;
        runStage = 0;

        // moving
        if (keyboard.left) {
            if (!keyboard.right) {
                looking = 2;
                if (!checkLeftCollision()) {
                    xPixelPosition--;
                    running = true;
                }
            }
        } else if (keyboard.right ) {
            looking = 6;
            if (!checkRightCollision()) {
                xPixelPosition++;
                running = true;
            }
        }
        if (keyboard.up) {
            if (!keyboard.down) {
                looking = 4;
                if (!checkUpCollision()) {
                    yPixelPosition--;
                    running = true;
                }
            }
        } else if (keyboard.down) {
            looking = 0;
            if (!checkDownCollision()) {
                yPixelPosition++;
                running = true;
            }
        }

        // running animation
        if (running) {
            if (runTimer < 10) { // 10 frames legs up
                runStage = 1;
                runTimer++;
            } else if (runTimer < 19) { // 9 + 1 frames default
                runTimer++;
            } else {
                runTimer = 0;
            }
        }
        
        moveTo((xPixelPosition + 8) / 16, (yPixelPosition + 15) / 16);
    }

    void update() {
        move();
    }

    Image getSprite() {
        return sprites[looking + runStage];
    }

    @Override
    int getXPixelPosition() {
        return this.xPixelPosition;
    }
    
    @Override
    int getYPixelPosition() {
        return this.yPixelPosition;
    }

    int[] getObservedTilePosition() {
        int[] position = new int[2];
        switch (this.looking) {
            case 0:
                position[0] = this.getXPosition();
                position[1] = this.getYPosition() + 1;
                break;
            case 2:
                position[0] = this.getXPosition() - 1;
                position[1] = this.getYPosition();
                break;
            case 4:
                position[0] = this.getXPosition();
                position[1] = this.getYPosition() - 1;
                break;
            case 6:
                position[0] = this.getXPosition() + 1;
                position[1] = this.getYPosition();
                break;
        }
        return position;
    }

    boolean checkDownCollision() {
        updateCollisionArea(xPixelPosition, yPixelPosition + 1);
        return hall.checkCollision(this, xPosition, yPosition + 1) || hall.checkCollision(this, xPosition + 1, yPosition + 1) || hall.checkCollision(this, xPosition - 1, yPosition + 1);
    }

    boolean checkUpCollision() {
        updateCollisionArea(xPixelPosition, yPixelPosition - 1);
        return hall.checkCollision(this, xPosition, yPosition - 1) || hall.checkCollision(this, xPosition + 1, yPosition - 1) || hall.checkCollision(this, xPosition - 1, yPosition - 1);
    }

    boolean checkRightCollision() {
        updateCollisionArea(xPixelPosition + 1, yPixelPosition);
        return hall.checkCollision(this, xPosition + 1, yPosition) || hall.checkCollision(this, xPosition + 1, yPosition + 1) || hall.checkCollision(this, xPosition + 1, yPosition - 1);
    }

    boolean checkLeftCollision() {
        updateCollisionArea(xPixelPosition - 1, yPixelPosition);
        return hall.checkCollision(this, xPosition - 1, yPosition) || hall.checkCollision(this, xPosition - 1, yPosition + 1) || hall.checkCollision(this, xPosition - 1, yPosition - 1);
    }
}
