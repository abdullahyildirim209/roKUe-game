package com.rokue.game.entities;

public abstract class GameObject extends Entity{
    private Entity x;
    protected boolean canCaryRune;

    public boolean caryRune() {
        return canCaryRune;
    }

    public Entity getX() {
        return x;
    }

    public void setX(Entity x){
        this.x = x;
    }
    
}
