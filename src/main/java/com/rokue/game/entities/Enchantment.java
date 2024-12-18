package com.rokue.game.entities;

import com.rokue.game.map.Hall;

public abstract class Enchantment extends Entity {
    protected boolean pickable = true;
    protected boolean selected = false;

    
    public Enchantment() {
        super();
    }

    public boolean isPickable() {
        return pickable;
    }

    public void setPickable(boolean p) {
        pickable = p;
    }

    public void setSelected(boolean s) {
        selected = s;
    }

    @Override
    public boolean isEnchantment() {
        return true;
    }

    @Override
    public void place(int xPosition, int yPosition, Hall hall) {
        super.place(xPosition, yPosition, hall);
        hall.getEnchantments().add(this);
    }

        @Override
    public int getShadowX() {
        return getXPixelPosition() + 3;
    }

    @Override
    public int getShadowY() {
        return getYPixelPosition() + 15;
    }

    @Override
    public int getShadowHeight() {
        return 9;
    }

    @Override
    public int getShadowWidth() {
        return 10;
    }
}
