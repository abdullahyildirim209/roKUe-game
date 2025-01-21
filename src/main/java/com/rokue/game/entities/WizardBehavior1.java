package com.rokue.game.entities;

import java.io.Serializable;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;

public class WizardBehavior1 extends WizardBehavior implements Serializable{
    private static final long serialVersionUID = 1L;
    private final long randomlyPlaceTime =  60 / 2;
    private final long disappearTime = 1 * 60;
    private final long appearTime;
    private boolean teleported = false;
    
    public WizardBehavior1(Hall hall, Wizard wizard) {
        super(hall, wizard);
        appearTime = PlayPanel.tickTime;
    }

    @Override
    public void behave() {
        wizard.setSpriteNum(0);
        long currentTime = PlayPanel.tickTime;

        if (!teleported && currentTime - appearTime > randomlyPlaceTime) {
            hall.getHero().randomlyChangePlace();
            System.out.println("Hero teleported.");
            teleported = true;
        }
        if (currentTime - appearTime > disappearTime) {
            int x = wizard.getXPosition();
            int y = wizard.getYPosition();
            hall.getGrid()[x][y] = null;
            wizard.setActive(false);
        }
        
    }
    
}
