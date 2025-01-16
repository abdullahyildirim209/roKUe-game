package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;

public class Wizard extends Character implements Serializable { 
    private static final long serialVersionUID = 1L;
    private final long changeInterval = 5 * 60;
    private long lastChangeTime = 0;

    public Wizard() {
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
        long currentTime = PlayPanel.tickTime;

        if (!hall.isHeroExit() && !hall.isDoorOpen()){
            if (currentTime - lastChangeTime > changeInterval) {
                hall.changeRunePos();
                System.out.println("Wizard: Rune pos changed");
                SoundManager.playSound("wizard");
                lastChangeTime = currentTime;
            }
        }
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
       return spriteLoader.getMonsterSprites()[0];
    }

    
}
