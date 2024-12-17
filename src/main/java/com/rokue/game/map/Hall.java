package com.rokue.game.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import com.rokue.game.entities.*;
import com.rokue.game.entities.GameObject;

public class Hall {
    public static final int tiles = 18;
    public static final int pixelsPerTile = 16;

    private final Entity[][] grid = new Entity[tiles][tiles];
    private final Set<Enchantment> enchantments = new HashSet<>();
    private final Set<GameObject> objects = new HashSet<>();
    private final Set<Monster> monsters = new HashSet<>();
    private final Set<Entity> others = new HashSet<>();
    private final Random random = new Random();
    private Hero hero;
    private Rune rune = new Rune();

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public Rune getRune() {
        return rune;
    }


    public void addEntity(Entity entity, int x, int y) {
        if (grid[x][y] == null) {
            grid[x][y] = entity;
            if (entity instanceof Monster){
                monsters.add((Monster) entity);
            }
            else if (entity instanceof GameObject) {
                objects.add((GameObject) entity);
            }
            else if (entity instanceof Enchantment) {
                enchantments.add((Enchantment) entity);
            }
            else others.add(entity);
        }
    }


    public void removeEntity(Entity entity) {
        grid[entity.getXPosition()][entity.getYPosition()] = null;
        if (entity instanceof Monster){
            monsters.remove(entity);
        }
        else if (entity instanceof GameObject) {
            objects.remove(entity);
        }
        else if (entity instanceof Enchantment) {
            enchantments.remove(entity);
        }
        else others.remove(entity);
    }

    public Set<Monster> getMonsters() {
        return monsters;
    }

    public Set<GameObject> getObjects() {
        return objects;
    }

    public Set<Enchantment> getEnchantments() {
        return enchantments;
    }

    public Set<Entity> getOthers() {
        return others;
    }

    public boolean isPositionEmpty(int x, int y) {
        if (x < 0 || x >= tiles || y < 0 || y >= tiles) {
            return false;
        }
        return grid[x][y] == null;
    }

    public void processEntities() {
        Set<Entity> toRemove = new HashSet<>();
        boolean allFightersReached = true;

        for (Enchantment e : enchantments) {
            if (e instanceof LuringGem) {
                LuringGem gem = (LuringGem) e;

                for (Monster potentialFighter : monsters) {
                    if (potentialFighter instanceof Fighter) {
                        Fighter fighter = (Fighter) potentialFighter;

                        if (Math.abs(fighter.getXPosition() - gem.getXPosition()) <= 2 &&
                                Math.abs(fighter.getYPosition() - gem.getYPosition()) <= 2) {
                            allFightersReached &= true;
                            toRemove.add(gem);
                        }
                        else allFightersReached &= false;
                    }
                }
            }
        }

        if (allFightersReached) {
            for (Entity entity : toRemove) {
                removeEntity(entity);
            }

            for (Monster m : monsters){
                if (m instanceof Fighter){
                    ((Fighter) m).reset();
                }
            }
        }
    }

    public Entity checkForObjects() {
        int hx = hero.getXPosition();
        int hy = hero.getYPosition();
        Entity x = null;
        if (!isPositionEmpty(hx+1, hy)) {
            x = getEntityAt(hx+1, hy);
            if (x instanceof GameObject) return x;
        }
        if (!isPositionEmpty(hx, hy+1)) {
            x = getEntityAt(hx, hy+1);
            if (x instanceof GameObject) return x;
        }
        if (!isPositionEmpty(hx-1, hy)) {
            x = getEntityAt(hx-1, hy);
            if (x instanceof GameObject) return x;
        }
        if (!isPositionEmpty(hx, hy-1)) {
            x = getEntityAt(hx, hy-1);
            if (x instanceof GameObject) return x;
        }

        return null;

    }

    public void openObject(Entity e) {
        if (e instanceof GameObject){
            Entity x = ((GameObject) e).getX();
            removeEntity(e);
            if (x != null){
                x.setPosition(e.getXPosition(), e.getYPosition());
                addEntity(x, e.getXPosition(), e.getXPosition());
            }
        }
    }


    public void placeRandomCrate() {
        int x, y;
        int centerX = tiles / 2;
        int centerY = tiles / 2;
        int radius = 4;

        do {
            x = centerX - radius + random.nextInt(2 * radius + 1);
            y = centerY - radius + random.nextInt(2 * radius + 1);
        } while (!isPositionEmpty(x, y));

        Crate crate = new Crate();
        crate.place(x, y);

        if (!rune.isPositionSet()) {
            rune.setPosition(crate.getXPosition(), crate.getYPosition());
            crate.setX(rune);
        }

        addEntity(crate, x, y);

        System.out.println("Crate added at: (" + x + ", " + y + ")");
    }

    public void placeRandomArcher() {
        Archer archer = new Archer();
        int x, y;
        do {
            x = (int) (Math.random() * tiles);
            y = (int) (Math.random() * tiles);
        } while (!isPositionEmpty(x, y));

        archer.place(x, y, this);
        addEntity(archer, x, y);
    }

    public void placeRandomFighter() {
        Fighter fighter = new Fighter();
        int x, y;
        do {
            x = random.nextInt(tiles);
            y = random.nextInt(tiles);
        } while (!isPositionEmpty(x, y));

        fighter.place(x, y, this);
        addEntity(fighter, x, y);
    }

    public void placeRandomWizard() {
        Wizard wizard = new Wizard();
        int x, y;
        do {
            x = (int) (Math.random() * tiles);
            y = (int) (Math.random() * tiles);
        } while (!isPositionEmpty(x, y));

        wizard.place(x, y, this);
        addEntity(wizard, x, y);
    }

    public void placeLuringGem(int x, int y, int direction) {
        LuringGem luringGem = new LuringGem();
        luringGem.place(x, y, this);

        for (Monster monster : monsters) {
            if (monster instanceof Fighter) {
                ((Fighter) monster).followLuringGem(x, y);
            }
        }

        addEntity(luringGem, x, y);
    }

    public Entity getEntityAt(int x, int y) {
        if (x >= 0 && x < tiles && y >= 0 && y < tiles) {
            return grid[x][y];
        }
        return null;
    }

    public void changeRunePos() {
        if (hero.getHealth() > 0 && !(getEntityAt(rune.getXPosition(), rune.getYPosition()) instanceof Rune)) {
            GameObject newObj = null;
            GameObject oldObj = (GameObject) getEntityAt(rune.getXPosition(), rune.getYPosition());
            ArrayList<GameObject> olist = new ArrayList<>();
            for (GameObject obj : objects){
                if (obj.caryRune() && obj != oldObj) {
                    olist.add(obj);
                }
            }
            if (olist.size() > 0 && oldObj != null) {
                newObj = olist.get(random.nextInt(olist.size()));
                oldObj.setX(newObj.getX());
                newObj.setX(rune);
                rune.setPosition(newObj.getXPosition(), newObj.getYPosition());
            }
        }
    }
}
