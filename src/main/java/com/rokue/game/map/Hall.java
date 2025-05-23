package com.rokue.game.map;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import com.rokue.game.entities.Archer;
import com.rokue.game.entities.Character;
import com.rokue.game.entities.CloakOfProtection;
import com.rokue.game.entities.Enchantment;
import com.rokue.game.entities.Entity;
import com.rokue.game.entities.ExtraLife;
import com.rokue.game.entities.ExtraTime;
import com.rokue.game.entities.Fighter;
import com.rokue.game.entities.Hero;
import com.rokue.game.entities.LuringGem;
import com.rokue.game.entities.Prop;
import com.rokue.game.entities.RevealRune;
import com.rokue.game.entities.Wizard;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.utils.RNG;

public class Hall implements Serializable {
    private static final long serialVersionUID = 1L;


    static final int tiles = 18; // effectively 16x16 grid (outer part is filled with invisible props to act as walls)
    static final int pixelsPerTile = 16; // 16x16 pixel squares

    boolean heroExit = false; // true when hero leaves through the door, playMode switches to the next hall when true
    boolean doorOpen = false;
    public Prop runeHolder;
    int time;
    int totalTime;
    Entity[][] grid = new Entity[tiles][tiles];
    HashSet<Character> characters = new HashSet<>();
    Hero hero = null;
    HashSet<Prop> props = new HashSet<>();
    HashSet<Prop> nonRuneProps = new HashSet<>();
    HashSet<Enchantment> enchantments = new HashSet<>();
    long lastRevealRuneTime = 0;
    int revealRuneX = 0;
    int revealRuneY = 0;
    boolean revealRuneactive = false; 
    long lastEnchantmentTime = 0;
    Enchantment lastEnchantment = null;
    LuringGem activeLuringGem = null;
    long lastMonsterSpawn = 0;
    public RNG RNG;
    
    public Hall(RNG RNG) {
        this.RNG = RNG;

        int randomDoorLeftX = RNG.nextInt(15) + 1; // random position for door

        // cover the outer edges with invisible props to prevent going out of bounds
        for (int y = 0; y < tiles; y++) {
            for (int x = 0; x < tiles; x++) {
                if (y == 0 && (x == randomDoorLeftX || x == (randomDoorLeftX + 1))) { // add the door
                    new Prop(x - randomDoorLeftX).place(x, y, this);
                } else if (y == 0 || y == 17 || x == 0 || x == 17) {
                    new Prop(-1).place(x, y, this);
                }
            }
        }
    }

    public static int getTiles() {
        return tiles;
    }

    public static int getPixelsPerTile() {
        return pixelsPerTile;
    }

    public boolean isHeroExit() {
        return heroExit;
    } 
    
    public void setHeroExit(boolean a) {
        heroExit = a;
    }

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(boolean a) {
        doorOpen = a;
    }

    public int getTime() {
        return time;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTime(int t) {
        time = t;
    }

    public void setTotalTime(int t) {
        totalTime = t;
    }

    public Entity[][] getGrid() {
        return grid;
    }

    public HashSet<Character> getCharacters() {
        return characters;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero h) {
        hero = h;
        lastEnchantmentTime = PlayPanel.tickTime;
        lastMonsterSpawn = PlayPanel.tickTime;
    }

    public HashSet<Prop> getProps() {
        return props;
    }

    public HashSet<Prop> getNonRuneProps() {
        return nonRuneProps;
    }

    public HashSet<Enchantment> getEnchantments() {
        return enchantments;
    }

    public RNG getRNG() {
        return RNG;
    }

    public void update() {
        // Remove Wizards if not active
        Iterator<Character> i = characters.iterator();
        while (i.hasNext()) {
            Character c = i.next();
            if (c instanceof Wizard) {
                if (!((Wizard) c).isActive()) {
                    i.remove();
                }
            }
        }

        // RevealRune deactivate
        if (PlayPanel.tickTime - lastRevealRuneTime > 10*60) {
            deactivateRevealRune();
        }

        // Random enchantment spawn
        if (PlayPanel.tickTime - lastEnchantmentTime > 12*60) {
            lastEnchantmentTime = PlayPanel.tickTime;
            int x = RNG.nextInt(5);
            Enchantment e = null;
            int[] pos = getRandomEmptyTilePosition();
            if (x == 0) {
                e = new CloakOfProtection();
                e.place(pos[0], pos[1], this);
            }
            else if (x == 1) {
                e = new RevealRune();
                e.place(pos[0], pos[1], this);
            }
            else if (x == 2) {
                e = new LuringGem();
                e.place(pos[0], pos[1], this);
            }
            else if (x == 3) {
                e = new ExtraLife();
                e.place(pos[0], pos[1], this);
            }
            else {
                e = new ExtraTime();
                e.place(pos[0], pos[1], this);
            }

            lastEnchantment = e;
        }

        // Enchantment delete if not picked
        if (PlayPanel.tickTime - lastEnchantmentTime > 6*60 && lastEnchantment != null) {
            enchantments.remove(lastEnchantment); 
            grid[lastEnchantment.getXPosition()][lastEnchantment.getYPosition()] = null;
            lastEnchantment = null;
        } 
        
        // Follow ActiveLuringGem
        if (activeLuringGem != null) {
            for (Character f : characters) {
                if (f instanceof Fighter) {
                    ((Fighter) f).followLuringGem();
                }
            }
        }

        // Delete LuringGem if all fighters are nearby
        if (activeLuringGem != null) {
            boolean allFightersReached = true;

            for (Character c : characters) {
                if (c instanceof Fighter) {
                    Fighter f = (Fighter) c;
                    if (Math.abs(f.getXPosition() - activeLuringGem.getXPosition()) <= 2 && Math.abs(f.getYPosition() - activeLuringGem.getYPosition()) <= 2) {
                        allFightersReached &= true;
                    }
                    else allFightersReached &= false;
                }
            }

            if (allFightersReached) {
                grid[activeLuringGem.getXPosition()][activeLuringGem.getYPosition()] = null;
                enchantments.remove(activeLuringGem);
                activeLuringGem = null;

                for (Character c : characters) {
                    if (c instanceof Fighter) {
                        ((Fighter) c).reset();
                    }
                }
            }
        }

        // Monster Spwaning
        if (PlayPanel.tickTime - lastMonsterSpawn > 8*60) {
            lastMonsterSpawn = PlayPanel.tickTime;
            int a = RNG.nextInt(3);
            Character c = null;
            if (a == 0) c = new Fighter();
            else if (a == 1) c = new Archer();
            else c = new Wizard();
            
            c.randomlyPlace(this);
        }
    }


    // used for random enemy and hero spawning spawning
    public int[] getRandomEmptyTilePosition() {
        int[] position = new int[2];
        
        int tileCounter = RNG.nextInt((tiles * tiles) - props.size() - characters.size() - nonRuneProps.size() - enchantments.size());

        for (int y = 0; y < tiles; y++) {
            for (int x = 0; x < tiles; x++) {
                if (grid[x][y] == null) {
                    if (tileCounter == 0) {
                        position[0] = x;
                        position[1] = y;
                        return position;
                    } else {
                        tileCounter--;
                    }
                }
            }
        }
        return null;
    }

    // used for checking the tiles hero is trying to walk to
    public boolean checkCollision(Entity entity, int x, int y) {
        if (grid[x][y] == null) return false;
        return grid[x][y].getCollisionArea().intersects(entity.getCollisionArea());
    }

    // used for getting the prop the hero is looking at
    public Prop getProp(int[] coordinates) {
        if (grid[coordinates[0]][coordinates[1]] == null) return null;
        if (grid[coordinates[0]][coordinates[1]].isProp()) return (Prop) grid[coordinates[0]][coordinates[1]];
        return null;
    }

    // used for getting the enchantment the hero is looking at
    public Enchantment getEnchantment(int[] coordinates) {
        if (grid[coordinates[0]][coordinates[1]] == null) return null;
        if (grid[coordinates[0]][coordinates[1]].isEnchantment()) return (Enchantment) grid[coordinates[0]][coordinates[1]];
        return null;
    }

    // used for spawning the rune in a random prop
    public Prop getRandomProp() {
        int randomIndex = RNG.nextInt(props.size());
        int currentIndex = 0;
        for (Prop prop : props) {
            if (currentIndex == randomIndex) return prop;
            currentIndex++;
        }
        return null;
    }

    public void changeRunePos() {
        if (props.size() >= 2) {
            Prop p = null;
            do {
                p = getRandomProp();
            } while (p == runeHolder);
            runeHolder = p;
        }
        System.out.println(runeHolder.getXPosition() + " " + runeHolder.getYPosition());
    }

    public void placeLuringGem(int x, int y) {
        if (activeLuringGem != null) return;
        else {
            LuringGem l = new LuringGem();
            l.setPickable(false);
            l.place(x, y, this);
            activeLuringGem = l;
        }
    }

    public LuringGem getActiveLuringGem() {
        return activeLuringGem;
    }

    public void revealRune() {
        lastRevealRuneTime = PlayPanel.tickTime;
        do {
            revealRuneX = runeHolder.getXPosition() - 3 + RNG.nextInt(4);
            revealRuneY = runeHolder.getYPosition() - 3 + RNG.nextInt(4);
        } while (!(revealRuneX > 0 && revealRuneX + 4 < tiles && revealRuneY > 0 && revealRuneY + 4 < tiles));
        System.out.println(revealRuneX + " " + revealRuneY);
        revealRuneactive = true;
    }

    public void deactivateRevealRune() {
        revealRuneactive = false;
    }

    public boolean isRevealRuneActive() {
        return revealRuneactive;
    }

    public int getRevealRuneX() {
        return revealRuneX;
    }

    public int getRevealRuneY() {
        return revealRuneY;
    }

    public boolean repOk() {
        // Check grid dimensions and validity
        if (grid == null || grid.length != tiles || grid[0].length != tiles) {
            return false;
        }
        for (int x = 0; x < tiles; x++) {
            for (int y = 0; y < tiles; y++) {
                if (grid[x][y] != null && !(grid[x][y] instanceof Entity)) {
                    return false;
                }
            }
        }
    
        // Check characters
        for (Character character : characters) {
            if (character == null || !(character instanceof Character)) {
                return false;
            }
        }
    
        // Check props
        for (Prop prop : props) {
            if (prop == null || !(prop instanceof Prop)) {
                return false;
            }
        }
    
        // Check enchantments
        for (Enchantment enchantment : enchantments) {
            if (enchantment == null || !(enchantment instanceof Enchantment)) {
                return false;
            }
        }
    
        // Check hero
        if (hero != null && !characters.contains(hero)) {
            return false;
        }
    
        // Check boolean values
        if (doorOpen != true && doorOpen != false) {
            return false;
        }
        if (heroExit != true && heroExit != false) {
            return false;
        }
    
        return true;
    }
}