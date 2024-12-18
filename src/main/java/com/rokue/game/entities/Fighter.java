package com.rokue.game.entities;

import java.awt.Image;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class Fighter extends Character{
    private boolean sideOfHero = false;

    private long lastMoveTime = 0;
    private final long moveDelay = 500;
    private boolean followLuringGem = false;

    public Fighter() {
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
    }

    public void followLuringGem() {
        followLuringGem = true;
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        return spriteLoader.getMonsterSprites()[sideOfHero ? 3 : 4];
    }
    
}
