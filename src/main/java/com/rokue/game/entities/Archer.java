package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;

public class Archer extends Character implements Serializable { 
    private static final long serialVersionUID = 1L;
    private final long attackInterval = 60;
    private long lastAttackTime = 0;
    private boolean sideOfHero = false;

    public Archer(){
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

        long currentTime = PlayPanel.tickTime;

        if (!hall.isHeroExit()) {
            int heroX = hall.getHero().getXPosition();
            int heroY = hall.getHero().getYPosition();
            int distance = Math.abs(heroX - xPosition) + Math.abs(heroY - yPosition);

            if (distance < 4 && currentTime - lastAttackTime > attackInterval && !hall.getHero().isCloakActive()) {
                hall.getHero().decreaseHealth();
                lastAttackTime = currentTime;
                SoundManager.playSound("archer");
            }
        }
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        return spriteLoader.getMonsterSprites()[sideOfHero ? 1 : 2];
    }

	public void setLastAttackTime(long lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}

	public void setSideOfHero(boolean sideOfHero) {
		this.sideOfHero = sideOfHero;
	}

	public long getAttackInterval() {
		return attackInterval;
	}

	public long getLastAttackTime() {
		return lastAttackTime;
	}

	public boolean isSideOfHero() {
		return sideOfHero;
	}
    
    
    
}
