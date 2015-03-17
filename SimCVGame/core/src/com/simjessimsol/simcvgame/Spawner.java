package com.simjessimsol.simcvgame;

import com.simjessimsol.scoreobjects.ScoreIncreaser;
import com.simjessimsol.scoreobjects.TenPointer;
import com.simjessimsol.scoreobjects.TwentyPointer;

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

    public void spawnTwentyPointers() {
        TwentyPointer twentyPointer = new TwentyPointer(20, 20, fallspeed * 3);
        scoreIncreasers.add(twentyPointer);
    }

    public void speedUp() {
        if (fallspeed < 10) {
            fallspeed++;
        }
    }
}
