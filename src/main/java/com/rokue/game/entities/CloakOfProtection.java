package com.rokue.game.entities;

import java.awt.Image;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class CloakOfProtection extends Enchantment{

    public CloakOfProtection() {
        super();
        collisionX = 2;
        collisionY = 0;
        collisionWidth = Hall.getPixelsPerTile() - 3;
        collisionHeight = Hall.getPixelsPerTile() + 3;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;
    }

    @Override
    public void update() { }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        return spriteLoader.getEnchantmentSprites()[(pickable && selected) ? 4 : 0];
    }
}
