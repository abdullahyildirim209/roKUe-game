package com.rokue.game.utils;

import java.io.Serializable;

import com.rokue.game.map.Hall;
// plus other classes you need

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Hall[] halls;           // 2x2 or 4 or whichever you have
    private int currentHallIndex;   // if your game tracks which hall is active
    private int heroLives;          // or store inside your Hero object
    private long globalTime;        // or store inside each Hall’s time, etc.
    private int loadTime; 
    // Any other global-level data: scores, etc.

    // Constructors, getters, setters
    public GameState(Hall[] halls, int currentHallIndex, int heroLives, long globalTime, int time) {
        this.halls = halls;
        this.currentHallIndex = currentHallIndex;
        this.heroLives = heroLives;
        this.globalTime = globalTime;
        this.loadTime = time;
    }

    public Hall[] getHalls() {
        return halls;
    }

    public int getCurrentHallIndex() {
        return currentHallIndex;
    }

    public int getHeroLives() {
        return heroLives;
    }

    public long getGlobalTime() {
        return globalTime;
    }

    public int getTime(){
        return loadTime;
    }
}
