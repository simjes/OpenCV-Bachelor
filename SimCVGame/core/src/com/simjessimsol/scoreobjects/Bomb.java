package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Bomb extends ScoreIncreaser {

    public Bomb(int width, int height, int fallspeed) {
        super(width, height, fallspeed);
        texture = new Texture(Gdx.files.internal("bomb.png"));
    }

}
