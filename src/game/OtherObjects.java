package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class OtherObjects extends Entity{
	Image sprite;
	
	OtherObjects (String imgPath) {
        super();
        sprite = new ImageIcon(Hero.class.getResource(imgPath)).getImage();
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
