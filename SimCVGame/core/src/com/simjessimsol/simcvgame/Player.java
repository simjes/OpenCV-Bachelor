package com.simjessimsol.simcvgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Player {
    private int x;
    private int y;
    private float width;
    private float height;
    private TextureAtlas characterAtlas;
    private Animation characterAnimation;

    public Player(int startX, int startY, float width, float height) {
        this.width = width;
        this.height = height;
        x = startX;
        y = startY;
        characterAtlas = new TextureAtlas(Gdx.files.internal("gc.pack"));
        characterAnimation = new Animation(1 / 15f, characterAtlas.getRegions(), Animation.PlayMode.LOOP);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //TODO: x bli forskjøvet slik at point.x som opencv fant er x + width / 2 som er midten av fig.
    public void setX(int x) {
        this.x = x - ((int) width / 2);
    }

    public void setY(int y) {
        this.y = y - ((int) height / 2);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void dispose() {
        characterAtlas.dispose();
    }

    public Animation getCharacterAnimation() {
        return characterAnimation;
    }
}
