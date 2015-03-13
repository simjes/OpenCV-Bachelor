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

    //TODO: x,y nede i venstre hjørne av charen, endre?
    public int getX() {
        return x;// - ((int) width / 2);
    }

    public int getY() {
        return y;// - ((int) height / 2);
    }

    public void setX(int x) {
        this.x = x;// - ((int) width / 2);
    }

    public void setY(int y) {
        this.y = y;// - ((int) height / 2);
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
