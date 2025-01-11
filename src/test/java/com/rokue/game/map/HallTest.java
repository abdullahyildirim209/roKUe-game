package com.rokue.game.map;

import com.rokue.game.entities.Prop;
import com.rokue.game.utils.RNG;

import junit.framework.TestCase;

public class HallTest extends TestCase {


    /*
    Overview of the Hall Class
    Purpose: Represents a dungeon hall in the game, containing a grid of tiles. Each tile may hold props, entities, or enchantments, and the hall manages game mechanics like spawning monsters, activating enchantments, and tracking the hero's state.
    Features:
    Grid-based structure (tiles x tiles grid).
    Tracks hero, monsters, props, and enchantments.
    Manages the placement of objects and interactions like collisions. 
    */


    /**
     * Abstract Function (AF)
     * AF(c) = A dungeon hall represented as:
     *   c.grid[x][y] = the entity occupying the tile at position (x, y), or null if the tile is empty.
     *   c.characters = the set of all characters (monsters or hero) present in the hall.
     *   c.props = the set of all props placed in the hall.
     *   c.enchantments = the set of all enchantments currently active in the hall.
     *   c.doorOpen = whether the door is open to allow the hero to exit.
     *   c.heroExit = whether the hero has exited through the door.
     */

     /**
     * RI(c) =
     *   - c.grid must be a valid 2D array of size [tiles][tiles].
     *   - All non-null elements in c.grid must be instances of Entity.
     *   - c.characters must contain only valid Character instances.
     *   - c.props and c.enchantments must contain only valid Prop and Enchantment instances, respectively.
     *   - Every entity in c.grid must be tracked in one of c.characters, c.props, or c.enchantments.
     *   - c.hero must be null or a valid Hero instance, and it must be tracked in c.characters.
     *   - c.doorOpen and c.heroExit must be boolean values.
     */


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

    
    
    public void testRepOk() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);

        // Verify the repOk holds on valid initialization
        assertTrue("The hall's representation invariant should hold on initialization", hall.repOk());

        // Introduce an invalid state: Add a Prop to the grid without adding it to the props set
        Prop rogueProp = new Prop(999); // Create a rogue Prop
        hall.getGrid()[4][4] = rogueProp; // Place it directly into the grid

        // Ensure this Prop is not added to the props set
        assertFalse("repOk should fail if grid elements aren't properly tracked in props", hall.repOk());
    }

    
    


}