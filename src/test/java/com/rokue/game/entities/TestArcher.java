package com.rokue.game.entities;

import junit.framework.TestCase;
import java.awt.Image;
import java.awt.image.BufferedImage;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;
import com.rokue.game.utils.RNG;
import com.rokue.game.input.Keyboard;

//This class is to test the update method in Archer class
/**
 * Requires:
 * - `hall` must not be null.
 * - `hall.getHero()` must return a valid `Hero` object.
 * - `PlayPanel.tickTime` must be a valid long timestamp representing the current time in ticks.
 *
 * Modifies:
 * - The `sideOfHero` field of the `Archer` instance.
 * - The `lastAttackTime` field of the `Archer` instance.
 * - The health of the hero if the archer successfully attacks.
 * 
 *
 * Effects:
 * - Updates `sideOfHero` to indicate whether the archer is to the left or right of the hero.
 * - If the hero is within range (Manhattan distance < 4), not cloaked, and the attack interval has elapsed:
 *     - Decreases the hero's health by 1.
 *     - Updates `lastAttackTime` to the current tick time.
 * - Does nothing if the hero has exited the hall or is out of range.
 */

public class TestArcher extends TestCase {

    private Archer archer;
    private Hall hall;
    private Hero hero;
    private SpriteLoader spriteLoader;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        archer = new Archer();
        
        // Manually setting up mock objects
        hall = new Hall(new RNG()) {
            private Hero hero = new Hero(new Keyboard());

            @Override
            public Hero getHero() {
                return hero;
            }

            @Override
            public boolean isHeroExit() {
                return false;
            }
        };

        hero = hall.getHero();
        spriteLoader = new SpriteLoader() {
            private Image[] monsterSprites = new Image[3];

            @Override
            public Image[] getMonsterSprites() {
                return monsterSprites;
            }
        };

        // Attach hall to the Archer
        archer.setHall(hall); // Assuming there's a setter for Hall
    }
    
    /*This method is to check that the hero should lose health when attacked by the Archer when the Archer
     * is less than a range of distance of 4 and the last attack time is correct
     */
    public void testArcherUpdateDistance() {
        // Setup
        hero.setXPosition(5);
        hero.setYPosition(5);
        hero.setHealth(3);
        hero.setCloakActive(false); //Cloak is not active
        PlayPanel.tickTime = 75;
        
        // Archer's initial position |5-2| + |5-9| >= 4
        archer.setXPosition(2);
        archer.setYPosition(9);

        // Execute
        archer.update();

        // Verify
        assertEquals(3, hero.getHealth()); // Hero is not within the range, health remains unchanged
        assertEquals(0, archer.getLastAttackTime()); // Hero is not within the range, attack is unsuccessful, last attack time should be 0
        
        // Archer's new position |5-4| + |5-7| < 4
        archer.setXPosition(4);
        archer.setYPosition(7);

        // Execute
        archer.update();

        // Verify
        assertEquals(2, hero.getHealth()); // Hero is within the range, health should decrease by 1
        assertEquals(PlayPanel.tickTime, archer.getLastAttackTime()); // Hero is within the range, attack is successful, last attack time should be updated to tickTime
    }
    
    /*This method is to check whether hero loses health when attacked by the Archer depending on the comparison of
     * the difference between the tickTime and LastAttackTime to the AttackInterval
     */
    public void testArcherUpdateAttackInterval() {
        // Setup
        hero.setXPosition(9);
        hero.setYPosition(6);
        // Archer's initial position |9-9| + |6-8| < 4. Hero is in the range
        archer.setXPosition(9);
        archer.setYPosition(8);
        hero.setHealth(4);
        hero.setCloakActive(false); //Cloak is not active
        PlayPanel.tickTime = 80;

        //tickTime - LastAttackTime < attackInterval, Hero should not be affected
        archer.setLastAttackTime(PlayPanel.tickTime - archer.getAttackInterval()+1);
        
        // Execute
        archer.update();

        // Verify
        assertEquals(4, hero.getHealth()); // Hero is not affected, health should remain unchanged
        assertEquals(PlayPanel.tickTime - archer.getAttackInterval()+1, archer.getLastAttackTime());
        
        //tickTime - LastAttackTime > attackInterval, Hero should be affected
        archer.setLastAttackTime(PlayPanel.tickTime - archer.getAttackInterval()-1);
        
        // Execute
        archer.update();

        // Verify
        assertEquals(3, hero.getHealth()); // Hero is affected, health should decrease by 1
        assertEquals(PlayPanel.tickTime, archer.getLastAttackTime()); //the LastAttackTime should be updated to the tickTime
    }
    
    /*This method is to check whether hero loses health when attacked by the Archer depending on the activity
     * of the Cloak of Protection and whether last attack time is correct
     */
    public void testArcherUpdateHeroCloakActivity() {
        // Setup
        hero.setXPosition(10);
        hero.setYPosition(15);
        // Archer's initial position |10-8| + |15-14|< 4. Hero is in the range
        archer.setXPosition(8);
        archer.setYPosition(14);
        hero.setHealth(5);
        PlayPanel.tickTime = 100;

        //Cloak is active
        hero.setCloakActive(true);
        
        // Execute
        archer.update();

        // Verify
        assertEquals(5, hero.getHealth()); // Cloak is active, health should remain unchanged
        assertEquals(0, archer.getLastAttackTime()); //the last attack time should be 0 because hero is not affected
        
        //Cloak is not active
        hero.setCloakActive(false);
        
        // Execute
        archer.update();

        // Verify
        assertEquals(4, hero.getHealth()); // Cloak is not active, health should decrease by 1
        assertEquals(PlayPanel.tickTime, archer.getLastAttackTime()); //the last attack time should be updated to tick time as Hero is affected
    }
    
    public void testGetSprite() {
        // Create mock images for testing
        Image sideOfHeroSprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Image notSideOfHeroSprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // Mock sprite loader
        SpriteLoader spriteLoader = new SpriteLoader() {
            private final Image[] monsterSprites = new Image[3];

            @Override
            public Image[] getMonsterSprites() {
                monsterSprites[1] = sideOfHeroSprite;
                monsterSprites[2] = notSideOfHeroSprite;
                return monsterSprites;
            }
        };

        // Test when the archer is on the hero's side
        archer.setSideOfHero(true); // Assuming there's a setter for this in testing
        Image sprite = archer.getSprite(spriteLoader);
        assertSame(sideOfHeroSprite, sprite);

        // Test when the archer is not on the hero's side
        archer.setSideOfHero(false);
        sprite = archer.getSprite(spriteLoader);
        assertSame(notSideOfHeroSprite, sprite);
    }

}
