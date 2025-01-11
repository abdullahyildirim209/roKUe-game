package com.rokue.game.entities;

import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;
import com.rokue.game.utils.RNG;

import junit.framework.TestCase;

public class TestHero extends TestCase {

    private Hall hall;
    private Hero hero;
    private Keyboard keyboard;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Create a new Hall, Keyboard and Hero
        hall = new Hall(new RNG());
        keyboard = new Keyboard();
        hero = new Hero(keyboard);
        hero.randomlyPlace(hall);
    }


    public void testAddToInventory() {
        // get inventory values before adding
        int i1_0 = hero.getInventory()[0];
        int i1_1 = hero.getInventory()[1];
        int i1_2 = hero.getInventory()[2];
        
        // add new enchantments to inventory
        hero.addToInventory(new RevealRune());
        hero.addToInventory(new CloakOfProtection());
        hero.addToInventory(new LuringGem());

        //  get inventory values after adding
        int[] i2 = hero.getInventory();

        // Verify
        assertEquals(i1_0 + 1, i2[0]);
        assertEquals(i1_1 + 1, i2[1]);
        assertEquals(i1_2 + 1, i2[2]);
    }

    public void testRemoveFromInventory() {
        // add each enchantment for testing
        hero.addToInventory(new RevealRune());
        hero.addToInventory(new CloakOfProtection());
        hero.addToInventory(new LuringGem());

        // tests for non-empty inventory

        int i0 = hero.getInventory()[0];    // get RevealRune amount in the inventory
        assertTrue(hero.removeFromInventory(new RevealRune()));
        assertEquals(i0 - 1, hero.getInventory()[0]);

        int i1 = hero.getInventory()[1];    // get CloakOfPRotection amount in the inventory
        assertTrue(hero.removeFromInventory(new CloakOfProtection()));
        assertEquals(i1 - 1, hero.getInventory()[1]);

        int i2 = hero.getInventory()[2];    // get LuringGem amount in the inventory
        assertTrue(hero.removeFromInventory(new LuringGem()));
        assertEquals(i2 - 1, hero.getInventory()[2]);

        // remove all CloakOfProtection
        while (hero.removeFromInventory(new CloakOfProtection())) {}    
        
        // test for empty inventory
        assertFalse(hero.removeFromInventory(new CloakOfProtection()));
    }

    public void testActivateCloak() {
        // Setup
        hero.addToInventory(new CloakOfProtection());
        hero.setCloakActive(false);
        keyboard.p = true;

        // Execute
        hero.activateCloak();

        // Verify
        assertTrue(hero.isCloakActive());
        assertTrue(hero.getCloakStartTime() > 0);
    }

    public void testSelectProp() {
        // create and place Prop
        Prop prop = new Prop(4);
        prop.place(hero.getXPosition(), hero.getYPosition() + 1, hall);
        
        // Execute
        hero.selectProp();

        // Verify selectedProp is prop
        assertEquals(prop, hero.selectedProp);
    }
}
