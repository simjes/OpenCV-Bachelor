package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TenPointer extends ScoreIncreaser {

    public TenPointer(int width, int height) {
        super(width, height);
        scoreModifier = 10;
        texture = new Texture(Gdx.files.internal("apple.png"));
        fallspeed = 10;
    }

    @Override
    public void update() {
        position.y -= fallspeed;
        hitbox.set(position.x, position.y, hitbox.width, hitbox.height);
        if (position.y < 0) {
            position.y = 500;
        }
    }
}
