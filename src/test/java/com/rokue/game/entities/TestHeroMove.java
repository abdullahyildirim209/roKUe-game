package com.rokue.game.entities;

import junit.framework.TestCase;
import com.rokue.game.entities.Hero;
import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;
import com.rokue.game.utils.RNG;
import com.rokue.game.entities.Prop;

/**
* Requires:
*  - A valid Hall object (this.hall) must already be set.
*  - A valid Keyboard object (this.keyboard) must be linked to the Hero.
*
* Modifies:
*  - this.xPixelPosition, this.yPixelPosition (they may be decremented or incremented).
*  - this.running and this.runStage (used for animations).
*  - this.looking and this.lastSideLooking (used for sprite orientation).
*
* Effects:
*  - Moves the Hero’s pixel position by 1 in the direction(s) indicated by the pressed direction keys
*  - Prevents movement if a collision is detected via check*Collision() methods.
*  - Updates the tile-based position by calling moveTo(...) at the end of the method.
*  - Updates running animation timing variables (runTimer, runStage).
*/

public class TestHeroMove extends TestCase {

    private Hero hero;
    private Hall hall;
    private Keyboard keyboard;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        keyboard = new Keyboard();
        hall = new Hall(new RNG(0));
        
        // Create the hero and place it in the hall at tile (5,5).
        hero = new Hero(keyboard);
        hero.place(5, 5, hall); 
    }

    /**
     * Test 1: Move Left with No Collision
     *  - Press the left key
     *  - Ensure the hero's xPixelPosition is decremented
     *  - Ensure hero is looking left (2)
     *  - Ensure hero is running
     */
    public void testMoveLeftNoCollision() {
        int initialXPixel = hero.getXPixelPosition();
        int initialYPixel = hero.getYPixelPosition();
        
        keyboard.left = true;
        hero.move();
        
        // The hero should move left by 1 pixel
        assertEquals("Hero should have moved left by 1 pixel", initialXPixel - 1, hero.getXPixelPosition());
        // Y should remain unchanged
        assertEquals("Hero's Y-pixel position should remain unchanged", initialYPixel, hero.getYPixelPosition());
        // Looking at left (2)
        assertEquals("Hero should look left", 2, hero.looking);   
        // hero should be in running state
        assertTrue("Hero should be in running state", hero.running);
    }

    /**
     * Test 2: Move Right with a Collision
     *  - Place a blocking Prop to the immediate right, so the hero cannot move
     *  - Press the right key
     *  - Hero should not move due to collision
     */
    public void testMoveRightCollision() {
        // Place a Prop at tile (6,5) to block rightward movement.
        // Hero tries to move from xPixelPosition = 5*16 to (5*16) + 1
        // but the tile (6,5) is occupied by a prop, there shouldnt be movement but the hero should look right (6)
        Prop blockingProp = new Prop(0);
        blockingProp.place(6, 5, hall);

        int initialXPixel = hero.getXPixelPosition();
        int initialYPixel = hero.getYPixelPosition();
        
        keyboard.right = true;
        hero.move();

        // The hero should NOT move horizontally because of collision
        assertEquals("Hero's X-pixel should not have changed due to collision", initialXPixel, hero.getXPixelPosition());
        // Y should remain unchanged
        assertEquals("Hero's Y-pixel position should remain unchanged", initialYPixel, hero.getYPixelPosition());
        // hero should be looking right (6)
        assertEquals("Hero should look right", 6, hero.looking);   
        // hero should not be in running state
        assertFalse("Hero should not be in running state", hero.running);
    }

    /**
     * Test 3: Moving and stopping by pressing the opposing key
     *  - Press up
     *  - move one tick
     *  - press down without releasing up
     *  - move one tick
     *  - Hero should have moved only by 1 pixel up, not in running state and still facing up
     */
    public void testMoveDiagonalNoCollision() {
        int initialXPixel = hero.getXPixelPosition();
        int initialYPixel = hero.getXPixelPosition();

        keyboard.up = true;
        hero.move();
        keyboard.down = true;
        hero.move();

        // Y should be decremented by 1
        assertEquals("Hero's X-pixel should not have changed due to collision", initialXPixel - 1, hero.getXPixelPosition());
        // X should remain unchanged
        assertEquals("Hero's Y-pixel position should remain unchanged", initialYPixel, hero.getYPixelPosition());
        // hero should be looking up (4)
        assertEquals("Hero should look up", 4, hero.looking);
        // hero should not be in running state
        assertFalse("Hero should not be in running state", hero.running);
    }
}
