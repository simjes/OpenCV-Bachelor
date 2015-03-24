package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TenPointer extends ScoreIncreaser {

    public TenPointer(int width, int height, int fallspeed) {
        super(width, height, fallspeed);
        scoreModifier = 10;
        texture = new Texture(Gdx.files.internal("apple.png"));
    }
}
