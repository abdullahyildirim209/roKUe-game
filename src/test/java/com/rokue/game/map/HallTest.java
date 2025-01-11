package com.rokue.game.map;

import com.rokue.game.entities.Prop;
import com.rokue.game.utils.RNG;

import junit.framework.TestCase;

public class HallTest extends TestCase {


    public void testObjectPlacementOnGrid() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);

        // Place a prop at a specific position
        int x = 5, y = 5;
        Prop prop = new Prop(4);
        prop.place(x, y, hall);

        // Verify that the prop is placed on the grid
        assertEquals("Prop should be placed at the correct grid position", prop, hall.getGrid()[x][y]);
        assertTrue("Prop should be present in the hall's props set", hall.getProps().contains(prop));
    }

    public void testRandomEmptyTilePosition() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);
    
        // Place an entity to occupy some positions
        hall.getGrid()[5][5] = new Prop(4);
        hall.getGrid()[6][6] = new Prop(4);
    
        // Call the method to get a random empty tile position
        int[] position = hall.getRandomEmptyTilePosition();
    
        // Verify that the returned position is valid and unoccupied
        assertNotNull("Position should not be null", position);
        assertTrue("Position should be within grid bounds",
                position[0] >= 0 && position[0] < Hall.getTiles() &&
                position[1] >= 0 && position[1] < Hall.getTiles());
        assertNull("Position should be empty", hall.getGrid()[position[0]][position[1]]);
    }

    public void testChangeRunePosition() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);
    
        // Add props to the hall
        Prop prop1 = new Prop(1);
        Prop prop2 = new Prop(2);
        prop1.place(5, 5, hall);
        prop2.place(6, 6, hall);
        hall.getProps().add(prop1);
        hall.getProps().add(prop2);
    
        // Set an initial rune holder
        hall.runeHolder = prop1;
    
        // Call changeRunePos and verify the rune holder changes
        hall.changeRunePos();
        assertNotSame("Rune holder should change to a different prop", prop1, hall.runeHolder);
    }

}