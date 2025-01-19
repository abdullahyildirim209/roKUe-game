package com.rokue.game.entities;

import java.io.Serializable;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;

public class WizardBehavior2 extends WizardBehavior implements Serializable{
    private static final long serialVersionUID = 1L;
    private final long changeInterval = 3 * 60;
    private long lastChangeTime = 0;
    
    public WizardBehavior2(Hall hall, Wizard wizard) {
        super(hall, wizard);
    }

    @Override
    public void behave() {
        wizard.setSpriteNum(1);
        long currentTime = PlayPanel.tickTime;

        if (!hall.isHeroExit() && !hall.isDoorOpen()){
            if (currentTime - lastChangeTime > changeInterval) {
                hall.changeRunePos();
                System.out.println("Wizard: Rune pos changed");
                SoundManager.playSound("wizard");
                lastChangeTime = currentTime;
            }
        }
    }
    
}
