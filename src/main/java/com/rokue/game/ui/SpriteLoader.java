package com.rokue.game.ui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class SpriteLoader {
    int currentHallNo = 0;

    // 0 = down, 2 = left, 4 = up, 6 = right (stand)
    // 1 = down, 3 = left, 5 = up, 7 = right (walk)
    Image[] heroSprites = new Image[10];

    // 0 = door left, 2 = door right, 4 = healtChest,               6 = openHealtChest,               8 = crate (not open / highlighted)
    // 1 = door left, 3 = door right, 5 = healtChest (highlighted), 7 = openHealtChest (highlighted), 9 = crate (open / highlighted)
    Image[] propSprites = new Image[30];

    // 0 = water, 1 = earth, 2 = fire, 3 = air
    Image[] runeSprites = new Image[4];

    // 0 = background, 1 = bottom wall
    Image[] hallSprites = new Image[2];

    // 0,1,2 = wizard, 3,4 = archer, 5,6 = fighter
    Image[] monsterSprites = new Image[7];

    Image[] enchantmentSprites = new Image[8];

    Image[] arrowSprites = new Image[8];



    public SpriteLoader() {
        loadSprites(9, "hero", heroSprites);
        loadSprites(29, "prop", propSprites);
        loadSprites(1, "hall", hallSprites);
        loadSprites(3, "rune", runeSprites);
        loadSprites(6, "monster", monsterSprites);
        loadSprites(7, "enchantment", enchantmentSprites);
        loadSprites(7, "arrow", arrowSprites);
        
    }

    public int getCurrentHallNo() {
        return currentHallNo;
    }

    public Image[] getHeroSprites() {
        return heroSprites;
    }

    public Image[] getPropSprites() {
        return propSprites;
    }
    
    public Image[] getRuneSprites() {
        return runeSprites;
    }

    public Image[] getMonsterSprites() {
        return monsterSprites;
    }

    public Image[] getEnchantmentSprites() {
        return enchantmentSprites;
    }

    // end included
    private void loadSprites(int end, String entity, Image[] spriteArray) {
        for (int imageNumber = 0; imageNumber <= end; imageNumber++) {
            spriteArray[imageNumber] = new ImageIcon(SpriteLoader.class.getResource(String.format("/sprites/%s/%d.png", entity, imageNumber))).getImage();
        }
    }

    public Image[] getArrowSprites() {
        return arrowSprites;
    }
}

