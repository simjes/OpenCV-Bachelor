package com.simjessimsol.simcvgame;

import com.badlogic.gdx.Gdx;
import com.simjessimsol.scoreobjects.Bomb;
import com.simjessimsol.scoreobjects.ScoreIncreaser;
import com.simjessimsol.scoreobjects.TenPointer;
import com.simjessimsol.scoreobjects.FiftyPointer;

import java.util.ArrayList;

public class Spawner {

    private ArrayList<ScoreIncreaser> scoreIncreasers;

    private int fallspeed = 3;
    private float gdxWidth;
    private float gdxHeight;

    public Spawner(ArrayList<ScoreIncreaser> scoreIncreasers) {
        this.scoreIncreasers = scoreIncreasers;
        gdxWidth = Gdx.app.getGraphics().getWidth();
        gdxHeight = Gdx.app.getGraphics().getHeight();
    }

    public void spawnTenPointers() {
        TenPointer tenPointer = new TenPointer((int) gdxWidth / 25, (int) gdxHeight / 10, fallspeed);
        scoreIncreasers.add(tenPointer);
    }

    public void spawnFiftyPointers() {
        FiftyPointer fiftyPointer = new FiftyPointer((int) gdxWidth / 50, (int) gdxHeight / 25, fallspeed * 3);
        scoreIncreasers.add(fiftyPointer);
    }

    public void spawnBombs() {
        Bomb bomb = new Bomb((int) gdxWidth / 35, (int) gdxHeight / 15, fallspeed * 4);
        scoreIncreasers.add(bomb);
    }

    public void speedUp() {
        if (fallspeed < 10) {
            fallspeed++;
        }
    }
}
