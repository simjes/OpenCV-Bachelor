package com.simjessimsol.simcvgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private float width;
    private float height;
    private TextureAtlas characterAtlas;
    private Animation characterAnimation;
    private Rectangle hitbox;

    public Player(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        characterAtlas = new TextureAtlas(Gdx.files.internal("gc.pack"));
        characterAnimation = new Animation(1 / 15f, characterAtlas.getRegions(), Animation.PlayMode.LOOP);
        hitbox = new Rectangle();
    }

    public void update() {
        hitbox.set(position.x, position.y, width, height);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    //TODO: x bli forskjøvet slik at point.x som opencv fant er x + width / 2 som er midten av fig.
    public void setX(int x) {
        position.x = x - width / 2;
    }

    public void setY(int y) {
        position.y = y - height / 2;
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

    public Rectangle getHitbox() {
        return hitbox;
    }
}
