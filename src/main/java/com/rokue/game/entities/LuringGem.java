package com.rokue.game.entities;

import java.awt.Image;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class LuringGem extends Enchantment {

    public LuringGem() {
        super();
        
        collisionX = 3;
        collisionY = 0;
        collisionWidth = Hall.getPixelsPerTile() - 6;
        collisionHeight = Hall.getPixelsPerTile() + 2;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
    }
    

    @Override
    public void update() { }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        return spriteLoader.getEnchantmentSprites()[(pickable && selected) ? 6 : 2];
    }
    
}
