package com.rokue.game.entities;

import com.rokue.game.map.Hall;

public abstract class Character extends Entity {
    int xPixelPosition;
    int yPixelPosition;

    public Character() {}

    @Override
    void place(int xPosition, int yPosition, Hall hall) {
        this.xPixelPosition = xPosition * Hall.pixelsPerTile;
        this.yPixelPosition = yPosition * Hall.pixelsPerTile - 7;
        super.place(xPosition, yPosition, hall);
        hall.characters.add(this);
    }

    @Override
    int getXPixelPosition() {
        return this.xPixelPosition;
    }

    @Override
    int getYPixelPosition() {
        return this.yPixelPosition;
    }

    @Override
    int getShadowX() {
        return getXPixelPosition() + 3;
    }

    @Override
    int getShadowY() {
        return getYPixelPosition() + 15;
    }

    @Override
    int getShadowHeight() {
        return 10;
    }

    @Override
    int getShadowWidth() {
        return 10;
    }
}
