package com.rokue.game.entities;

import java.io.Serializable;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;

public class WizardBehavior3 extends WizardBehavior implements Serializable{
    private static final long serialVersionUID = 1L;
    private final long disappearTime = 2 * 60;
    private final long appearTime;
    
    public WizardBehavior3(Hall hall, Wizard wizard) {
        super(hall, wizard);
        appearTime = PlayPanel.tickTime;
    }

    @Override
    public void behave() {
        wizard.setSpriteNum(2);
        long currentTime = PlayPanel.tickTime;

        if (currentTime - appearTime > disappearTime) {
            int x = wizard.getXPosition();
            int y = wizard.getYPosition();
            hall.getGrid()[x][y] = null;
            wizard.setActive(false);
        }
    }
    
}
