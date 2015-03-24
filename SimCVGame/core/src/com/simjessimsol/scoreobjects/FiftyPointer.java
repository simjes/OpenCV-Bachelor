package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class FiftyPointer extends ScoreIncreaser {
    public FiftyPointer(int width, int height, int fallspeed) {
        super(width, height, fallspeed);
        scoreModifier = 50;
        texture = new Texture(Gdx.files.internal("red.png"));
    }
}
