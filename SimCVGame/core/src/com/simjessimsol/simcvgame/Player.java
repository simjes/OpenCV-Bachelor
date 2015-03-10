package com.simjessimsol.simcvgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Player {
    private int x;
    private int y;
    private TextureAtlas characterAtlas;
    private Animation characterAnimation;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;
        characterAtlas = new TextureAtlas(Gdx.files.internal("gc.pack"));
        characterAnimation = new Animation(1 / 30f, characterAtlas.getRegions());
    }

    public void dispose() {
        characterAtlas.dispose();
    }

    public Animation getCharacterAnimation() {
        return characterAnimation;
    }
}
