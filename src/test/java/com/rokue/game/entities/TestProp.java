package com.rokue.game.entities;

import com.rokue.game.map.Hall;
import com.rokue.game.utils.RNG;

import junit.framework.TestCase;

/**
 * This class tests the functionality of the Prop class.
 *
 * Requires:
 * - A valid Hall object in which the Prop can be placed.
 * - The Hall's hero must be set if testing chest or rune interactions that affect hero health or door states.
 *
 * Modifies:
 * - The hall's grid, when placing or removing the Prop.
 * - The hall's props or nonRuneProps sets (depending on the Prop's ID).
 * - Possibly the hero's health or the hall's doorOpen and heroExit flags when searching the Prop.
 *
 * Effects:
 * - Verifies that the Prop is correctly placed on the hall's grid.
 * - Tests Prop behaviors based on different IDs (e.g., door, chest, crate).
 * - Ensures searching a Prop triggers the appropriate changes (e.g., opening a door, granting health, removing itself).
 * - Checks the Y-pixel position offset for two-tile-high Props.
 */
public class TestProp extends TestCase {

    private Hall hall;
    private Hero hero;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Create a new Hall with a Hero for testing
        hall = new Hall(new RNG());
        hero = new Hero(null); // 'null' for input since it's not needed in this test
        hall.setHero(hero);
    }

    /**
     * Test that placing a Prop in the Hall correctly updates the Hall's grid
     * and the appropriate set (props or nonRuneProps).
     */
    public void testPlaceProp() {
        // ID < 4 => goes to nonRuneProps, ID >= 4 => goes to props
        int x = 5, y = 5;
        Prop propDoor = new Prop(0); // A door prop
        Prop propCrate = new Prop(4); // A crate prop

        // Place door
        propDoor.place(x, y, hall);
        assertSame("Door should be placed in nonRuneProps", propDoor, hall.getGrid()[x][y]);
        assertTrue("Door should be added to nonRuneProps", hall.getNonRuneProps().contains(propDoor));
        assertFalse("Door should not be in props set", hall.getProps().contains(propDoor));

        // Place crate
        int x2 = 6, y2 = 6;
        propCrate.place(x2, y2, hall);
        assertSame("Crate should be placed on the grid", propCrate, hall.getGrid()[x2][y2]);
        assertTrue("Crate should be added to props set", hall.getProps().contains(propCrate));
        assertFalse("Crate should not be in nonRuneProps", hall.getNonRuneProps().contains(propCrate));
    }

    /**
     * Test that searching a door Prop (ID=0 or ID=1) with the door open
     * causes the hero to exit (heroExit set to true).
     */
    public void testSearchDoorOpen() {
        // Create a door prop at ID=0, place it, and open the door
        Prop door = new Prop(0);
        door.place(3, 3, hall);
        hall.setDoorOpen(true);

        // Search the prop
        door.search();

        // Verify heroExit is set
        assertTrue("Hero should exit when searching door if door is open", hall.isHeroExit());
    }

    /**
     * Test searching a heart chest (ID=2, then ID=3) to ensure it transitions
     * correctly from closed to open, and eventually increases hero's health
     * when opened the second time.
     */
    public void testSearchHeartChest() {
        // Create a heart chest
        Prop heartChest = new Prop(2);
        heartChest.place(4, 4, hall);

        // 1) Search when ID=2 => becomes open chest (ID=3)
        heartChest.search();
        assertEquals("Prop ID should change to 3 (open chest)", 3, heartChest.ID);

        // 2) Search when ID=3 => hero gains health and prop disappears
        int initialHealth = hero.getHealth();
        heartChest.search();
        assertTrue("Hero's health should increase by 1", hero.getHealth() > initialHealth);
        assertNull("Prop should be removed from the hall's grid", hall.getGrid()[4][4]);
        assertFalse("Prop should be removed from nonRuneProps", hall.getNonRuneProps().contains(heartChest));
    }

    /**
     * Test that getYPixelPosition() returns one tile up, reflecting
     * that prop images are two tiles tall.
     */
    public void testGetYPixelPosition() {
        Prop tallProp = new Prop(4); // e.g., crate
        int xPos = 7, yPos = 7;
        tallProp.place(xPos, yPos, hall);

        int expected = yPos * Hall.getPixelsPerTile() - Hall.getPixelsPerTile();
        int actual = tallProp.getYPixelPosition();

        assertEquals("YPixelPosition should be one tile up", expected, actual);
    }
}
