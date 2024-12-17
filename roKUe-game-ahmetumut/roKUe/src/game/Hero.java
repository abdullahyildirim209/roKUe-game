package game;

import java.awt.Image;

class Hero extends Character {
    Keyboard keyboard;

    int looking = 0; // 0 = down, 2 = left, 4 = up, 6 = right
    boolean running = false;
    int runStage = 0; // 0 = default, 1 = legs up
    int runTimer = 0;

    Prop selectedProp;

    Hero(Keyboard keyboard) {
        super();
        this.keyboard = keyboard;

        collisionX = 3;
        collisionY = 15;
        collisionWidth = 10;
        collisionHeight = 1;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
    }

    // checks keyboard input, moves 1 pixel to that direction if no collision occurs, updates running and looking variables accordingly
    // this makes it so that moving diagonally is faster, might fix later
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
                runTimer = 0; // + 1 frame comes from this
            }
        }
        
        moveTo((xPixelPosition + 8) / 16, (yPixelPosition + 15) / 16);
    }

    void update() {
        move();
        selectProp();
        searchProp();
    }

    // used for getting the prop hero is looking at from the hall (selected prop)
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

    // used for highlighting the prop hero is looking at
    void selectProp() {
        if (selectedProp != null) selectedProp.selected = 0;
        selectedProp = hall.getProp(getObservedTilePosition());
        if (selectedProp != null) selectedProp.selected = 1;
    }

    // used when hero searches the highlighted prop for the rune
    void searchProp() {
        if (keyboard.use) {
            keyboard.use = false;
            if (selectedProp != null) {
                selectedProp.search();
            }
        } 
    }

    // might clean this up later, checks for collision in 3 tiles in bottom/top/right/left, function is chosen depending on which direction the hero is trying to move
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

    @Override
    Image getSprite(SpriteLoader spriteLoader) {
        return spriteLoader.heroSprites[looking + runStage];
    }
}
