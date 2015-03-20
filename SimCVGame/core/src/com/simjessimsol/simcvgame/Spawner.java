package com.simjessimsol.simcvgame;

import com.simjessimsol.scoreobjects.Bomb;
import com.simjessimsol.scoreobjects.ScoreIncreaser;
import com.simjessimsol.scoreobjects.TenPointer;
import com.simjessimsol.scoreobjects.FiftyPointer;

import java.util.ArrayList;

public class Spawner {

    private ArrayList<ScoreIncreaser> scoreIncreasers;

    private int fallspeed = 3;

    public Spawner(ArrayList<ScoreIncreaser> scoreIncreasers) {
        this.scoreIncreasers = scoreIncreasers;
    }

    public void spawnTenPointers() {
        TenPointer tenPointer = new TenPointer(50, 50, fallspeed);
        scoreIncreasers.add(tenPointer);
    }

    public void spawnFiftyPointers() {
        FiftyPointer fiftyPointer = new FiftyPointer(20, 20, fallspeed * 3);
        scoreIncreasers.add(fiftyPointer);
    }

    public void spawnBombs() {
        Bomb bomb = new Bomb(60, 60, fallspeed * 4);
        scoreIncreasers.add(bomb);
    }

    public void speedUp() {
        if (fallspeed < 10) {
            fallspeed++;
        }
    }
}
