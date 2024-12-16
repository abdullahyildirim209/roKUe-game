package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;

import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;

public class Hero extends Entity {
    private Hall hall;
    private int currentFrame = 0;
    private final Keyboard keyboard;
    private final Image[] sprites = new Image[8];
    private Image cloakLeftSprite;
    private Image cloakRightSprite;
    private long lastMoveTime = 0;
    private final long moveDelay = 200;
    private String direction = "down";
    private int health = 3;

    private boolean cloakActive = false;
    private long cloakStartTime = 0;
    private final long cloakDuration = 20000;

    // Constructor
    public Hero(Keyboard keyboard) {
        super();
        this.keyboard = keyboard;

        for (int i = 0; i < 8; i++) {
            sprites[i] = new ImageIcon(getClass().getResource("/sprites/hero/" + i + ".png")).getImage();
        }

        cloakLeftSprite = new ImageIcon(getClass().getResource("/sprites/objects/cloakOfProtectionLeft.png")).getImage();
        cloakRightSprite = new ImageIcon(getClass().getResource("/sprites/objects/cloakOfProtectionRight.png")).getImage();
    }

    public void place(int x, int y, Hall hall) {
        this.xPosition = x;
        this.yPosition = y;
        this.hall = hall;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        // Activate cloak on pressing 'P'
        if (keyboard.p && !cloakActive) {
            activateCloak();
        }

        if (cloakActive && currentTime - cloakStartTime > cloakDuration) {
            deactivateCloak();
        }

        if (currentTime - lastMoveTime < moveDelay) {
            return; // Movement delay
        }

        boolean moved = false;

        if (keyboard.up && yPosition > 0 && hall.isPositionEmpty(xPosition, yPosition - 1)) {
            yPosition--;
            if (!direction.equals("up")) {
                currentFrame = 0; // Reset animation
            }
            direction = "up";
            moved = true;
        }
        // Move Down
        else if (keyboard.down && yPosition < Hall.tiles - 1 && hall.isPositionEmpty(xPosition, yPosition + 1)) {
            yPosition++;
            if (!direction.equals("down")) {
                currentFrame = 0;
            }
            direction = "down";
            moved = true;
        }
        // Move Left
        if (keyboard.left && xPosition > 0 && hall.isPositionEmpty(xPosition - 1, yPosition)) {
            xPosition--;
            if (!direction.equals("left")) {
                currentFrame = 0;
            }
            direction = "left";
            moved = true;
        }
        // Move Right
        else if (keyboard.right && xPosition < Hall.tiles - 1 && hall.isPositionEmpty(xPosition + 1, yPosition)) {
            xPosition++;
            if (!direction.equals("right")) {
                currentFrame = 0;
            }
            direction = "right";
            moved = true;
        }

        if (moved) {
            currentFrame = (currentFrame + 1) % 2; // Toggle frames 0-1
            lastMoveTime = currentTime;
        }
    }

    public void decreaseHealth() {
        if (!cloakActive) {
            if (health > 0) {
                health--;
                System.out.println("Hero health decreased to: " + health);
            }
            if (health == 0) {
                System.out.println("Game Over!");
                // Add logic to end the game
            }
        }
    }

    // Activate the cloak
    private void activateCloak() {
        this.cloakActive = true;
        this.cloakStartTime = System.currentTimeMillis();
        System.out.println("Cloak activated! Duration: 20 seconds");
    }

    // Deactivate the cloak
    private void deactivateCloak() {
        this.cloakActive = false;
        System.out.println("Cloak expired!");
    }

    // Check if cloak is active
    public boolean isCloakActive() {
        return cloakActive;
    }
    public long getCloakDuration() {
        return cloakDuration;
    }
    public long getCloakStartTime() {
        return cloakStartTime;
    }

    public long getRemainingCloakTime() {
        if (cloakActive) {
            long elapsedTime = System.currentTimeMillis() - cloakStartTime;
            return (cloakDuration - elapsedTime) / 1000; // Remaining time in seconds
        }
        return 0;
    }

    // Return hero health
    public int getHealth() {
        return health;
    }



    @Override
    public Image getSprite() {
        if (isCloakActive()) {
            switch (direction) {
                case "left":
                    return cloakLeftSprite;
                case "right":
                    return cloakRightSprite;
                case "up":
                case "down":
                    return cloakLeftSprite;
            }
        }

        switch (direction) {
            case "up":
                return sprites[4 + currentFrame];
            case "down":
                return sprites[0 + currentFrame];
            case "left":
                return sprites[2 + currentFrame];
            case "right":
                return sprites[6 + currentFrame];
            default:
                return sprites[0];
        }
    }
}
