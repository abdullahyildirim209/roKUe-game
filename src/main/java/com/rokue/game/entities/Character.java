package com.rokue.game.entities;

import com.rokue.game.map.Hall;

public abstract class Character extends Entity {
    int xPixelPosition;
    int yPixelPosition;

    public Character() {}

    // add to the character set in the hall and center it to the placed tile
    @Override
    public void place(int xPosition, int yPosition, Hall hall) {
        this.xPixelPosition = xPosition * Hall.getPixelsPerTile();
        this.yPixelPosition = yPosition * Hall.getPixelsPerTile() - 7;
        super.place(xPosition, yPosition, hall);
        hall.getCharacters().add(this);
    }

    @Override
    public int getXPixelPosition() {
        return this.xPixelPosition;
    }
    
    @Override
    public int getYPixelPosition() {
        return this.yPixelPosition;
    }

    @Override
    public int getShadowX() {
        return getXPixelPosition() + 3;
    }

    @Override
    public int getShadowY() {
        return getYPixelPosition() + 15;
    }

    @Override
    public int getShadowHeight() {
        return 10;
    }

    @Override
    public int getShadowWidth() {
        return 10;
    }
}
