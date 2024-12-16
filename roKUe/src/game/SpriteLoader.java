package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class SpriteLoader {
    int currentHallNo = 0;

    // 0 = down, 2 = left, 4 = up, 6 = right (stand)
    // 1 = down, 3 = left, 5 = up, 7 = right (walk)
    Image[] heroSprites = new Image[8];

    // 0 = door left, 2 = door right, 4 = crate (not open / highlighted)
    // 1 = door left, 3 = door right, 5 = crate (open / highlighted)
    Image[] propSprites = new Image[6];

    // no sprites yet
    Image[] enemySprites;

    // 0 = water, 1 = earth, 2 = fire, 3 = air
    Image[] runeSprites = new Image[4];

    // 0 = background, 1 = bottom wall
    Image[] hallSprites = new Image[2];

    public SpriteLoader() {
        loadSprites(7, "hero", heroSprites);
        loadSprites(5, "prop", propSprites);
        loadSprites(1, "hall", hallSprites);
        loadSprites(3, "rune", runeSprites);
    }

    // end included
    private void loadSprites(int end, String entity, Image[] spriteArray) {
        for (int imageNumber = 0; imageNumber <= end; imageNumber++) {
            spriteArray[imageNumber] = new ImageIcon(SpriteLoader.class.getResource(String.format("sprites/%s/%d.png", entity, imageNumber))).getImage();
        }
    }
}

