package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class ExtraLife extends Enchantment implements Serializable { 
    private static final long serialVersionUID = 1L;
    private boolean open = false;

    public ExtraLife() {
        super();
        collisionY = 16;
    }
    
    @Override
    public void update() { }

    public void openChest() {
        open = true;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public int getYPixelPosition() {
        return super.getYPixelPosition() - Hall.getPixelsPerTile();
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public int getShadowX() {
        return getXPixelPosition() + 0;
    }

    @Override
    public int getShadowY() {
        return getYPixelPosition() + 32;
    }

    @Override
    public int getShadowHeight() {
        if (open) return 15; 
        return 11;
    }

    @Override
    public int getShadowWidth() {
        return 16;
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        int x = 8;
        if (selected) x += 1;
        if (open) x += 2;
        return spriteLoader.getEnchantmentSprites()[x];
    }
}
