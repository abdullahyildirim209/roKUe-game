package game;

import java.util.Random;

// use this for anything randomized so that we can extract the seed after the game
public class RNG extends Random {
    private int seed;

    public RNG(int seed) {
        super(seed);
        this.seed = seed;
    }

    public RNG() {
        this(new Random().nextInt());
    }

    public int getSeed() {
        return seed;
    }
}
