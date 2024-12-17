package com.rokue.game.entities;

import java.awt.Image;
import javax.swing.ImageIcon;
import com.rokue.game.map.Hall;

public class Wizard extends Monster{
    private Hall hall;
    private final Image sprite;
    private final long changeInterval = 5000;
    private long lastChangeTime = 0;

    public Wizard() {
        super();
        sprite = new ImageIcon(getClass().getResource("/sprites/monsters/wizard.png")).getImage();
    }

    public void place(int x, int y, Hall hall) {
        this.xPosition = x;
        this.yPosition = y;
        this.hall = hall;
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (hall.getHero() != null){
            if (currentTime - lastChangeTime > changeInterval) {
                hall.changeRunePos();
                System.out.println("Wizard: Rune pos changed");
                lastChangeTime = currentTime;
            }
        }
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

}
