package game;

import java.awt.Image;

public class Prop extends Entity {
    Image sprite;

    // -1 = invisible, 0 = doorLeft, 1 = doorRight, 2 = crate
    int ID;

    // 0 = not selected, 1 = selected
    int selected;

    public Prop (int ID) {
        super();
        this.ID = ID;
        this.selected = 0;
        collisionY = 16;
        if (ID < 2) {
            shadow = false;
        }
    }

    @Override
    public
    void place(int xPosition, int yPosition, Hall hall) {
        super.place(xPosition, yPosition, hall);
        if (ID < 2) {
            hall.nonRuneProps.add(this);
        } else {
            hall.props.add(this);
        }
    }

    @Override
    void update() {}

    // prop images are 2 tiles tall so its pixel position is 1 tile up
    @Override
    int getYPixelPosition() {
        return super.getYPixelPosition() - Hall.pixelsPerTile;
    }

    @Override
    int getHeight() {
        return 2;
    }

    @Override
    Image getSprite(SpriteLoader spriteLoader) {
        if (ID == -1) return null;
        if (ID < 2) {
            if (hall.doorOpen) {
                return spriteLoader.propSprites[2 * ID + 1];
            } else {
                return spriteLoader.propSprites[2 * ID];
            }
        }
        if (hall.doorOpen && hall.runeHolder == this) return spriteLoader.runeSprites[spriteLoader.currentHallNo];
        return spriteLoader.propSprites[2 * ID + selected];  
    }

    @Override
    boolean isProp() {
        return true;
    }

    void search() {
        if ((ID == 0 || ID == 1) && hall.doorOpen) {
            hall.heroExit = true;
        } else if (hall.runeHolder == this) {
            hall.doorOpen = true;
        }
    }

    @Override
    int getShadowX() {
        if (hall.doorOpen && hall.runeHolder == this) return getXPixelPosition() + 3;
        return getXPixelPosition() + 0;
    }

    @Override
    int getShadowY() {
        if (hall.doorOpen && hall.runeHolder == this) return getYPixelPosition() + 26;
        return getYPixelPosition() + 32;
    }

    @Override
    int getShadowHeight() {
        if (hall.doorOpen && hall.runeHolder == this) return 8;
        return 11;
    }

    @Override
    int getShadowWidth() {
        if (hall.doorOpen && hall.runeHolder == this) return 11;
        return 16;
    }
}
