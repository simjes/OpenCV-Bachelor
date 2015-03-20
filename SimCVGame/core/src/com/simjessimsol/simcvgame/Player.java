package com.simjessimsol.simcvgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private float width;
    private float height;
    private Texture texture;
    private Rectangle hitbox;
    private int score;

    public Player(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        hitbox = new Rectangle();
        hitbox.set(position.x, height, width, 1);
        texture = new Texture(Gdx.files.internal("player.png"));
        score = 0;
    }

    public void update() {
        hitbox.set(position.x, height, width, 1);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getScore() {
        return score;
    }

    public void addToScore(int points) {
        score += points;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setX(int x) {
        position.x = x - width / 2;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void dispose() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }
}
