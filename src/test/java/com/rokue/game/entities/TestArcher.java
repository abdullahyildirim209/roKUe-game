package com.rokue.game.entities;

import junit.framework.TestCase;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.rokue.game.audio.SoundManager;
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
 * - May trigger a sound effect through `SoundManager.playSound`.
 *
 * Effects:
 * - Updates `sideOfHero` to indicate whether the archer is to the left or right of the hero.
 * - If the hero is within range (Manhattan distance < 4), not cloaked, and the attack interval has elapsed:
 *     - Decreases the hero's health by 1.
 *     - Updates `lastAttackTime` to the current tick time.
 *     - Plays the "archer" sound effect via `SoundManager`.
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
    public void testUpdate_AttackHero() {
        // Setup
        hero.setXPosition(5);
        hero.setYPosition(5);
        hero.setHealth(10);
        hero.setCloakActive(false);
        PlayPanel.tickTime = 75;

        // Archer's initial position
        archer.setXPosition(4);
        archer.setYPosition(7);

        // Execute
        archer.update();

        // Verify
        assertEquals(9, hero.getHealth()); // Health should decrease by 1
        assertEquals(75, archer.getLastAttackTime()); 
    }
    
    /*This method is to check that hero does not lose health when attacked by the Archer when the Archer
     * is further than a range of distance of 4 and the last attack time is correct
     */
    public void testUpdate_HeroOutOfRange() {
        // Setup
        hero.setXPosition(10);
        hero.setYPosition(15);
        hero.setCloakActive(false);
        hero.setHealth(3);
        PlayPanel.tickTime = 100;

        // Archer's initial position
        archer.setXPosition(4);
        archer.setYPosition(9);

        // Execute
        archer.update();

        // Verify
        assertEquals(3, hero.getHealth()); // Health should remain unchanged
        assertEquals(0, archer.getLastAttackTime()); //the last attack time should be 0 because hero is not affected
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

