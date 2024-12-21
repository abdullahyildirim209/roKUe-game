package com.rokue.game.entities;

import java.awt.Image;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class Wizard extends Character {
    private final long changeInterval = 5000;
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
        long currentTime = System.currentTimeMillis();

        if (!hall.isHeroExit() && !hall.isDoorOpen()){
            if (currentTime - lastChangeTime > changeInterval) {
                hall.changeRunePos();
                System.out.println("Wizard: Rune pos changed");
                lastChangeTime = currentTime;
            }
        }
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
       return spriteLoader.getMonsterSprites()[0];
    }

    
}
