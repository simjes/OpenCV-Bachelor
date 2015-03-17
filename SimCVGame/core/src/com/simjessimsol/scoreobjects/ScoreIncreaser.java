package com.simjessimsol.scoreobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.simjessimsol.simcvgame.Player;

import java.util.Random;

public abstract class ScoreIncreaser {
    protected Vector2 position;
    protected int fallspeed;
    protected Rectangle hitbox;
    protected Texture texture;
    protected int scoreModifier;
    protected int width;
    protected int height;

    public ScoreIncreaser(int width, int height) {
        this.width = width;
        this.height = height;

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        Random xPositionRandomizer = new Random();
        position.x = xPositionRandomizer.nextInt(screenWidth + 1);
        position.y = screenHeight;
        hitbox = new Rectangle(position.x, position.y, width, height);
    }

    public abstract void update();

    public boolean collides(Player player) {
        return Intersector.overlaps(hitbox, player.getHitbox());
    }

    public Texture getTexture() {
        return texture;
    }

    public int getScoreModifier() {
        return scoreModifier;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void dispose() {
        texture.dispose();
    }
}
