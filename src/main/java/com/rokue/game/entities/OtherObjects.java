package com.rokue.game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class OtherObjects extends Prop {
    private final Image sprite;

    public OtherObjects(String imgPath) {
        super(3); // Assuming 4 is the ID for generic props like crates or decorations
        java.net.URL resource = OtherObjects.class.getResource(imgPath);
        if (resource == null) {
            System.out.println("Resource not found: /sprites/images/" + imgPath);
            throw new IllegalArgumentException("Resource not found: /sprites/images/" + imgPath);
        }
        this.sprite = new ImageIcon(resource).getImage();
    }
    

    @Override
    public void place(int xPosition, int yPosition, Hall hall) {
        super.place(xPosition, yPosition, hall);
        hall.getProps().add(this);
        System.out.println("object konuldu" );
    }

    @Override
    public void update() {
        // No behavior updates for generic objects
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
        return sprite; // Use the custom sprite loaded via the constructor
    }

    @Override
    public int getYPixelPosition() {
        return super.getYPixelPosition() - Hall.getPixelsPerTile(); // Adjust position for 2-tile tall objects
    }

    @Override
    public int getHeight() {
        return 2; // Assume 2-tile height for OtherObjects
    }
}
