package com.rokue.game.entities;

import com.rokue.game.map.Hall;

public abstract class WizardBehavior {
    
    protected  Hall hall;
    protected Wizard wizard;

    public WizardBehavior(Hall hall, Wizard wizard) {
        this.hall = hall;
        this.wizard = wizard;
    } 

    public abstract void behave();
    
}
