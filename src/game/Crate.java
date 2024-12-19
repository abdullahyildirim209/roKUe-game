package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Crate extends Entity {
    Image sprite;

    // 0 = invisible, 1 = crate
    static final Image[] sprites = {null, new ImageIcon(Hero.class.getResource("sprites/objects/Crate.png")).getImage()};

    Crate (int spriteNo) {
        super();
        sprite = sprites[spriteNo];
        collisionX = 3;
        collisionY = 16;
        collisionWidth = 10;
        collisionHeight = 1;
    }

    Crate () {
        super();
        sprite = sprites[1];
        collisionX = 3;
        collisionY = 16;
        collisionWidth = 10;
        collisionHeight = 1;
    }

    @Override
    void update() {}

    @Override
    Image getSprite() {
        return sprite;
    }

    
    @Override
    int getYPixelPosition() {
        return super.getYPixelPosition() - Hall.pixelsPerTile;
    }

    @Override
    int getHeight() {
        return 2;
    }
}
