package com.rokue.game.map;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.rokue.game.entities.*;

public class Hall {
    public static final int tiles = 18;
    public static final int pixelsPerTile = 16;

    private final Entity[][] grid = new Entity[tiles][tiles];
    private final Set<Entity> entities = new HashSet<>();
    private final Random random = new Random();
    private Hero hero;

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void addEntity(Entity entity, int x, int y) {
        if (grid[x][y] == null) {
            grid[x][y] = entity;
            entities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        grid[entity.getXPosition()][entity.getYPosition()] = null;
        entities.remove(entity);
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public boolean isPositionEmpty(int x, int y) {
        if (x < 0 || x >= tiles || y < 0 || y >= tiles) {
            return false;
        }
        return grid[x][y] == null;
    }

    public void processEntities() {
        Set<Entity> toRemove = new HashSet<>();

        for (Entity entity : entities) {
            if (entity instanceof LuringGem) {
                LuringGem gem = (LuringGem) entity;

                for (Entity potentialFighter : entities) {
                    if (potentialFighter instanceof Fighter) {
                        Fighter fighter = (Fighter) potentialFighter;

                        if (fighter.getXPosition() == gem.getXPosition() &&
                                fighter.getYPosition() == gem.getYPosition()) {
                            toRemove.add(gem);
                        }
                    }
                }
            }
        }

        for (Entity entity : toRemove) {
            removeEntity(entity);
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
        } while (!isPositionEmpty(x, y)); // Pozisyon doluysa yeniden seç

        Crate crate = new Crate();
        crate.place(x, y);
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

    public void placeLuringGem(int x, int y, int direction) {
        LuringGem luringGem = new LuringGem();
        luringGem.place(x, y, this);

        for (Entity entity : entities) {
            if (entity instanceof Fighter) {
                ((Fighter) entity).followLuringGem(x, y);
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
}
