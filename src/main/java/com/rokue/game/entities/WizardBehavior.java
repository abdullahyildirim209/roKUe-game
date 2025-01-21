package com.rokue.game.entities;

import java.io.Serializable;

import com.rokue.game.map.Hall;

public abstract class WizardBehavior implements Serializable{
    private static final long serialVersionUID = 1L;
    protected  Hall hall;
    protected Wizard wizard;

    public WizardBehavior(Hall hall, Wizard wizard) {
        this.hall = hall;
        this.wizard = wizard;
    } 

    public abstract void behave();
    
}
