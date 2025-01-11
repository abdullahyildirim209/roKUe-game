package com.rokue.game.entities;

import com.rokue.game.map.Hall;
import com.rokue.game.utils.RNG;
import junit.framework.TestCase;

public class FighterTests extends TestCase {

    public void testMoveTowardsBlocked() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);
        Fighter fighter = new Fighter();
        fighter.place(5, 5, hall);
        hall.getGrid()[6][5] = new Fighter();

        fighter.moveTowards(7, 5, false);
        assertEquals(5, fighter.getXPosition());
        assertEquals(5, fighter.getYPosition());
    }

    public void testMoveTowardsRandomMove() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);
        Fighter fighter = new Fighter();
        fighter.place(5, 5, hall);

        fighter.moveTowards(7, 5, true);
        int x = fighter.getXPosition();
        int y = fighter.getYPosition();

        assertTrue(Math.abs(x - 5) <= 1);
        assertTrue(Math.abs(y - 5) <= 1);
    }


    public void testFighterDoesNotAttackHeroOutOfRange() {
        RNG rng = new RNG();
        Hall hall = new Hall(rng);

        Hero hero = new Hero(null);
        hero.place(2, 2, hall);
        hall.setHero(hero);

        Fighter fighter = new Fighter();
        fighter.place(4, 5, hall);

        int initialHealth = hero.getHealth();

        for (int i = 0; i < 5; i++) {
            fighter.update();
        }

        int finalHealth = hero.getHealth();
        assertEquals("The Fighter should not attack the Hero if out of range", initialHealth, finalHealth);
    }


}