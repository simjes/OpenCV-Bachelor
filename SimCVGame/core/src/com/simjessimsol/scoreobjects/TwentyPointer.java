package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TwentyPointer extends ScoreIncreaser {
    public TwentyPointer(int width, int height, int fallspeed) {
        super(width, height, fallspeed);
        scoreModifier = 20;
        texture = new Texture(Gdx.files.internal("red.png"));
    }
}
