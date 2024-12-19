package game;

import java.util.HashSet;
import java.util.Random;

public class Hall {
    static final int tiles = 18; // effectively 16x16 grid (outer part is walls)
    static final int pixelsPerTile = 16; // 16x16 pixel squares

    Entity[][] grid = new Entity[tiles][tiles];
    HashSet<Entity> entities = new HashSet<>();
    
    public Hall() {
        for (int y = 0; y < tiles; y++) {
            for (int x = 0; x < tiles; x++) {
                if (y == 0 || y == 17 || x == 0 || x == 17) {
                    new Crate(0).place(x, y, this);

                }
            }
        }
    }

    public int getObjectCount() {
        int count = 0;
        for (int x = 0; x < tiles; x++) {
            for (int y = 0; y < tiles; y++) {
                if (grid[x][y] != null) count++;
            }
        }
        return count;
    }
    
    public boolean meetsObjectCriteria(int requiredObjects) {
        return getObjectCount() >= requiredObjects;
    }
    

    int[] getRandomEmptyTilePosition() {
        int[] position = new int[2];
        
        Random random = new Random();
        int tileCounter = random.nextInt((tiles * tiles) - entities.size());

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

    boolean checkCollision(Entity entity, int x, int y) {
        if (grid[x][y] == null) return false;
        return grid[x][y].collisionArea.intersects(entity.collisionArea);
    }
}
