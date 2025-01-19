package com.rokue.game.entities;

import java.awt.Image;
import java.io.Serializable;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.SpriteLoader;

public class Wizard extends Character implements Serializable { 
    private static final long serialVersionUID = 1L;
    private WizardBehavior wb;
    private boolean active;
    private int spriteNum = 0;
    

    public Wizard() {
        super();
        active = true;
        collisionX = 1;
        collisionY = 5;
        collisionWidth = Hall.getPixelsPerTile() - 2;
        collisionHeight = Hall.getPixelsPerTile() + 2;
        collisionArea.width = collisionWidth;
        collisionArea.height = collisionHeight;

    }

    public void setActive(boolean a) {
        active = a;
    }

    public boolean isActive() {
        return active;
    }

    public void setSpriteNum(int x) {
        spriteNum = x;
    }

    @Override
    public void update() {
        if (active) {
            int totalTime = hall.getTotalTime();
            int time = hall.getTime();

            if ((float)time/totalTime < 0.3) {
                System.out.println("w1");
                if (!(wb instanceof WizardBehavior1)) {
                    wb = new WizardBehavior1(hall, this);
                }
            }
            else if ((float)time/totalTime > 0.7) {
                System.out.println("w2");
                if (!(wb instanceof  WizardBehavior2)) {
                    wb = new WizardBehavior2(hall, this);
                }
            }
            else {
                System.out.println("w3");
                if (!(wb instanceof WizardBehavior3)) {
                    wb = new WizardBehavior3(hall, this);
                }
            }

            wb.behave();
        }
    }

    @Override
    public Image getSprite(SpriteLoader spriteLoader) {
       return spriteLoader.getMonsterSprites()[spriteNum];
    }

    
}
