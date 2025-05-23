package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class RevealRune extends Enchantment implements Serializable { 
    private static final long serialVersionUID = 1L;

    public RevealRune() {
        super();
        collisionX = 1;
        collisionY = 0;
        collisionWidth = Hall.getPixelsPerTile() - 2;
        collisionHeight = Hall.getPixelsPerTile() + 2;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
    }
    
    @Override
    public void update() { }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        return spriteLoader.getEnchantmentSprites()[(pickable && selected) ? 5 : 1];
    }
}
