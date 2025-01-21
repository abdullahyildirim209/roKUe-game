package com.rokue.game.entities;

import java.awt.Image;
import java.util.Arrays;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;

public class Hero extends Character{
    Keyboard keyboard;

    int looking = 0; // 0 = down, 2 = left, 4 = up, 6 = right
    boolean lastSideLooking = false;
    boolean running = false;
    int runStage = 0; // 0 = default, 1 = legs up
    int runTimer = 0;
    public int health = 3;
    int[] inventory = {0, 0, 0};
    boolean cloakActive = false;
    private long cloakStartTime = 0; // When the cloak was activated
    private final long cloakDuration = 20 * 60; // Cloak duration (20 seconds in ticks)

    Prop selectedProp;
    Enchantment selectedEnchantment;

    public Hero(Keyboard keyboard) {
        super();
        this.keyboard = keyboard;

        collisionX = 3;
        collisionY = 15;
        collisionWidth = 10;
        collisionHeight = 1;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
    }

    public int getHealth() {
        return health;
    }

    public long getCloakStartTime() {
        return cloakStartTime;
    }

    public long getCloakDuration() {
        return cloakDuration;
    }

    public boolean isCloakActive() {
        return cloakActive;
    }

    @Override
    public void randomlyPlace(Hall hall) {
        int[] position = hall.getRandomEmptyTilePosition();
        this.place(position[0], position[1], hall);
        hall.setHero(this);
    }

    public void randomlyChangePlace() {
        int[] position = hall.getRandomEmptyTilePosition();
        hall.getGrid()[xPosition][yPosition] = null;
        xPosition = position[0];
        yPosition = position[1];
        xPixelPosition = xPosition * Hall.getPixelsPerTile();
        yPixelPosition = yPosition * Hall.getPixelsPerTile() - 7;
        hall.getGrid()[xPosition][yPosition] = this;
    }


    public void decreaseHealth() {
        if (health > 0) {
            health--;
        }
        if (health == 0) {
            System.out.println("Hero died!");
            SoundManager.playSound("fail");
        }
    }

    public void addToInventory(Enchantment e) {
        if (e instanceof RevealRune) inventory[0]++;
        else if (e instanceof CloakOfProtection) inventory[1]++;
        else if (e instanceof LuringGem) inventory[2]++;
        else return;
    }

    public boolean removeFromInventory(Enchantment e) {
        if ((e instanceof RevealRune) && inventory[0] != 0) {
            inventory[0]--;
            return true;
        }
        else if ((e instanceof CloakOfProtection) && inventory[1] != 0) {
            inventory[1]--;
            return true;
        }
        if ((e instanceof LuringGem) && inventory[2] != 0) {
            inventory[2]--;
            return true;
        }
        else {
            System.out.println("Not in the inventory");
            return false;
        }
    }

    public void update() {
        revealRune();
        activateCloak();
        deactivateCloak();
        throwLuringGem();
        move();
        selectProp();
        selectEnchantment();
        use();
        checkMouseInteraction(); // new
    }

    // Activate Reveal Rune on pressing 'R'
    public void revealRune() {
        if (keyboard.r && removeFromInventory(new RevealRune())) {
            hall.revealRune();
            keyboard.r = false;
        }
    }

    // Activate cloak on pressing 'P'
    public void activateCloak() {
        if (keyboard.p && !cloakActive && removeFromInventory(new CloakOfProtection())) {
            cloakStartTime = PlayPanel.tickTime;
            cloakActive = true;
            keyboard.p = false;
        }
    }

    public void deactivateCloak() {
        if (cloakActive && PlayPanel.tickTime - cloakStartTime > cloakDuration) {
            cloakActive = false;
        }
    }

    // Throw Luring Gem on pressing 'B'
    public void throwLuringGem() {
        if (hall.getActiveLuringGem() == null && keyboard.b) {
            int gemX = xPosition;
            int gemY = yPosition;
            int direction = -1;

            if (keyboard.a) {
                direction = 0;
                gemX--;
            } else if (keyboard.d) {
                direction = 1;
                gemX++;
            } else if (keyboard.w) {
                direction = 2;
                gemY--;
            } else if (keyboard.s) {
                direction = 3;
                gemY++;
            }

            if (direction != -1 && hall.getGrid()[gemX][gemY] == null && removeFromInventory(new LuringGem())) {
                hall.placeLuringGem(gemX, gemY);
                keyboard.b = false;
            }            
        }
    }

    // checks keyboard input, moves 1 pixel to that direction if no collision occurs, updates running and looking variables accordingly
    // this makes it so that moving diagonally is faster, might fix later
    public void move() {
        running = false;
        runStage = 0;

        // moving
        if (keyboard.left) {
            if (!keyboard.right) {
                looking = 2;
                lastSideLooking = true;
                if (!checkLeftCollision()) {
                    xPixelPosition--;
                    running = true;
                }
            }
        } else if (keyboard.right ) {
            looking = 6;
            lastSideLooking = false;
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

    // used for getting the prop hero is looking at from the hall (selected prop)
    public int[] getObservedTilePosition() {
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
    public void selectProp() {
        if (selectedProp != null) selectedProp.selected = 0;
        selectedProp = hall.getProp(getObservedTilePosition());
        if (selectedProp != null) selectedProp.selected = 1;
    }
 
    public void selectEnchantment() {
        if (selectedEnchantment != null) selectedEnchantment.setSelected(false);
        selectedEnchantment = hall.getEnchantment(getObservedTilePosition());
        if (selectedEnchantment != null) selectedEnchantment.setSelected(true);
    }

    // used when hero searches the highlighted prop for the rune and picks enchantments
    public void use() {
        if (keyboard.use) {
            keyboard.use = false;
            if (selectedProp != null) {
                selectedProp.search();
            }

            if (selectedEnchantment != null && selectedEnchantment.isPickable()) {
                keyboard.use = false;
                if (selectedEnchantment instanceof ExtraTime) {
                    hall.setTime(hall.getTime() + 5);
                }
                if (selectedEnchantment instanceof ExtraLife) {
                    if (!((ExtraLife) selectedEnchantment).isOpen()) {
                        ((ExtraLife) selectedEnchantment).openChest();
                        return;
                    }
                    else {
                        if (health < 5) {
                            health++; 
                        }
                    }
                }
                else {
                    addToInventory(selectedEnchantment);
                }

                SoundManager.playSound("itemCollected");
                hall.getEnchantments().remove(selectedEnchantment);
                hall.getGrid()[selectedEnchantment.getXPosition()][selectedEnchantment.getYPosition()] = null;
                System.out.println(Arrays.toString(inventory));
            }
        } 
    }

    private void checkMouseInteraction() {
        if (keyboard.mouseButtonPressed) {
            keyboard.mouseButtonPressed = false; // Reset so we don't spam interactions
            if (selectedProp != null) {
                // Optionally check "distance" if you only want close interactions
                // e.g., if (distanceTo(selectedProp) <= 1) selectedProp.interact();
                selectedProp.interact();  
            }

            if (selectedEnchantment != null && selectedEnchantment.isPickable()) {
                keyboard.use = false;
                if (selectedEnchantment instanceof ExtraTime) {
                    hall.setTime(hall.getTime() + 5);
                }
                if (selectedEnchantment instanceof ExtraLife) {
                    if (!((ExtraLife) selectedEnchantment).isOpen()) {
                        ((ExtraLife) selectedEnchantment).openChest();
                        return;
                    }
                    else {
                        if (health < 5) {
                            health++; 
                        }
                    }
                }
                else {
                    addToInventory(selectedEnchantment);
                }

                SoundManager.playSound("itemCollected");
                hall.getEnchantments().remove(selectedEnchantment);
                hall.getGrid()[selectedEnchantment.getXPosition()][selectedEnchantment.getYPosition()] = null;
                System.out.println(Arrays.toString(inventory));
            }
        }
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

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        if (!cloakActive) return spriteLoader.getHeroSprites()[looking + runStage];
        else {
            return spriteLoader.getHeroSprites()[lastSideLooking ? 8 : 9];
        }
    }


    @Override
    public int getShadowWidth() {
        return (cloakActive ? 15 : 10);
    }

    @Override
    public int getShadowX() {
        return getXPixelPosition() + (cloakActive ? 1 : 3);
    }

	public int[] getInventory() {
		return inventory;
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	public void setLooking(int looking) {
		this.looking = looking;
	}

	public void setLastSideLooking(boolean lastSideLooking) {
		this.lastSideLooking = lastSideLooking;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setRunStage(int runStage) {
		this.runStage = runStage;
	}

	public void setRunTimer(int runTimer) {
		this.runTimer = runTimer;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setInventory(int[] inventory) {
		this.inventory = inventory;
	}

	public void setCloakActive(boolean cloakActive) {
		this.cloakActive = cloakActive;
	}

	public void setCloakStartTime(long cloakStartTime) {
		this.cloakStartTime = cloakStartTime;
	}

	public void setSelectedProp(Prop selectedProp) {
		this.selectedProp = selectedProp;
	}

	public void setSelectedEnchantment(Enchantment selectedEnchantment) {
		this.selectedEnchantment = selectedEnchantment;
	}
	
	
    
}
