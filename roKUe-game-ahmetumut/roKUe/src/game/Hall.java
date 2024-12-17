package game;

import java.util.HashSet;

public class Hall {
    static final int tiles = 18; // effectively 16x16 grid (outer part is filled with invisible props to act as walls)
    static final int pixelsPerTile = 16; // 16x16 pixel squares

    boolean heroExit = false; // true when hero leaves through the door, playMode switches to the next hall when true
    boolean doorOpen = false;
    public Prop runeHolder;

    int time;

    Entity[][] grid = new Entity[tiles][tiles];
    HashSet<Character> characters = new HashSet<>();
    HashSet<Prop> props = new HashSet<>();
    HashSet<Prop> nonRuneProps = new HashSet<>();

    RNG RNG;
    
    public Hall(RNG RNG) {
        this.RNG = RNG;

        // cover the outer edges with invisible props to prevent going out of bounds
        for (int y = 0; y < tiles; y++) {
            for (int x = 0; x < tiles; x++) {
                if (y == 0 && (x == 8 || x == 9)) { // add the door
                    new Prop(x - 8).place(x, y, this);
                } else if (y == 0 || y == 17 || x == 0 || x == 17) {
                    new Prop(-1).place(x, y, this);
                }
            }
        }
    }

    // used for random enemy and hero spawning spawning
    int[] getRandomEmptyTilePosition() {
        int[] position = new int[2];
        
        int tileCounter = RNG.nextInt((tiles * tiles) - props.size() - characters.size() - nonRuneProps.size());

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
    boolean checkCollision(Entity entity, int x, int y) {
        if (grid[x][y] == null) return false;
        return grid[x][y].collisionArea.intersects(entity.collisionArea);
    }

    // used for getting the prop the hero is looking at
    Prop getProp(int[] coordinates) {
        if (grid[coordinates[0]][coordinates[1]] == null) return null;
        if (grid[coordinates[0]][coordinates[1]].isProp()) return (Prop) grid[coordinates[0]][coordinates[1]];
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
}
