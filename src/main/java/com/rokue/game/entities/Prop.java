package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class Prop extends Entity implements Serializable { 
    private static final long serialVersionUID = 1L;
    Image sprite;

    // -1 = invisible, 0 = doorLeft, 1 = doorRight, 2 = heartChest, 3 = openHeartChest, 4 = crate
    //5=blueFlag, 6=greenFlag, 7=yellowFlag, 8=redFlag, 9=earthDecorate, 10=fireDecorate, 
    //11=waterDecorate, 12=head, 13=ladder, 14=pillar 
    int ID;

    // 0 = not selected, 1 = selected
    int selected;

    public Prop (int ID) {
        super();
        this.ID = ID;
        this.selected = 0;
        collisionY = 16;
        if (ID < 2 || ID == 13) { // or ladder
            shadow = false;
        }
        if (ID == 12) { // head
            collisionX = 2;
            collisionWidth = 12;
        }
    }

    @Override
    public void place(int xPosition, int yPosition, Hall hall) {
        super.place(xPosition, yPosition, hall);
        if (ID < 4) {
            hall.getNonRuneProps().add(this);
        } else {
            hall.getProps().add(this);
        }
    }

    @Override
    public void update() {}

    // prop images are 2 tiles tall so its pixel position is 1 tile up
    @Override
    public int getYPixelPosition() {
        return super.getYPixelPosition() - Hall.getPixelsPerTile();
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override 
    public Image getSprite(SpriteLoader spriteLoader) {
        if (ID == -1) return null;
        if (ID < 2) {
            if (hall.isDoorOpen()) {
                return spriteLoader.getPropSprites()[2 * ID + 1];
            } else {
                return spriteLoader.getPropSprites()[2 * ID];
            }
        }
        if (hall.isDoorOpen() && hall.runeHolder == this) return spriteLoader.getRuneSprites()[spriteLoader.getCurrentHallNo()];
        return spriteLoader.getPropSprites()[2 * ID + selected];  
    }

    @Override
    public boolean isProp() {
        return true;
    }

    public void search() {
        if ((ID == 0 || ID == 1) && hall.isDoorOpen()) {
            hall.setHeroExit(true);
        } else if (ID == 2) {
            ID = 3;
        } else if (ID == 3) {
            hall.getHero().increaseHealth();
            hall.getNonRuneProps().remove(this);
            hall.getGrid()[xPosition][yPosition] = null;
        } else if (hall.runeHolder == this) {
            hall.setDoorOpen(true);

            SoundManager.playSound("doorOpen");
        }
    }

    @Override
    public int getShadowX() {
        if (hall.isDoorOpen() && hall.runeHolder == this) return getXPixelPosition() + 3;
        if (ID == 12) return getXPixelPosition() + 3; //head
        return getXPixelPosition() + 0;
    }

    @Override
    public int getShadowY() {
        if (hall.isDoorOpen() && hall.runeHolder == this) return getYPixelPosition() + 26;
        if (ID == 12) return getYPixelPosition() + 28; //head
        return getYPixelPosition() + 32;
    }

    @Override
    public int getShadowHeight() {
        if (hall.isDoorOpen() && hall.runeHolder == this) return 8;
        if (ID == 3) return 15; 
        if (ID == 12) return 10; //head
        if (ID == 14) return 24; //pillar
        return 11;
    }

    @Override
    public int getShadowWidth() {
        if (hall.isDoorOpen() && hall.runeHolder == this) return 11;
        if (ID == 12) return 10; //head
        return 16;
    }

}
